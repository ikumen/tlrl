FROM node:14-alpine

ENV PUPPETEER_SKIP_CHROMIUM_DOWNLOAD=true
ENV CHROMIUM_PATH=/usr/bin/chromium-browser

ENV FETCHER_ARCHIVE_DIR=${FETCHER_ARCHIVE_DIR:-/data/archive}
ENV FETCHER_LOG_LVL=${FETCHER_LOG_LVL:-4}
ENV FETCHER_KAFKA_BROKERS=${FETCHER_KAFKA_BROKERS:-kafka:9092}
ENV FETCHER_SOLR_SERVER=${FETCHER_SOLR_SERVER:-solr:8983}

WORKDIR /app/fetcher
COPY . .
RUN apk add --no-cache udev ttf-freefont chromium \
  && chmod a+x start-after.sh \
  && npm install

ENTRYPOINT [ "/usr/local/bin/node", "index.js" ]