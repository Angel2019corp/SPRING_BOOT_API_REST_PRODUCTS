# Rutas de la API y acceso por rol

Documento generado a partir del análisis de los controladores y `SecurityConfig`.

## Roles del sistema

| Rol | Descripción |
|-----|-------------|
| `ADMIN` | Acceso total: usuarios, productos, categorías, ventas, stock |
| `VENDOR` | Ventas (lectura), productos y categorías (solo lectura) |
| `WAREHOUSE` | Stock (lectura/escritura), productos (solo lectura) |
| `USER` | Productos y categorías (solo lectura) |

> El registro público (`POST /api/auth/register`) está restringido a `USER`, `VENDOR` y `WAREHOUSE` mediante `@Pattern` en `UsuarioDTO`. El rol `ADMIN` solo puede crearse directamente en BD o por otro `ADMIN` autenticado.

## Autenticación

Todas las rutas excepto `/api/auth/**` requieren un header `Authorization: Bearer <token>`. El token JWT se obtiene en `POST /api/auth/login` y expira en 1 hora.

- **401 Unauthorized** — token ausente, inválido o expirado (manejado por `JwtAuthenticationEntryPoint`).
- **403 Forbidden** — rol insuficiente (manejado por `JwtAccessDeniedHandler`).
- Ambas respuestas devuelven JSON con el formato `{"error": "..."}`.

## Rutas

### `AuthController` — `/api/auth`

| Método | Ruta | Acceso | Descripción |
|---|---|---|---|
| POST | `/api/auth/register` | Público | Registro de usuario. Body: `UsuarioDTO` (`username`, `password`, `role`). Solo acepta `USER`, `VENDOR`, `WAREHOUSE`. |
| POST | `/api/auth/login` | Público | Devuelve `{ "token": "<jwt>" }` si las credenciales son válidas. |

### `ProductoController` — `/api/productos`

| Método | Ruta | Acceso | Descripción |
|---|---|---|---|
| GET | `/api/productos` | `ADMIN`, `VENDOR`, `WAREHOUSE`, `USER` | Listado paginado de productos (`page`, `size`, `sort`). |
| GET | `/api/productos/categoria/{categoriaId}` | `ADMIN`, `VENDOR`, `WAREHOUSE`, `USER` | Productos filtrados por categoría, paginado. |
| GET | `/api/productos/{id}` | `ADMIN`, `VENDOR`, `WAREHOUSE`, `USER` | Detalle de un producto. Devuelve 404 si no existe. |
| POST | `/api/productos` | `ADMIN` | Crea un producto. Body: `ProductoRequest`. |

> `PUT /api/productos/{id}` y `DELETE /api/productos/{id}` están comentados en `ProductoController.java:46-55` y actualmente no existen.

### `VentaController` — `/api/ventas`

| Método | Ruta | Acceso | Descripción |
|---|---|---|---|
| GET | `/api/ventas` | `ADMIN`, `VENDOR` | Listado paginado de ventas. |
| GET | `/api/ventas/fechas?fechaInicio=YYYY-MM-DD&fechaFin=YYYY-MM-DD` | `ADMIN`, `VENDOR` | Ventas filtradas por rango de fechas, paginado. |

> No existe `POST /api/ventas`; las ventas se insertan directamente en la base de datos.

## Matriz resumen acceso/rol

| Ruta | ADMIN | VENDOR | WAREHOUSE | USER | Anónimo |
|---|:---:|:---:|:---:|:---:|:---:|
| `POST /api/auth/register` | ✅ | ✅ | ✅ | ✅ | ✅ |
| `POST /api/auth/login` | ✅ | ✅ | ✅ | ✅ | ✅ |
| `GET /api/productos` | ✅ | ✅ | ✅ | ✅ | ❌ |
| `GET /api/productos/categoria/{id}` | ✅ | ✅ | ✅ | ✅ | ❌ |
| `GET /api/productos/{id}` | ✅ | ✅ | ✅ | ✅ | ❌ |
| `POST /api/productos` | ✅ | ❌ | ❌ | ❌ | ❌ |
| `GET /api/ventas` | ✅ | ✅ | ❌ | ❌ | ❌ |
| `GET /api/ventas/fechas` | ✅ | ✅ | ❌ | ❌ | ❌ |

## Reglas de seguridad (`SecurityConfig.java:28-32`)

```java
.requestMatchers("/api/auth/**").permitAll()
.requestMatchers(HttpMethod.GET,  "/api/productos/**").hasAnyRole("ADMIN", "VENDOR", "WAREHOUSE", "USER")
.requestMatchers(HttpMethod.POST, "/api/productos/**").hasRole("ADMIN")
.requestMatchers(HttpMethod.GET,  "/api/ventas/**").hasAnyRole("ADMIN", "VENDOR")
.anyRequest().authenticated()
```

## Observaciones

- **Sin jerarquía de roles**: `ADMIN` aparece explícitamente en cada regla. Si se omite, no hereda permisos automáticamente.
- **`WAREHOUSE` sin endpoints propios**: la tabla `STOCK` y su controlador están pendientes.
- **`PUT`/`DELETE` de productos**: si se descomentan, caen bajo `anyRequest().authenticated()` sin restricción de rol — habría que añadir reglas explícitas (`hasRole("ADMIN")`).
- **Filtrado de ventas por usuario**: `VENDOR` actualmente ve todas las ventas, no solo las propias (pendiente de prioridad media).
