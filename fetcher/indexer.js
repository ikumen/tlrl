const http = require('http');

const requestOpts = {
  method: 'POST',
  headers: {'Content-Type': 'application/json'}
}

const updatedBookmarkToSolrUpdateStr = 
    ({id, title, description, readStatus, sharedStatus, tags}) => {
  const doc = {id: `${id}`};
  if (title) doc.title = {'set': title}
  if (description) doc.description = {'set': description}
  if (readStatus) doc.readStatus = {'set': readStatus}
  if (sharedStatus) doc.sharedStatus = {'set': sharedStatus}
  if (tags) doc.tags = {'set': tags.map(t => t.id)}
  return `"add": ${JSON.stringify({doc})}`;
}

/**
 * Wrapper for Solr JSON Index handler API, with support for adding, updating
 * and removing Bookmark documents from our Solr index.
 * 
 * @param {Object} cfg
 * @param {String} cfg.solrEndpoint solr endpoint to make request to
 * @param {Object} cfg.logger logger instance to use
 */
function Indexer({solrEndpoint, logger}) {
  /**
   * Generic helper for sending actual document to solr endpoint.
   * 
   * @param {Object} data contain Solr command and documents
   */
  const sendToSolr = (data) => new Promise((resolve, reject) => {
    const url = new URL(solrEndpoint);
    const opts = {...requestOpts, 
      hostname: url.hostname,
      port: url.port,
      path: url.pathname
    };
    const request = http.request(opts, (res) => {
      logger.debug(`status: ${res.statusCode}, headers: ${JSON.stringify(res.headers)}`);
      res.on('data', (chunk) => {
        // Note: response will not end unless data is consumed
        //console.log(`Received ${chunk.length} bytes of data.`);
      });
      res.on('end', () => {
        //logger.info("should resolve send to solr")
        resolve()
      }); 
    });
    request.on('error', reject);
    request.write(data);
    request.end();
  });
  
  return {
    /** Add new Bookmark document to our index */
    add: (bookmark) => {
      const { id, title, content, owner, webUrl, description, readStatus, 
        sharedStatus, createdDateTime, updatedDateTime, tags } = bookmark;

      return sendToSolr(JSON.stringify({
        commit: {},
        add: { doc: {
          id: `${id}`,
          ownerId: owner.id,
          ownerName: owner.name,
          title, 
          description,
          content,
          tags: (tags||[]).map(t => t.id),
          urlId: webUrl.id,
          url: webUrl.url,
          readStatus,
          sharedStatus,
          createdDateTime,
          archivedDateTime: updatedDateTime
        }}
      }));
    },
    /** Updating existing Bookmark document with given id  */
    update: (bookmarks) => {
      //logger.debug('updating: ', bookmarks);
      return sendToSolr(`{"commit": {},  ${bookmarks.map(updatedBookmarkToSolrUpdateStr).join(',')}}`);
    },
    /** Update the delete flag for given Bookmark document */
    delete: (bookmarks) => {
      //logger.debug('deleting: ', bookmarks);
      return sendToSolr(`{"commit": {}, "delete": [${bookmarks.map(({id}) => `"${id}"`).join(',')}]}`);
    }
  }
}

exports.Indexer = Indexer;





