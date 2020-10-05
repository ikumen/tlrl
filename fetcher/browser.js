const puppeteer = require('puppeteer-extra');
const StealthPlugin = require('puppeteer-extra-plugin-stealth')

const fs = require('fs');
const { retry } = require('./helpers');

const unsupportedMediaTypes = [
  '.avi', '.flv', '.mov', '.mp3', '.mp4', '.wmv'
];

const launchOptions = {
  args: [
    '--no-sandbox',
    '--disable-setuid-sandbox',
    '--disable-infobars',
    '--window-position=0,0',
    '--ignore-certifcate-errors',
    '--ignore-certifcate-errors-spki-list',
    //'--user-agent="Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3312.0 Safari/537.36"'
  ],
  headless: true,
  executablePath: process.env.CHROMIUM_PATH,
  ignoreHTTPSErrors: true,
};


function Browser({logger, retriesOnFailure=4}) {

  /**
   * Filters out (i.e, aborts) request for unsupported media types.
   * @param {Object} request https://pptr.dev/#?product=Puppeteer&version=v3.1.0&show=api-class-request
   */
  const requestMediaTypeFilter = (request) => {
    const url = request.url().toLowerCase();
    const resourceType = request.resourceType();
    if (resourceType === 'media'
        || unsupportedMediaTypes.filter(t => url.endsWith(t)).length > 0
        || url.includes('.doubleclick.net')) {
      logger.debug(`Aborting request for ${url}`)
      request.abort();
    } else {
      request.continue();
    }
  }

  return {
    load: (url) => new Promise(async (resolve, reject) => {
      // Some pages can crash browser, so we will explicitly just use 
      // a new browser for each requested url.
      const browser = await puppeteer.launch(launchOptions);
      const page = await browser.newPage();
      await page.setRequestInterception(true);
      page.on('request', requestMediaTypeFilter);

      try {
        // Try to load the url until we're successful. We have to
        // catch here and reject so upstream can handle otherwise
        // we'll get an unhandledRejection.
        await retry(() => page.goto(url, {
          waitUntil: "networkidle0"
        }), {retries: retriesOnFailure});
        await page.waitFor("*");
      } catch (err) {
        logger.warn(`Unable to load ${url} after ${retriesOnFailure} reties.`, err);
        reject(err);
      }

      resolve({
        /** Return the page title */
        title: async () => await page.title(),
        content: async () => await page.$eval('*', el => el.innerText),
        /** Save page as PDF to given path */
        pdf: async (opts) => {
          await page.emulateMediaType('screen');
          await page.pdf(opts);
        },
        /** Save page as mhtml to given path */
        mhtml: async ({path}) => {
          // https://github.com/puppeteer/puppeteer/issues/3575#issuecomment-447258318
          const session = await page.target().createCDPSession();
          await session.send('Page.enable');
          // https://vanilla.aslushnikov.com/?Page.captureSnapshot
          const snapshot = await session.send('Page.captureSnapshot');
          session.detach();
          return new Promise((resolve, reject) => {
            fs.writeFile(path, snapshot.data, (err) => {
              if (err) reject(new Error(`Unable to save mhtml: ${path}`, err));
              else resolve();
            });  
          })
        },
        /** Close up the current context and page */
        close: async () => {
          if (browser) {
            await browser.close();
          }
        }
      });
    }), // end load()
  }  
}

exports.Browser = Browser;