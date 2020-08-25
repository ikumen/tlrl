FROM node:14-alpine

ENV FETCHER_ARCHIVE_DIR=${FETCHER_ARCHIVE_DIR}
ENV PUPPETEER_SKIP_CHROMIUM_DOWNLOAD=true
ENV CHROMIUM_PATH=/usr/bin/chromium-browser
ENV FETCHER_LOG_LVL=${FETCHER_LOG_LVL:-4}
ENV FETCHER_KAFKA_BROKERS=${FETCHER_KAFKA_BROKERS:-kafka:9092}
ENV FETCHER_SOLR_SERVER=${FETCHER_SOLR_SERVER:-solr:8983}

WORKDIR /app/fetcher
COPY . .
RUN 'if [ -z "${FETCHER_ARCHIVE_DIR}" ]; \
      then echo "Environment FETCHER_ARCHIVE_DIR not set."; \
      exit 1; fi' \
  && apk add --no-cache udev ttf-freefont chromium \
  && npm install

CMD ["node", "index.js"]