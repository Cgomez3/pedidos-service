# Microservicio de Pedidos

## Descripción
Este microservicio se encarga de la **carga y gestión de pedidos** desde archivos CSV, aplicando validaciones de negocio y control de idempotencia para evitar cargas duplicadas.

Se implementa siguiendo **arquitectura hexagonal**, principios SOLID y buenas prácticas de Spring Boot 3, Spring Data JPA y PostgreSQL.

---

## Ejecución

### Requisitos
- Java 17
- Maven 3.9+
- PostgreSQL 15+
- IDE recomendado: IntelliJ IDEA / Eclipse

### Configuración
1. Configura el archivo `application.yml` con la conexión a tu base de datos:
```yaml
server:
  port: 8081
spring:
  application:
    name: pedidos-service
  datasource:
    url: jdbc:postgresql://localhost:5432/pedido_bd
    username: postgres
    password: cgomez84
    driver-class-name: org.postgresql.Driver

  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        jdbc:
          batch_size: 1000
        order_inserts: true
        order_updates: true

    sql:
      init:
        mode: always

  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true
  security:
    jwt:
      secret: ASDFSSDFFRRTGFGHTYYHHGGGHHGHHHHUUUYIYUIYUIYUIUYYIYUIYPOOOKKADSJABNBCNBASDHAJDJAJKAKSDKASD

pedidos:
  batch-size: 1000