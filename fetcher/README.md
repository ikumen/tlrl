## TLRL Fetcher 

Node application responsible for fetching, indexing and archiving bookmarked web pages&mdash;triggered by upstream bookmark events (e.g, when a bookmark is created, updated, and deleted) via [Kafka](kafka.apache.org).

```
+------------------------------------------------+
|                    TLRL                        |
| +---------+     +-------+  +-----------------+ |
| |  Spring | (1) | Kafka |  |(2)   Fetcher    | |
| | backend |----->       |--> +-------------+ | |  (3) +------+
| |   app   <-----|       <--| | Puppeteer   <---^-----> +-----+
| |         |     |   (6) |  | +-------------+ | |      | | www |
| +---------+     +-------+  |     (4) +----+  | |      +-|     |
|                 +-------+  | (5) /---> PDF|  | |        +-----+
|                 | Solr  <--^----/    +----+  | |
|                 +-------+  +-----------------+ |
+------------------------------------------------+
```

The `backend` application publishes a message (step 1) to the following Kafka topics: `bookmark.created`, `bookmark.updated` and `bookmark.deleted`, whenever a bookmark is created, updated, or deleted. `fetcher` (step 2) listens on those topics and performs the given action on the received payload.

Topics:
- `bookmark.created`
  - `payload`: JSON representing newly created bookmark 
  - `action`: 
    - start up an instance of Puppeteer, (step 3) grab the web page at the bookmarked url
    - generate a PDF (step 4) version of the web page for archiving
    - extract the content (step 5) for indexing
    - send the bookmark and web page (step 5) content to Solr to index
    - notifies backend the bookmark has been archived/index (step 6 to topic `bookmark.archived`)
- `bookmark.updated`
  - `payload`: JSON list of bookmarks with updated fields
  - `action`: send the updated fields to Solr for indexing (step 5)
- `bookmark.deleted`
  - `payload`: JSON list of deleted bookmark ids
  - `action`: send delete command with bookmark ids to Solr (step 5)

### Running Fetcher
First start the `backend` application and dependent services.

```bash
# Start the services
docker-compose up zookeeper kafka solr
```
Then start the `backend` application.
```bash
# If you're not developing the backend app, but need it running
docker-compose up app

# ... or if you're also working on the backend application
SPRING_PROFILES_ACTIVE=dev,h2 ./mvnw -f backend/pom.xml spring-boot:run -Ddb=h2
```
Finally start `fetcher`.
```bash
# If you just need fetcher running
docker-compose up fetcher

# ... or if you're also working on fetcher, just start it up like any node app
npm install --prefix fetcher
FETCHER_LOG_LVL=4 node fetcher/index.js
```

Both scenarios above, run `fetcher` basically with the defaults:
  - listens to Kafka broker at: `localhost:9093`
  - saves the archived PDFs to `<project-root>/target/fetcher/archive`

