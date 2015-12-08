#!/usr/bin/env bash
/Library/PostgreSQL/9.4/pgAdmin3.app/Contents/SharedSupport/pg_dump --username "postgres" --schema-only --file db-backup.sql stock
