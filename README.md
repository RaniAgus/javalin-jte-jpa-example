# javalin-web-template

## Dependencies

- SDK: [Java 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- Build Tool: [Maven](https://maven.apache.org/download.cgi)
- Web Framework: [Javalin](https://javalin.io/)
- Template Engine: [JTE](https://jte.gg/)
- ORM: [flbulgarelli/jpa-extras](https://github.com/flbulgarelli/jpa-extras)

## IntelliJ Setup

- Install the [JTE](https://plugins.jetbrains.com/plugin/13407-jte) plugin

## Database Setup

### PostgreSQL

- Install [PostgreSQL](https://www.postgresql.org/download/)
- Create a database named `project`
- Run `io.github.raniagus.project.Bootstrap` to create the tables

### Docker Compose

- Install [Docker](https://docs.docker.com/get-docker/) and
  [Docker Compose](https://docs.docker.com/compose/install/)
- Run `docker volume create --name=project-data` to create a volume for the database
- Run `docker-compose up -d db` to start the database
- Run `io.github.raniagus.project.Bootstrap` to create the tables

## Running the Application

- Run `io.github.raniagus.project.Application` to start the application

## Deploy

### Docker Compose

- Run `docker-compose up -d` to start the application
- Run `docker-compose down` to stop the application


### Railway

- Go to [Railway](https://railway.app/)
- Create a new project
  - Create a new PostgreSQL database and copy the connection string
  - Connect the project to your GitHub repository and go to the project settings
    - Change the restart policy to `Never`
    - Add the following environment variables:
      - `DB_URL`: The database URL
      - `DB_USER`: The database user
      - `DB_PASSWORD`: The database password
      - `PORT`: The port to listen on
    - Generate a new domain name provided by Railway
