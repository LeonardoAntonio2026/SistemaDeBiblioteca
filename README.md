# Sistema de Biblioteca (API REST)

Backend desarrollado en **Spring Boot** para la gestión integral de una biblioteca: catálogo de títulos, ejemplares físicos, lectores, préstamos, devoluciones, reservas en cola y multas.

---

## Requisitos previos

Para compilar y ejecutar este proyecto necesitas tener instalado:

* **Java JDK 21** (versión configurada en el proyecto).
* **MySQL 8.0 o superior** (en ejecución local o remota).
* **Maven 3.10+** *(opcional, el proyecto incluye Maven Wrapper `./mvnw` y `mvnw.cmd`)*.

---
## Cómo ejecutar el proyecto

### 1. Clonar el repositorio y entrar al directorio

```bash
git clone <URL_DEL_REPOSITORIO>
cd SistemaDeBiblioteca

```Linux o macOS
./mvnw spring-boot:run

```Windoes
mvnw.cmd spring-boot:run