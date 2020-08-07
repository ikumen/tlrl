## TLRL Database Schema and Migration

This directory contains the database creation and migration scripts for our project, more specifically for [PostgreSQL](https://www.postgresql.org/). The scripts are broken into two sub-directories, outlined below.

  - `migration` contains the individual migration scripts, all managed by [Flyway](https://flywaydb.org)
  - `schema` contains the scripts to initialize a new database and schema for our project. 

### Database and Schema Initialization

In development, you can use the provided `docker-compose.dev.yml` to bring up a PostgreSQL database. By default the [docker image](https://hub.docker.com/_/postgres) is configured to take care of the database initialization and schema creation, specifically we've configured it to run scripts under the `schema` directory.

```bash
docker-compose -f docker-compose.dev.yml up postgres
```

For production, you'll probably want to incorporate the `schema` scripts into your flow.

### Migrations

Next we use [Flyway](https://flywaydb.org) to apply the database migrations that will create our tables, sequences, constraints, and on going changes.

_Note: assuming you're using the provided `docker-compose.dev.yml` to bring up a PostgreSQL instance, it should have created an admin user/password of `postgres/postgres` by default&mdash;we'll use this with Flyway._

```bash
FLYWAY_USER=postgres FLYWAY_PASSWORD=postgres ./mvnw flyway:migrate -Ddb=postgres
```

We rely on [Maven profiles](http://maven.apache.org/guides/introduction/introduction-to-profiles.html) to load the appropriate database dependencies for the given context&mdash;in development I also an embedded H2 database.
Our build is configured to load H2 by default unless it sees a `db` property indicating a different database dependency to load. In the above command we load PostgreSQL dependencies with `-Ddb=postgres` argument.

If you're new to Flyway, checkout [their migrations](https://flywaydb.org/documentation/migrations) documentation to see how we're using it to manage our database changes. 


