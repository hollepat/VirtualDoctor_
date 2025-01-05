# System for symptom classification

To run this application you should startup services in following order.

1. Virtual Doctor App
2. docker-compose up to start up database (liquibase must run automatically)
3. classification_model with `--import-data` flag for the first time to import base dataset into database
4. AVD emulator
5. HealthData Provider

More detail for each komponent is in the project folder for each service, since each is written in different language it was kept seperated.
