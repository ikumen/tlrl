#!/bin/bash
set -e

#
# Creates the tlrl database and users. This script is used in conjunction with
# our PostgreSQL container (https://hub.docker.com/_/postgres). Note: The script is 
# only run if the mounted data volume (./target/postgres) is empty.
#
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
-- Create tlrl database and user.
CREATE USER ${DB_ADMIN:?} WITH PASSWORD '${DB_ADMIN_PASSWORD:?}';
CREATE USER ${DB_USER:?} WITH PASSWORD '${DB_USER_PASSWORD:?}';
CREATE DATABASE ${DB_NAME:?};
\c ${DB_NAME};
REVOKE CONNECT ON DATABASE ${DB_NAME} FROM public;
REVOKE TEMPORARY ON DATABASE ${DB_NAME} FROM public;
REVOKE ALL ON SCHEMA public FROM public;
GRANT ALL PRIVILEGES ON DATABASE ${DB_NAME} TO ${DB_ADMIN};
GRANT ALL ON SCHEMA public TO ${DB_ADMIN};
GRANT USAGE ON SCHEMA public to ${DB_USER};
GRANT CONNECT ON DATABASE $DB_NAME TO ${DB_USER};
EOSQL
