FROM openjdk:25-ea-4-jdk-oraclelinux9
WORKDIR /app
COPY ./target/ target/

# Environment Variables
ENV USERS_FILE=/app/data/users.json
ENV PRODUCTS_FILE =/app/data/products.json
ENV CARTS_FILE =/app/data/carts.json
ENV ORDERS_FILE =/app/data/orders.json

RUN mvn clean install

EXPOSE 8080
CMD ["java","-jar","/app/target/mini1.jar"]