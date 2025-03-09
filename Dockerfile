FROM openjdk:23-jdk-slim

WORKDIR /app

COPY ./target/ target/

VOLUME ["/app/data"]

COPY ./src/main/java/com/example/data/carts.json /app/data/carts.json
COPY ./src/main/java/com/example/data/orders.json /app/data/orders.json
COPY ./src/main/java/com/example/data/products.json /app/data/products.json
COPY ./src/main/java/com/example/data/users.json /app/data/users.json

ENV CARTS_JSON_PATH=/app/data/carts.json
ENV ORDERS_JSON_PATH=/app/data/orders.json
ENV PRODUCTS_JSON_PATH=/app/data/products.json
ENV USERS_JSON_PATH=/app/data/users.json

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "target/mini1.jar"]
