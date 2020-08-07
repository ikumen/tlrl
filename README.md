# TLRL 
TLRL (_too long; read later_)&mdash;a little bookmarking app with the ability to search and archive bookmark contents. I use it to manage my bookmarks and to learn backend development, specifically on the [Spring](//spring.io)+[React](//reactjs.org) stack.


### Start Here
- [Project Overview](#project-overview) 
- Quick start
  - [Running TLRL](#running-tlrl)
  - [Development Setup](#development-setup)


## Project Overview
Here's a quick overview of the application architecture, project structure and code organization.

### Architecture
A high level view of the overall architecture.

```
                +-------------------------------------------+
                |                   TLRL                    |
                | +-------------+           +-------------+ |
                | | Spring App  |           |  PostgreSQL | |
            +----------------+ <------------->            | |
            |  web client    |  |           +-------------+ | 
            |                |  | +-------+                 |   +-----+
            +------------^---+  | |       |  +------------+ |   | +-----+
                | |      |      | | Kafka |  | Fetcher   <------->| www |
+-------------+ | | +----v---+ <--->     <---->           | |   | |     |    
| bookmarklet <----->        |  | |       |  +------^-----+ |   +-|     |
+-------------+ | | |        |  | +-------+         |       |     +-----+
                | | |  API   |  |            +------v-----+ |
                | | |        | <-------------->           | |
                | | +--------+  |            | Solr       | |
                | +-------------+            +------------+ |
                +-------------------------------------------+             
```
The system consist of the following components:
- [Spring Boot](https://spring.io/projects/spring-boot) application that serves:
  - API for clients for managing bookmarks
  - [React](//reactjs.org) web client app (talks with our API)
  - [bookmarklet](https://en.wikipedia.org/wiki/Bookmarklet) for saving bookmarks (talks with our API)
- [PostgreSQL](https://www.postgresql.org/) database for persistence
- Fetcher for:
  - fetching bookmarked pages
  - converting fetched pages to pdf
  - extract fetched page text to Solr index
- [Solr](https://lucene.apache.org/solr/) provides search


### Structure and Organization
The project structure is a [monorepo](https://en.wikipedia.org/wiki/Monorepo), with sub projects resembling the components in the diagram above:
- `backend` Maven style layout, containing the application (API, security) code base and database schema. Packages organized mostly by feature then layer:
  - `bookmark` contains bookmark feature (e.g, self-contained controller, repository, service)
  - `security` security related code used across the overall application
  - `core` core domain/models used across the application
  - `support` helper/utilities used across the application
  - `user` user feature (e.g, self-contained repository, service)
- `frontend` code base for the single page web client that talks with the application API, bookmarklet. Note, we build the frontend and deploy it with the main application code. `src` organized mostly by layer:
  - `components`
  - `pages`
  - `support`
- `fetcher` code base for the Fetcher app, it runs standalone and organized as:
  - `index.js` main application entry point
  - `indexer.js` wrapper for Solr JSON api
  - `browser.js` wrapper for puppeteer 
- `solr` configuration files for Solr search engine
 
## Quick Start
Here are some quick start instructions for either running a demo of the application or getting started with development. In either case you'll need to fork/clone the project: https://github.com/ikumen/tlrl.git

### Running TLRL
#### Requirements
You'll need the following software:
- [Java 8 at a minimum](https://adoptopenjdk.net/)
- [Docker](https://www.docker.com/) with [docker-compose](https://docs.docker.com/compose/install/)

#### OAuth Configurations
We'll need at least one (or both) OAuth provider to handle authentication:
- GitHub
- Google

#### Start
Next use `docker-compose` to bring up the application and all dependent services.
```
cd <project-root>
docker-compose -f docker-compose.dev.yml up postgres zookeeper solr kafka
```

Point your browser at http://localhost:8080 to check out the application.

### Development Setup








