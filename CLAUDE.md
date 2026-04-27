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
- `security/` — JWT auth (`JwUtil`, `JwtFilter`, `SecurityConfig`, `JwtAuthenticationEntryPoint`, `JwtAccessDeniedHandler`)

**Authentication flow:**
- `POST /api/auth/register` and `POST /api/auth/login` are public; all other endpoints require a `Bearer <token>` header.
- `JwtFilter` validates the token and sets the Spring Security context with `ROLE_<role>` authority. Si el token está expirado o es inválido, delega en `JwtAuthenticationEntryPoint` para devolver un 401 JSON y corta la cadena con `return`.
- `JwtAuthenticationEntryPoint` maneja los 401 (token ausente/inválido/expirado) devolviendo `{"error": "..."}`. `JwtAccessDeniedHandler` maneja los 403 (rol insuficiente) con el mismo formato. Ambos están registrados en `SecurityConfig` vía `exceptionHandling(...)`.
- JWT secret and expiry (1 hour) are configured in `application.properties` via `jwt.secret`.

**Key domain relationships:**
- `Producto` → `Categoria` (ManyToOne, lazy)
- `Venta` → `Producto` (ManyToOne, lazy); `total` and `fechaVenta` are `insertable=false, updatable=false` — computed/defaulted by the database.

**Pagination:** All list endpoints accept Spring `Pageable` query params (`page`, `size`, `sort`).

**Date filtering in `VentaRepository`:** Uses a native Oracle query with `TRUNC()` to compare dates, since `FECHA_VENTA` is a `TIMESTAMP` column.

**`ProductoService.convertirDTO`** returns `"Sin categoria"` when `getCategoria()` is null. Every saved producto should have a valid `categoriaId`.

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

### Medium priority
- **Control de acceso a nivel de datos** — Un `VENDOR` puede ver todas las ventas, no solo las propias. Filtrar por usuario del token en `VentaService`.
- **Jerarquía de roles no configurada** — `ADMIN` no hereda permisos de roles inferiores automáticamente. Configurar `RoleHierarchy` o listar roles explícitamente con `hasAnyRole`.
- **Tabla `STOCK` pendiente** — Necesaria para el rol `WAREHOUSE`. Crear entidad, repositorio, servicio y controller.

### Low priority
- **`System.out.println` in production code** — `VentaController.java:39` usa println para debug. Reemplazar con logger SLF4J (ya se usa correctamente en `GlobalExceptionHandler`).
- **Missing `equals`/`hashCode` on JPA entities** — Can cause unexpected behavior in collections or dirty-checking.
- **No DB migration tool** — Schema is managed manually. Consider Flyway or Liquibase to version SQL scripts.
- **`PUT` and `DELETE` for productos are commented out** — `ProductoController.java:46-55`. The API currently has no way to update or delete products.
- **No sales creation endpoint** — `POST /api/ventas` does not exist; sales must be inserted directly in the database.
- **Sin invalidación de tokens JWT** — Si un usuario es eliminado o su rol cambia, su token sigue válido hasta expirar (1 hora). Considerar blacklist en Redis para producción crítica.
- **Sin auditoría de accesos** — No hay registro de quién accedió a qué endpoint. Considerar AOP o un filtro de auditoría.
- **JWT secret debe ser robusto** — Verificar que `jwt.secret` en `prop.env` tenga al menos 256 bits aleatorios y se rote periódicamente.

## Completed improvements
- **Inyección por constructor en services** — `ProductoService` y `VentaService` ahora reciben sus dependencias por constructor en lugar de `@Autowired` en campos, alineándose con `AuthController` y `UsuarioService`. Facilita los tests unitarios.
- **`ventarepository` renombrado a `ventaRepository`** — Campo, parámetro del constructor y todos los usos en `VentaService` ahora siguen camelCase.
- **`System.out.println` removido de `ProductoService`** — Eliminado el println de debug en `guardar`. Falta aún el de `VentaController.java:39`.
- **`SecurityConfig` manejo de 401/403 con JSON** — Configurado `exceptionHandling` con `JwtAuthenticationEntryPoint` (401) y `JwtAccessDeniedHandler` (403). Ambos serializan `{"error": "..."}` con `ObjectMapper`. `JwtFilter` delega en el `AuthenticationEntryPoint` los casos de `ExpiredJwtException`, `JwtException` e `IllegalArgumentException`, cortando la cadena con `return` para no escribir el response dos veces.
- **Global exception handler completo** — `GlobalExceptionHandler` maneja `MethodArgumentNotValidException` (400), `EntityNotFoundException` (404) y `Exception` genérico (500). El 500 no expone el stack trace.
- **`obtenerPorId` lanza excepción** — `ProductoService.obtenerPorId` ahora lanza `EntityNotFoundException` en lugar de devolver `null`.
- **NPE corregido en `convertirDTO`** — `ProductoService.convertirDTO` tiene null check en `getCategoria()`; devuelve `"Sin categoria"` si es null.
- **`UsuarioRepository.existsUser` fixed** — Replaced non-functional `existsUser(Usuario)` with derived query `existsByUsernameAndRole(String, String)`.
- **Credentials externalized** — `DB_USERNAME`, `DB_PASSWORD` y `JWT_SECRET` movidos a variables de entorno en `application.properties`; ya no hay valores en texto plano.
- **Role escalation on register fixed** — `UsuarioDTO` ahora restringe el campo `role` mediante `@Pattern(regexp="^(USER|VENDOR|WAREHOUSE)$")`. `ADMIN` no puede registrarse públicamente.
- **Respuesta consistente en `GET /api/productos/{id}`** — `ProductoService.obtenerPorId` ahora devuelve `ProductoResponseDTO` usando `.map(this::convertirDTO)` en lugar de la entidad JPA cruda.
- **`SecurityConfig` acceso a GET productos corregido** — `USER` y `WAREHOUSE` agregados a `hasAnyRole` en la regla de GET `/api/productos/**`; todos los roles tienen acceso de lectura.
