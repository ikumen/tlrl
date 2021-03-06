/**
 * 
 */
const { Kafka, logLevel } = require('kafkajs');
const { Indexer } = require('./indexer');
const { Browser } = require('./browser');
const { ensureDirectoryExists, makePath, retry } = require('./helpers');

// List of Kafka brokers to connect to
const brokers = (process.env.FETCHER_KAFKA_BROKERS || 'localhost:9093').split(',');
const groupId = process.env.FETCHER_KAFKA_GROUPID || 'tlrl-fetcher';
const solrServer = process.env.FETCHER_SOLR_SERVER || 'localhost:8983';
// NOTHING=0, ERROR=1, WARN=2, INFO=4, DEBUG=5
const logLvl = process.env.FETCHER_LOG_LVL || logLevel.WARN; 
const timeout = process.env.TIMEOUT || 160000;
// Where we should archive PDF and mhtml files to
const archiveDir = process.env.FETCHER_ARCHIVE_DIR || './target/fetcher/archive';
const solrEndpoint = `http://${solrServer}/solr/tlrl/update`;

// Make sure the archive directory is available
ensureDirectoryExists(archiveDir, (err) => {
  if (err) {
    console.log(`Missing or invalid 'ARCHIVE_DIR' (path to archive directory)`);
    process.exit(1);
  }
  console.log(`Using ${archiveDir} as archive directory.`)
});

console.log(`-------------`);
console.log(`Starting TLRL Fetcher with the following configs:
FETCHER_KAFKA_BROKERS=${brokers}
FETCHER_SOLR_SERVER=${solrServer}
FETCHER_KAFKA_GROUPID=${groupId}
FETCHER_LOG_LVL=${logLvl}
FETCHER_ARCHIVE_DIR=${archiveDir}
TIMEOUT=${timeout}
-------------`);

/* Setup Kafka */
const kafka = new Kafka({
  logLevel: logLvl,
  clientId: 'fetcher',
  brokers,
  connectionTimeout: 20000,
  retry: {
    retries: 10
  }
});

const consumer = kafka.consumer({
  groupId,
  sessionTimeout: timeout
});
const admin = kafka.admin();
const producer = kafka.producer();
const logger = kafka.logger();
// Instance of puppeteer browser/page wrapper
const browser = new Browser({logger});
const indexer = new Indexer({solrEndpoint, logger});

/** Archives the given page, as PDF and mhtml formats */
const archivePage = async (baseDir, bookmark, page) => {
  const mhtmlFilename = makePath([baseDir, `${bookmark.id}.mht`]);
  const pdfFilename = makePath([baseDir, `${bookmark.id}.pdf`]);
  await page.mhtml({path: mhtmlFilename});
  await page.pdf({
    path: pdfFilename,
    printBackground: true
  });
}

const indexPage = async (bookmark, page) => {
  const content = await page.content();
  await indexer.add({...bookmark, content});
}

/** Handles bookmark.persisted events, fetching the webpage and indexing it. */
const onCreatedBookmark = ({baseDir, bookmark}) =>
  new Promise(async (resolve, reject) => {
    const { owner, webUrl } = bookmark;
    const ownerDir = makePath([baseDir, owner.id]);
    ensureDirectoryExists(ownerDir, err => {if (err) reject(err)});

    let page;
    try {
      page = await browser.load(webUrl.url);
      await archivePage(ownerDir, bookmark, page);
      await indexPage(bookmark, page);
      resolve(bookmark);
    } catch (err) {
      logger.info(`Caught: , ${err}`)
      reject(err);
    } finally {
      if (page) await page.close();
    }
});


/** Handles bookmark.updated events, sending the message to be index. */
const onUpdatedBookmark = async ({bookmarks}) => {
  await indexer.update(bookmarks);
}

const onDeletedBookmark = async ({bookmarks}) => {
  await indexer.delete(bookmarks);
}

const initializeTopics = async () => {
  try {
    await admin.connect();
    await admin.createTopics({
      topics: [
        {topic: 'created'},
        {topic: 'updated'},
        {topic: 'deleted'}
      ]
    });
  } finally {
    await admin.disconnect();
  }
}

const run = async ({baseDir}) => {
  //initializeTopics();
  await consumer.connect();
  await producer.connect();
  await consumer.subscribe({
    topic: /bookmark.(created|updated|deleted)/,
    fromBeginning: false
  });
  await consumer.run({
    eachMessage: async ({topic, partition, message}) => {
      const prefix = `${topic}[${partition} | ${message.offset}] / ${message.timestamp}`;
      const data = message.value.toString();
      /* Helper for logging errors, we want to continue afterwards */
      const onProcessingError = (err) => {
        logger.error(`Caught: ${err.stack}, topic=${topic}, data=${data}`)
      }

      logger.info(`Processing: ${prefix} ${message.key}, ${data}`);
      
      try {
        const bookmarks = JSON.parse(data);
        // We have Bookmark message, create a helper for producing success events for given Bookmark
        const onProcessingSuccess = async (type, key, bookmark) => {
          return await producer.send({topic: `bookmark.${type}`, messages: [
            { key: `${key}`, value: JSON.stringify({
              id: bookmark.id,
              archivedDateTime: bookmark.updatedDateTime,
              webUrl: bookmark.webUrl,
              owner: { id: bookmark.owner.id }
            })}
          ]});
        }

        if (topic === 'bookmark.created') {
          for (const i in bookmarks) {
            await onCreatedBookmark({baseDir, bookmark: bookmarks[i]})
              .then(async (resp) => {
                logger.info('Processing created successful:', resp);
                await onProcessingSuccess('archived', message.key, bookmarks[i])
              })
              .catch(onProcessingError);
          }
        } else if (topic === 'bookmark.updated') {
          onUpdatedBookmark({bookmarks})
            .then(() => logger.info(`Processing updated bookmarks: ${bookmarks
              .map(b => `${b.id}`).join(',')}`))
            .catch(onProcessingError);
        } else if (topic === 'bookmark.deleted') {
          onDeletedBookmark({bookmarks})
            .then(() => logger.info(`Processing deleted bookmarks: ${bookmarks
              .map(b => `${b.id}`).join(',')}`))
            .catch(onProcessingError);
        }
      } catch (err) {
        onProcessingError(err);
      }
    },
  });
}

const errorTypes = ['unhandledRejection', 'uncaughtException'];
const signalTraps = ['SIGTERM', 'SIGINT', 'SIGUSR2'];
const cleanUp = async () => {
  await consumer.disconnect();
  await producer.disconnect();
}


errorTypes.map(type => {
  process.on(type, async e => {
    try {
      await cleanUp();
      process.exit(0)
    } catch (err) {
      console.log(err)
      process.exit(1)
    }
  })
});

signalTraps.map(type => {
  process.once(type, async () => {
    try {
      await cleanUp();
    } finally {
      process.kill(process.pid, type)
    }
  })
});


retry(() => {
  return run({baseDir: archiveDir})
    .catch(async(err) => {
      await cleanUp();
      return err;
    });
}, {retries: 3, delay: 30})
.catch(err => {
  console.log(err);
  if (browser) browser.close();
  throw(err);
})




