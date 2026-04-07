# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build
./mvnw clean package

# Run
./mvnw spring-boot:run

# Run tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=ProductosApiApplicationTests
```

## Architecture

Spring Boot 3.5 REST API backed by an Oracle XE database (`localhost:1521/XEPDB1`). Java 17. Schema is managed externally (`ddl-auto=none`).

**Package layout:** `com.example.demo`
- `domain/` — JPA entities (`Producto`, `Categoria`, `Venta`, `Usuario`)
- `repository/` — Spring Data JPA repositories
- `service/` — Business logic; entities are mapped to DTOs here before returning to controllers
- `controller/` — REST controllers
- `controller/exception/` — Global exception handler (`GlobalExceptionHandler`)
- `dto/` — Response/request shapes (`ProductoResponseDTO`, `VentaResponseDTO`, `ProductoRequest`, `UsuarioDTO`)
- `security/` — JWT auth (`JwUtil`, `JwtFilter`, `SecurityConfig`)

**Authentication flow:**
- `POST /api/auth/register` and `POST /api/auth/login` are public; all other endpoints require a `Bearer <token>` header.
- `JwtFilter` validates the token and sets the Spring Security context with `ROLE_<role>` authority.
- JWT secret and expiry (1 hour) are configured in `application.properties` via `jwt.secret`.

**Key domain relationships:**
- `Producto` → `Categoria` (ManyToOne, lazy)
- `Venta` → `Producto` (ManyToOne, lazy); `total` and `fechaVenta` are `insertable=false, updatable=false` — computed/defaulted by the database.

**Pagination:** All list endpoints accept Spring `Pageable` query params (`page`, `size`, `sort`).

**Date filtering in `VentaRepository`:** Uses a native Oracle query with `TRUNC()` to compare dates, since `FECHA_VENTA` is a `TIMESTAMP` column.

**`ProductoService.convertirDTO`** calls `producto.getCategoria().getNombre()` — a producto without a categoria will throw NPE. Every saved producto must have a valid `categoriaId`.

## Roles

Roles definidos para el sistema. `ADMIN` solo puede crearse directamente en BD o por otro ADMIN autenticado.

| Rol | Descripción |
|-----|-------------|
| `ADMIN` | Acceso total: usuarios, productos, categorías, ventas, stock |
| `VENDOR` | Ventas (lectura), productos y categorías (solo lectura) |
| `WAREHOUSE` | Stock (lectura/escritura), productos (solo lectura) |
| `USER` | Productos y categorías (solo lectura) |

`UsuarioDTO` restringe el registro público a `USER`, `VENDOR` y `WAREHOUSE` mediante `@Pattern`.

## Pending improvements

### High priority
- **`GlobalExceptionHandler` incomplete** (`controller/exception/GlobalExceptionHandler.java`) — Missing handlers for `EntityNotFoundException` (404) and generic `Exception` (500). Only `MethodArgumentNotValidException` is currently handled.
- **`SecurityConfig` sin reglas por rol** (`security/SecurityConfig.java`) — Solo valida que el usuario esté autenticado. Falta configurar `hasRole`/`hasAnyRole` por endpoint para `ADMIN`, `VENDOR`, `WAREHOUSE` y `USER`.

### Medium priority
- **NPE in `ProductoService.convertirDTO`** (`service/ProductoService.java:70`) — `producto.getCategoria().getNombre()` is called without null check.
- **`obtenerPorId` returns `null`** (`service/ProductoService.java:35`) — Should throw `EntityNotFoundException` instead of returning null; requires corresponding handler in `GlobalExceptionHandler`.
- **Inconsistent responses** — `GET /api/productos/{id}` returns the raw `Producto` JPA entity while list endpoints return `ProductoResponseDTO`. All endpoints should use DTOs.
- **Field `@Autowired` vs constructor injection** — `ProductoService` and `VentaService` use field injection, which makes unit testing harder. Follow the constructor injection pattern already used in `AuthController` and `UsuarioService`.
- **Control de acceso a nivel de datos** — Un `VENDOR` puede ver todas las ventas, no solo las propias. Filtrar por usuario del token en `VentaService`.
- **Jerarquía de roles no configurada** — `ADMIN` no hereda permisos de roles inferiores automáticamente. Configurar `RoleHierarchy` o listar roles explícitamente con `hasAnyRole`.
- **Tabla `STOCK` pendiente** — Necesaria para el rol `WAREHOUSE`. Crear entidad, repositorio, servicio y controller.

### Low priority
- **`System.out.println` in production code** — `ProductoService.java:48` and `VentaController.java:39` use println for debug. Replace with SLF4J logger (already used correctly in `GlobalExceptionHandler`).
- **Nombre de variable fuera de convención** — `VentaService.java:19` declara `ventarepository` en minúsculas; debería ser `ventaRepository` (camelCase).
- **Missing `equals`/`hashCode` on JPA entities** — Can cause unexpected behavior in collections or dirty-checking.
- **No DB migration tool** — Schema is managed manually. Consider Flyway or Liquibase to version SQL scripts.
- **`PUT` and `DELETE` for productos are commented out** — `ProductoController.java:46-55`. The API currently has no way to update or delete products.
- **No sales creation endpoint** — `POST /api/ventas` does not exist; sales must be inserted directly in the database.
- **Sin invalidación de tokens JWT** — Si un usuario es eliminado o su rol cambia, su token sigue válido hasta expirar (1 hora). Considerar blacklist en Redis para producción crítica.
- **Sin auditoría de accesos** — No hay registro de quién accedió a qué endpoint. Considerar AOP o un filtro de auditoría.
- **JWT secret debe ser robusto** — Verificar que `jwt.secret` en `prop.env` tenga al menos 256 bits aleatorios y se rote periódicamente.

## Completed improvements
- **Global exception handler added** — `GlobalExceptionHandler` created in `controller/exception/`. Handles `MethodArgumentNotValidException` with field-level messages from `UsuarioDTO` validations.
- **`UsuarioRepository.existsUser` fixed** — Replaced non-functional `existsUser(Usuario)` with derived query `existsByUsernameAndRole(String, String)`.
- **Credentials externalized** — `DB_USERNAME`, `DB_PASSWORD` y `JWT_SECRET` movidos a variables de entorno en `application.properties`; ya no hay valores en texto plano.
- **Role escalation on register fixed** — `UsuarioDTO` ahora restringe el campo `role` mediante `@Pattern(regexp="^(USER|VENDOR|WAREHOUSE)$")`. `ADMIN` no puede registrarse públicamente.
