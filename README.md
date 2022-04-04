# OpenFoodFacts importer projet

### Goals

- Import the delta files from the open food facts database
- Save the file data, and trigger events to import each file individually
- Expose the file data through some apis, with additionnal data on the importer process 

### Features - Versions

- 0.0.1 : Spring/Maven project setup
- 0.0.2 : OpenFoodFacts delta file query and file info persisted
- 0.0.3 : BugFixes, adding file import feedback
- 0.0.4 : Improving controllers on file import trigger and feedback
- 1.0.0 : Simple docker packaging for the spring app, still needs a mysql running locally

### Installation

#### Required 
- maven (made with 3.6.3)
- docker (made with 20.10.14)
- mysql database with user created as in the spring configuration

#### Deploying
- clone the project
- execute the sql found in the /src/main/resources/database in the given order
- execute startup.sh script

This will package the latest version, and create a docker container to start it. 
Once started, the app should be accessible at http://localhost:8080/actuator/health


