FROM node:14-alpine

ARG FETCHER_ARCHIVE_DIR=/data/fetcher
ENV FETCHER_ARCHIVE_DIR=/data/fetcher
ENV PUPPETEER_SKIP_CHROMIUM_DOWNLOAD=true
ENV CHROMIUM_PATH=/usr/bin/chromium-browser

WORKDIR /app/fetcher
COPY . .
RUN apk add --no-cache udev ttf-freefont chromium \
  && npm install \
  && mkdir -p ${FETCHER_ARCHIVE_DIR}

CMD ["node", "index.js"]