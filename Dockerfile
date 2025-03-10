FROM openjdk:25-ea-4-jdk-oraclelinux9
WORKDIR /app
COPY target/*.jar app.jar
VOLUME /app/src/main/java/com/example/data
ENV USERS_FILE=/app/src/main/java/com/example/data/users.json
ENV PRODUCTS_FILE=/app/src/main/java/com/example/data/products.json
ENV CARTS_FILE=/app/src/main/java/com/example/data/carts.json
ENV ORDERS_FILE=/app/src/main/java/com/example/data/orders.json
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar" ]