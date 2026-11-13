# Actividad 4 - API REST con Spring Boot, Spring Security y JWT

API REST pura (solo JSON, sin Thymeleaf) con autenticación real mediante **Spring Security + JWT**, CRUD protegido de la entidad `Alumno` (con su relación `ManyToOne` a `Escuela`, retomada de la Actividad 3), DTOs, validación con `@Valid` y colección de pruebas en **Bruno**.

- Antonio vazquez karla guadalupe
- **API en el VPS:** 

## Tecnologías

Java 17 · Spring Boot 3.3 · Spring Security · JJWT 0.12 · Spring Data JPA · MySQL · Bean Validation · Bruno

## Estructura

```
src/main/java/com/escuela/act4/
├── config/        SecurityConfig (cadena de filtros, 401/403 en JSON)
├── controller/    AuthController, AlumnoController, EscuelaController
├── dto/           Requests y responses (el password nunca sale)
├── exception/     GlobalExceptionHandler + ApiError
├── model/         User, Alumno, Escuela (entities)
├── repository/    UserRepository, AlumnoRepository, EscuelaRepository
├── security/      JwtService, JwtAuthFilter, CustomUserDetailsService
└── service/       AuthService, AlumnoService, EscuelaService
bruno/             Colección de Bruno (peticiones y entornos)
docs/img/          Capturas de pantalla para este README
```

## Endpoints

| Método | Ruta | Protegido | Respuesta |
|--------|------|-----------|-----------|
| POST | `/api/auth/register` | No | 201 + token / 400 / 409 |
| POST | `/api/auth/login` | No | 200 + token / 400 / 401 |
| GET | `/api/escuelas` | Sí | 200 |
| POST | `/api/escuelas` | Sí | 201 / 400 |
| GET | `/api/alumnos?page=0&size=10` | Sí | 200 (paginado) |
| GET | `/api/alumnos/{id}` | Sí | 200 / 404 |
| POST | `/api/alumnos` | Sí | 201 + `Location` / 400 / 404 (escuela) / 409 (matrícula repetida) |
| PUT | `/api/alumnos/{id}` | Sí | 200 / 400 / 404 / 409 |
| DELETE | `/api/alumnos/{id}` | Sí | 204 / 404 |

Un alumno pertenece a una escuela, así que primero se crea una escuela y luego se manda su `escuelaId` al crear o actualizar un alumno:

```json
{ "nombre": "Karla Antonio", "matricula": "A001", "escuelaId": 1 }
```

Sin token (o con token inválido/expirado) los endpoints protegidos responden **401**.

Los endpoints protegidos requieren el header: `Authorization: Bearer <token>`.

### Ejemplo de error de validación (400)

```json
{
  "timestamp": "2026-11-11T23:30:00",
  "status": 400,
  "error": "Bad Request",
  "mensaje": "Hay errores de validación en los datos enviados",
  "errores": {
    "nombre": "El nombre es obligatorio",
    "matricula": "La matrícula es obligatoria"
  }
}
```

## Ejecutar en local

1. Tener Java 17+, Maven y MySQL corriendo.
2. Configurar variables de entorno (o editar `application.properties`):

```bash
export DB_URL="jdbc:mysql://localhost:3306/act4_db?createDatabaseIfNotExist=true&serverTimezone=UTC"
export DB_USER=root
export DB_PASSWORD=tu_password
export JWT_SECRET="un-secreto-largo-de-al-menos-32-caracteres"
mvn spring-boot:run
```

## Probar con Bruno

1. Abrir Bruno → **Open Collection** → seleccionar la carpeta `bruno/`.
2. Elegir el entorno `local` (o `vps`, editando `baseUrl`).
3. Ejecutar en orden: `01 Registro` → `02 Login` (guardan el token automáticamente en `{{token}}`) → `03` (crea la escuela y guarda su `escuelaId`) → peticiones 04 a 08 del CRUD de alumnos.
4. `09` prueba un endpoint protegido **sin token** (debe dar 401) y `10` una validación fallida (400).


## Capturas de pantalla

### Registro
![Registro]

### Login
![Login]

### Crear (POST)
![Crear]

### Listar con paginación (GET)
![Listar]

### Obtener por id (GET)
![Obtener]

### Actualizar (PUT)
![Actualizar]

### Eliminar (DELETE)
![Eliminar]

### Endpoint protegido sin token (401)
![Sin token]
