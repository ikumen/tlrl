## TLRL Database Migration Scripts

This directory contains the [Flyway](https://flywaydb.org) database migration scripts for our project, organized by database&mdash;currently we support [PostgreSQL](https://www.postgresql.org/). 

### General Workflow

The application runs an embedded H2 database by default in development. After successfully testing new schema changes on H2, we point the application to a PostgreSQL database and apply the schema changes there. If all goes well, we write the [changes as migrations scripts](https://flywaydb.org/documentation/migrations) into the `migrations` directory.

#### Applying Migrations

To test the changes on PostgreSQL, you can use the provided `docker-compose.yml` to bring up a local PostgreSQL instance. We use the following [docker image](https://hub.docker.com/_/postgres), with the initial schema creation script under [/config/db/postgresql/schema](/config/db/postgresql/schema) directory.

```bash
docker-compose up postgres
```
Once you have a PostgreSQL database to test against, use `maven` to run the migrations scripts.

```bash
FLYWAY_USER=postgres FLYWAY_PASSWORD=postgres ./mvnw -f backend/pom.xml flyway:migrate -Ddb=postgres
```

_Note: If applying migrations against the provided `docker-compose.yml`'s PostgreSQL instance, use the user/password of `postgres/postgres`._



