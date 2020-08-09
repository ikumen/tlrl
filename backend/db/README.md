## TLRL Database Schema and Migration

This directory contains the database creation and migration scripts for our project, more specifically for [PostgreSQL](https://www.postgresql.org/). The scripts are in two sub-directories, outlined below.

  - `migration` contains the individual migration scripts, all managed by [Flyway](https://flywaydb.org)
  - `schema` (provided for development) contains the scripts to initialize a new database and schema  

### Database and Schema Initialization

In development, you can use the provided `docker-compose.yml` to bring up a PostgreSQL database. The [docker image](https://hub.docker.com/_/postgres) is configured to take care of the database/schema creation by running the scripts under the `schema` directory.

```bash
docker-compose up postgres
```

For production, your operations team will probably manage the schema.

### Migrations

After the schema/database creation, we use [Flyway](https://flywaydb.org) to apply the database migrations that will create our tables, sequences, constraints, and future changes.

```bash
FLYWAY_USER=postgres FLYWAY_PASSWORD=postgres ./mvnw flyway:migrate -Ddb=postgres
```

_Note: assuming you're using the provided `docker-compose.yml` to bring up a PostgreSQL instance, it should have created an admin user/password of `postgres/postgres` by default&mdash;we'll use this with Flyway._



