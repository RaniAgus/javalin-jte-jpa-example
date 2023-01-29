# javalin-jte-jpa-example

## Dependencias

- SDK: [Java 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- Framework web: [Javalin](https://javalin.io/): es un fork de SparkJava, ver
  [comparativa](https://javalin.io/comparisons/sparkjava)
- Motor de Templates: [JTE](https://jte.gg/)
- ORM: [flbulgarelli/jpa-extras](https://github.com/flbulgarelli/jpa-extras) (con una modificación para soportar
  variables de entorno en la URL de la base de datos, ver 
  [Pull Request](https://github.com/flbulgarelli/jpa-extras/pull/2)).

## Configuración en local

### IntelliJ IDEA

- Instalar el [JTE plugin](https://plugins.jetbrains.com/plugin/13407-jte)

### Base de datos

#### Opción 1: PostgreSQL

- Instalar [PostgreSQL](https://www.postgresql.org/download/)
- Crear una base de datos llamada `example`
- Desde IntelliJ, ejecutar la clase `io.github.raniagus.example.Bootstrap` para crear las tablas e insertar datos de
prueba. Los usuarios de prueba son provistos por un archivo csv generado desde [Mockaroo](https://mockaroo.com/).

#### Opción 2: Docker Compose

- Instalar [Docker](https://docs.docker.com/get-docker/) y
  [Docker Compose](https://docs.docker.com/compose/install/)
- Desde consola,
  - Ejecutar `docker volume create --name=example-data` para crear un volumen para guardar los datos de la base de datos
  - Ejecutar `docker-compose up -d db` para iniciar la base de datos en segundo plano
- Desde IntelliJ, ejecutar la clase `io.github.raniagus.example.Bootstrap` para crear las tablas e insertar datos de
prueba

## Ejecutar la aplicación

- Ejecutar desde IntelliJ `io.github.raniagus.example.Application` para iniciar la aplicación

## Despliegue

### Opción 1: Docker Compose

La primera opción es usar una máquina virtual, como las que provee [Digital Ocean](https://www.digitalocean.com/), para
desplegar la aplicación completa (incluyendo la base de datos) en un servidor con Docker Compose instalado.

Simplemente, debemos clonar el repositorio, crear un archivo `.env` con las variables de entorno necesarias:
- `DB_URL`: La URL a la base de datos, ej.: `jdbc:postgresql://example-db:5432/example`
- `DB_USER`: El usuario de la base de datos
- `DB_PASSWORD`: La contraseña de la base de datos
- `PORT`: El puerto en el que se ejecutará la aplicación, que siempre será el `80`

Y ejecutar `docker-compose up -d`

Si bien esta es una opción posible, es recomendable usar una base de datos en un nodo separado como se explica en las
siguientes opciones.

### Opción 2: Railway

La segunda opción es usar Railway, un servicio de hosting de aplicaciones web que permite desplegar aplicaciones
mediante Docker, junto con una base de datos PostgreSQL. Tiene un plan gratuito que permite hasta 500 horas de uso por
mes, por lo que deberás apagar la aplicación cuando no la necesites usar.

- Ir a [Railway.app](https://railway.app/)
- Crear un nuevo proyecto
  - Crear una nueva base de datos PostgreSQL y copiar el connection string
  - Conectar el proyecto al repositorio de GitHub e ir a la configuración del mismo
    - Cambiar la política de reinicio a `Never`
    - Agregar las siguientes variables de entorno:
      - `DB_URL`: La URL a la base de datos, ej.: `jdbc:postgresql://example-db:5432/example`
      - `DB_USER`: El usuario de la base de datos
      - `DB_PASSWORD`: La contraseña de la base de datos
      - `PORT`: El puerto en el que se ejecutará la aplicación, que siempre será el `80`
    - Generar un dominio para el proyecto

Con el repositorio vinculado a Railway, cada vez que se haga un push a la rama `main`, Railway se encargará de hacer
un build de la aplicación y desplegarla en el servidor.

### Opción 3 (recomendada): Fly.io + CockroachDB

La última opción es [Fly.io](https://fly.io/) para desplegar la aplicación.
A diferencia de Railway, Fly.io no provee una base de datos, por lo que debemos complementarlo con otro servicio
como el que ofrece [CockroachDB](https://www.cockroachlabs.com/).

Los pasos que debemos seguir son:

- Crear una cuenta en [CockroachCloud](https://cockroachlabs.cloud/)
- Crear un cluster de CockroachDB y copiar el connection string

Luego,

- Instalar el [CLI de Fly](https://fly.io/docs/hands-on/install-flyctl/)
- Crear una cuenta ejecutando `flyctl auth signup` o registrarse con `flyctl auth login`
- Seguir los pasos para [desplegar via Dockerfile](https://fly.io/docs/languages-and-frameworks/dockerfile/)
- Agregar las siguientes variables de entorno:
  - `DB_URL`: La URL a la base de datos, ej.: `jdbc:postgresql://example-db:5432/example`
  - `DB_USER`: El usuario de la base de datos
  - `DB_PASSWORD`: La contraseña de la base de datos
  - `PORT`: El puerto en el que se ejecutará la aplicación, que siempre será el `80`

Esta es la opción que más recomiendo actualmente, ya que Fly.io:
- Provee un plan gratuito que permite hasta 2 aplicaciones desplegadas por tiempo ilimitado;
- Las contraseñas se pueden almacenar de forma segura usando `flyctl secrets set` (revisar la
  [documentación](https://fly.io/docs/reference/secrets/)); y
- Como el despliegue es manual a través de un comando, no es necesario crear commits para disparar el webhook en caso de
  habernos equivocado al configurar una variable de entorno o querer desplegar una versión anterior.

Además, CockroachDB es un servicio específico para bases de datos, por lo que provee muchas más opciones que Railway
para monitorear y administrar la base de datos. También podrías usarlo junto con Railway para desplegar la aplicación.
