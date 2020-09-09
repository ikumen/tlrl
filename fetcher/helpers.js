const fs = require('fs');

/** Helper to create file path for given parts */
const makePath = (parts) => parts.join('/');

/** Helper for creating directories */
const ensureDirectoryExists = (path, cb) => {
  fs.mkdir(path, {mode: 0755, recursive: true}, (err) => {
    if (!err || err.code === 'EEXIST') {
      cb(null);
    } else cb(err); // unable to create
  })
}

/** Helper for retrying promises */
const retry = (promise, {delay=5, retries=5}) => new Promise((resolve, reject) => {
  promise().then(resolve)
    .catch((err) => {
      console.log(err);
      if (retries <= 0) {
        reject(err);
      } else {
        console.log(`Retrying in ${delay} secs...`)
        setTimeout(() => {
          resolve(retry(promise, {delay: delay*2, retries: retries-1}));
        }, delay * 1000);
      }
    })
});

exports.makePath = makePath;
exports.ensureDirectoryExists = ensureDirectoryExists;
exports.retry = retry;

