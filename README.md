## Sqlite init db:
`CREATE TABLE if not exists main.mon (service_name TEXT, master_system TEXT, status_code INTEGER, moment TIMESTAMP DEFAULT (datetime(CURRENT_TIMESTAMP, 'localtime')));`