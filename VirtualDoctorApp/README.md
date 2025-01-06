# System for symptom classification

## Description
This is a system for symptom classification that helps patients to get medical advice about their health status.


## Technologies
- Spring Boot for Backend
- Python [libs] for classification model
- PostgreSQL for Database

## PostgreSQL

The database is initialized with the `liquibase` tool. The `liquibase.properties` file contains the configuration for the
database connection. The `liquibase` tool is used to initialize the database with the `changelog-master.yaml` file.


Connection to the database is done as following:

1. Run docker-compose.yml file containing the PostgreSQL database container configuration.
```shell
docker-compose up
```
2. Connect to the database using the following credentials for intelliJ IDEA:
```
#DataSourceSettings#
#LocalDataSource: postgres@localhost
#BEGIN#
<data-source source="LOCAL" name="postgres@localhost" uuid="b26f9eef-e335-4a8a-9f60-96d380e10c14"><database-info product="PostgreSQL" version="17.2 (Debian 17.2-1.pgdg120+1)" jdbc-version="4.2" driver-name="PostgreSQL JDBC Driver" driver-version="42.7.3" dbms="POSTGRES" exact-version="17.2" exact-driver-version="42.7"><identifier-quote-string>&quot;</identifier-quote-string></database-info><case-sensitivity plain-identifiers="lower" quoted-identifiers="exact"/><driver-ref>postgresql</driver-ref><synchronize>true</synchronize><jdbc-driver>org.postgresql.Driver</jdbc-driver><jdbc-url>jdbc:postgresql://localhost:5432/postgres</jdbc-url><jdbc-additional-properties><property name="com.intellij.clouds.kubernetes.db.host.port"/><property name="com.intellij.clouds.kubernetes.db.enabled" value="false"/><property name="com.intellij.clouds.kubernetes.db.container.port"/></jdbc-additional-properties><secret-storage>master_key</secret-storage><user-name>postgres</user-name><schema-mapping><introspection-scope><node negative="1"><node kind="database" qname="@"><node kind="schema" qname="@"/></node><node kind="database" qname="healthdb"><node kind="schema" negative="1"/></node></node></introspection-scope></schema-mapping><working-dir>$ProjectFileDir$</working-dir></data-source>
#END#
```
3. Run the `liquibase` tool to initialize the database with the `changelog-master.yaml` file. For that use following cmd in maven console:
```shell
mvn liquibase:update
```
This is due to problem that the `liquibase` tool is not starting automatically when the application is started.
