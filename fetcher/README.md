## TLRL Fetcher 

Node application responsible for fetching, indexing and archiving bookmarked web pages&mdash;triggered by upstream bookmark events (e.g, when a bookmark is created, updated, and deleted) coming from a Kafka based message queue. 

Basically the application listens to Kafka broker on the following topics, and then performs the given `action` on the received `payload`:
- `bookmark.created`
  - `payload`: newly created bookmark 
  - `action`: for each bookmark, 
    - start up an instance of Puppeteer, grab the web page at the bookmarked url
    - generate a PDF (and mht) version of the web page for archiving
    - extract the content for indexing
    - send the bookmark and web page content to Solr to index
- `bookmark.updated`
  - `payload`: list of updated bookmarks 
  - `action`: send the updated fields to Solr for indexing
- `bookmark.deleted`
  - `payload`: list of deleted bookmark ids
  - `action`: send delete command with bookmark ids to Solr

## Running Fetcher
### In development while developing other parts of tlrl, but you need fetcher running

If you need to have `fetcher` running while developing other components of `tlrl`, just use `docker-compose` to bring up a containerized version of fetcher, after you've started the `tlrl` application and Kafka. 

```
# first start up Kafka and Solr
docker-compose -f docker-compose.dev.yml up zookeeper kafka solr
```

Also make sure to start up `tlrl` application.

```
SPRING_PROFILES_ACTIVE=dev ./mvnw spring-boot:run
```

Then start up fetcher.

```
docker-compose -f docker-compose.dev.yml up fetcher
```

### In development while developing fetcher

If you need to actually run `fetcher` while actively working on it, just run it locally&mdash;it's just a Node app. Again, make sure to start Kafka and `tlrl` application beforehand.

```
docker-compose -f docker-compose.dev.yml up zookeeper kafka solr

# in another terminal
SPRING_PROFILES_ACTIVE=dev ./mvnw spring-boot:run
```
Then start fetcher locally

```
cd src/main/fetcher
npm install
FETCHER_LOG_LVL=4 node index.js
```

Both scenarios above, run `fetcher` basically with the defaults:
  - listens to Kafka broker at: `localhost:9092`
  - saves the archived PDFs/mht to `./data`

