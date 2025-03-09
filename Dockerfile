FROM openjdk:23-jdk-slim

WORKDIR /app

COPY ./target/ target/

ENV CARTS_JSON_PATH=/app/data/carts.json
ENV ORDERS_JSON_PATH=/app/data/orders.json
ENV PRODUCTS_JSON_PATH=/app/data/products.json
ENV USERS_JSON_PATH=/app/data/users.json

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "target/mini1.jar"]