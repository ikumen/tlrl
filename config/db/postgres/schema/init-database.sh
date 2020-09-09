#!/bin/bash
set -e

#
# Creates the tlrl database and users. This script is used in conjunction with
# our PostgreSQL container (https://hub.docker.com/_/postgres). Note: The script is 
# only run if the mounted data volume (./target/postgres) is empty.
#
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
-- Create tlrl database and user.
CREATE USER ${TLRL_DB_ADMIN:?} WITH PASSWORD '${TLRL_DB_ADMIN_PASSWORD:?}';
CREATE USER ${TLRL_DB_USER:?} WITH PASSWORD '${TLRL_DB_USER_PASSWORD:?}';
CREATE DATABASE ${TLRL_DB_NAME:?};
\c ${TLRL_DB_NAME};
REVOKE CONNECT ON DATABASE ${TLRL_DB_NAME} FROM public;
REVOKE TEMPORARY ON DATABASE ${TLRL_DB_NAME} FROM public;
REVOKE ALL ON SCHEMA public FROM public;
GRANT ALL PRIVILEGES ON DATABASE ${TLRL_DB_NAME} TO ${TLRL_DB_ADMIN};
GRANT ALL ON SCHEMA public TO ${TLRL_DB_ADMIN};
GRANT USAGE ON SCHEMA public to ${TLRL_DB_USER};
GRANT CONNECT ON DATABASE ${TLRL_DB_NAME} TO ${TLRL_DB_USER};
EOSQL
