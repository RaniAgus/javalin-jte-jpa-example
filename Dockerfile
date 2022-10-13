FROM maven:3.6.3-jdk-11-slim AS builder

WORKDIR /app

COPY pom.xml .

RUN ["/usr/local/bin/mvn-entrypoint.sh", "mvn", "verify", "clean", "--fail-never"]

COPY src ./src

RUN mvn package

FROM alpine:3.16 as web

RUN apk --no-cache add openjdk11

WORKDIR /app

COPY public ./public

COPY --from=builder /app/target/*-with-dependencies.jar ./app.jar
COPY --from=builder /app/jte-classes ./jte-classes

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
