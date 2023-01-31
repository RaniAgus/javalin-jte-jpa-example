# javalin-jte-jpa-example

Ejemplo de despliegue de una aplicación [Java 11] utilizando el siguiente stack:

- [Javalin]: framework web liviano que nació como un fork de Spark, por lo que es parecido a este (ver
  [comparativa entre Javalin y Spark]) pero se mantiene constantemente actualizado. 

- [JTE]: motor de templates que utiliza código Java directamente en los templates, similar a lo que uno haría en C# con
  [Razor]. Permite hard reload de templates sin necesidad de reiniciar la aplicación e incluye un 
  [plugin para IntelliJ IDEA] con autocompletado y soporte para refactoring.

- [Guice]: framework liviano de inyección de dependencias, a través de un service locator y anotaciones como `@Inject` y
  `@Singleton`. Es un punto medio entre resolver la inyección de dependencias a mano y usar frameworks como Spring.

- Un fork de [flbulgarelli/jpa-extras] (wrapper de [JPA] y [Hibernate 5] con fines educativos) que incluye soporte para
  variables de entorno (ver [Pull Request]).


[Java 11]: https://www.oracle.com/java/technologies/javase-jdk11-downloads.html
[Javalin]: https://javalin.io/
[comparativa entre Javalin y Spark]: https://javalin.io/comparisons/sparkjava
[JTE]: https://jte.gg/
[Razor]: https://learn.microsoft.com/en-us/aspnet/core/mvc/views/razor?view=aspnetcore-7.0
[plugin para IntelliJ IDEA]: https://plugins.jetbrains.com/plugin/13407-jte
[Guice]: https://github.com/google/guice
[flbulgarelli/jpa-extras]: https://github.com/flbulgarelli/jpa-extras
[JPA]: https://en.wikipedia.org/wiki/Java_Persistence_API
[Hibernate 5]: https://hibernate.org/orm/releases/5.4/
[Pull Request]: https://github.com/flbulgarelli/jpa-extras/pull/2

## Configuración en local

### IntelliJ IDEA

- Configurar el JDK 11:
  - Ir a `File > Project Structure > Project`
  - En la sección `Project SDK`, seleccionar `New...` y seleccionar el JDK 11
  - En la sección `Project language level`, seleccionar `11 - Lambdas, type annotations etc.`
- Instalar los plugins para [JTE](https://plugins.jetbrains.com/plugin/13407-jte) y
  [Guice](https://plugins.jetbrains.com/plugin/16876-guice)

### Base de datos

#### Paso 1: Instalar PostgreSQL

Para instalar la base de datos, hay dos opciones: instalar PostgreSQL localmente o usar Docker Compose. Yo recomiendo 
Docker Compose, ya que es más fácil de configurar y no requiere instalar nada en el sistema.

##### Opción 1: PostgreSQL local

- Instalar [PostgreSQL](https://www.postgresql.org/download/)
- Crear un usuario llamado `postgres` con contraseña `postgres`.
- Crear una base de datos llamada `example`.

En caso de querer usar otro nombre de usuario, contraseña o base de datos, se debe modificar el archivo
`src/main/resources/META-INF/persistence.xml` con los valores correspondientes.

##### Opción 2: Docker Compose

- Instalar [Docker] y [Docker Compose]
- Desde consola,
  - Ejecutar `docker volume create --name=example-data` para crear un volumen para guardar los datos de la base de datos
  - Ejecutar `docker-compose up db` cada vez que querramos iniciar la base de datos. Podemos apagarla con `Ctrl+C`.

[Docker]: https://docs.docker.com/get-docker/
[Docker Compose]: https://docs.docker.com/compose/install/

#### Paso 2: Insertar datos

Una vez iniciados PostgreSQL, debemos ejecutar la clase `io.github.raniagus.example.Bootstrap` para crear las tablas e
insertar datos de prueba leyendo los CSV que se encuentran en `src/main/resources/data/`.
Estos datos fueron generados con [Mockaroo](https://mockaroo.com/).

## Ejecutar la aplicación

- Ejecutar desde IntelliJ `io.github.raniagus.example.Application` para iniciar la aplicación.

## Despliegue

Para desplegar la aplicación, hay tres opciones: Docker Compose, Railway o Fly.io. Para cada una de ellas, se deben
agregar las variables de entorno necesarias para conectarse a la base de datos:
- `DB_URL`: La URL a la base de datos, ej.: `jdbc:postgresql://example-db:5432/example`
- `DB_USERNAME`: El usuario de la base de datos
- `DB_PASSWORD`: La contraseña de la base de datos
- `PORT`: El puerto en el que se ejecutará la aplicación, que siempre será el `80`

En local esto no hizo falta, ya que en caso de que no se especifiquen estas variables, se usan los valores por defecto
que se encuentran en `src/main/resources/META-INF/persistence.xml` (ver clase 
`io.github.raniagus.example.config.Configuration`).

### Opción 1: Docker Compose

La primera opción es usar una máquina virtual, como las que provee [Digital Ocean](https://www.digitalocean.com/), para
desplegar la aplicación completa (incluyendo la base de datos) en un servidor con Docker Compose instalado.

Simplemente, debemos clonar el repositorio, crear un archivo `.env` con las variables de entorno necesarias y ejecutar
`docker-compose up -d`

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
      - `DB_USERNAME`: El usuario de la base de datos
      - `DB_PASSWORD`: La contraseña de la base de datos
      - `PORT`: El puerto en el que se ejecutará la aplicación, que siempre será el `80`
    - Generar un dominio para el proyecto

Con el repositorio vinculado a Railway, cada vez que se haga un push a la rama `main`, Railway se encargará de hacer
un build de la aplicación y desplegarla en el servidor.

### Opción 3 (recomendada): Fly.io + CockroachDB

La última opción es [Fly.io](https://fly.io/) para desplegar la aplicación.

También provee una base de datos PostgreSQL, pero en este caso usaremos [CockroachDB](https://www.cockroachlabs.com/),
ya que el plan gratuito de Fly.io permite hasta 2 nodos de forma gratuita y probablemente querramos usar el segundo
para desplegar un cronjob.

Los pasos que debemos seguir son:

- Crear una cuenta en [CockroachCloud](https://cockroachlabs.cloud/)
- Crear un cluster de CockroachDB y copiar el connection string

Luego,

- Instalar el [CLI de Fly](https://fly.io/docs/hands-on/install-flyctl/)
- Crear una cuenta ejecutando `flyctl auth signup` o registrarse con `flyctl auth login`
- Seguir los pasos para [desplegar via Dockerfile](https://fly.io/docs/languages-and-frameworks/dockerfile/)
- Agregar las siguientes variables de entorno:
  - `DB_URL`: La URL a la base de datos, ej.: `jdbc:postgresql://example-db:5432/example`
  - `DB_USERNAME`: El usuario de la base de datos
  - `DB_PASSWORD`: La contraseña de la base de datos
  - `PORT`: El puerto en el que se ejecutará la aplicación, que siempre será el `80`

Esta es la opción que más recomiendo actualmente, ya que Fly.io:
- Provee un plan gratuito que permite hasta 2 aplicaciones desplegadas por tiempo ilimitado;
- Las contraseñas se pueden almacenar de forma segura usando `flyctl secrets set` (revisar la
  [documentación](https://fly.io/docs/reference/secrets/)); y
- Como el despliegue es manual a través de un comando, no es necesario crear commits para disparar el webhook en caso de
  habernos equivocado al configurar una variable de entorno o querer desplegar una versión anterior.
- De todas formas, es posible configurar Continuous Deployment a través de GitHub Actions siguiendo
  [este tutorial](https://fly.io/docs/app-guides/continuous-deployment-with-github-actions/). 

Además, por el lado de CockroachDB, al ser un servicio específico para bases de datos, provee muchas más opciones que
Railway para monitorear y administrar la base de datos. También podrías usar Railway para desplegar la aplicación y CockroachDB para la base de datos.
