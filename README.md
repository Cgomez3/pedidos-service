# Microservicio de Carga de Pedidos CSV

## Descripción
Este microservicio se encarga de la **carga y gestión de pedidos** desde archivos CSV, aplicando validaciones de negocio y control de idempotencia para evitar cargas duplicadas.

Se implementa siguiendo **arquitectura hexagonal**, principios SOLID y buenas prácticas de Spring Boot 3, Spring Data JPA, Flyway Y PostgreSQL.

---

## Ejecución

## 1. Requisitos
- Java 17
- Maven 3.9+
- PostgreSQL 15+
- IDE recomendado: IntelliJ IDEA / Eclipse

## 2. Configuración del Proyecto
2.1. Clonar el Repositorio
```bash
git clone https://github.com/Cgomez3/pedidos-service.git
cd pedidos-service
```
2.2. Configurar la base de datos en src/main/resources/application.yaml
- Modificar el usuario(USUARIO) y la clave(CLAVE) para el acceso a la base de Datos.
- Modificar el nombre de la Base de Datos(NOMBRE_BD)
```yaml
  application:
    name: pedidos-service
  datasource:
    url: jdbc:postgresql://localhost:5432/NOMBRE_BD
    username: USUARIO
    password: CLAVE
    driver-class-name: org.postgresql.Driver
```
2.3. Ejecutar migraciones Flyway:
```Bash
mvn flyway:migrate
```

## 3. Ejecutar localmente

Ejecuta la aplicación con Maven:
```Bash
mvn spring-boot:run
```
La aplicación arrancará en:
```Url
http://localhost:8081
```
Endpoints disponibles:

- /pedidos – Gestión de pedidos

- /cargar – Carga batch de pedidos vía archivo CSV
```Url
http://localhost:8081/pedidos/cargar
```
## 4. Estrategia de Batch

El microservicio implementa procesamiento batch de pedidos:

1. Carga de archivos CSV vía endpoint /cargar (multipart/form-data).

2. Cada registro del CSV incluye:
```
numeroPedido, clienteId, fechaEntrega, estado, zonaEntrega, requiereRefrigeracion
```
3. Validación y procesamiento:

- Validación de datos (fechas, cliente, zona).

- Detección de duplicados usando Idempotency Key.

- Inserción en la base de datos de manera transaccional para cada batch.

4. Migraciones Flyway aseguran que la base de datos siempre esté sincronizada con los scripts de creación de tablas.

5. Errores y auditoría: Se registran errores por fila para facilitar re-procesamiento sin afectar el batch completo.

## 5. Comandos útiles
```comandos
# Ejecutar tests
mvn test

# Limpiar target y recompilar
mvn clean install

# Reparar checksums Flyway
mvn flyway:repair
```

## 6. Estructura del proyecto
```Estructura
pedidos-service/
├─ .idea/                   # Configuración de IntelliJ
├─ .mvn/                    # Wrapper de Maven
├─ src/
│  ├─ main/
│  │  ├─ java/com/prueba/pedidos/pedidos_service/
│  │  │  ├─ aplicacion/      # Casos de uso y servicios
│  │  │  ├─ domain/          # Entidades, agregados y lógica de negocio
│  │  │  ├─ infrastructure/  # Repositorios y adaptadores
│  │  │  └─ shared/          # Clases/utilidades comunes
│  │  │  └─ PedidosServiceApplication.java
│  │  └─ resources/
│  │     ├─ db/migration/     # Scripts Flyway
│  │     ├─ application.yaml
│  │     └─ logback-spring.xml
│  └─ test/
├─ target/                  # Compilados (ignorado por Git)
├─ .gitignore
├─ .gitattributes
├─ HELP.md
├─ mvnw
├─ mvnw.cmd
└─ pom.xml
```