# Actividad 4 - API REST con Spring Boot, Spring Security y JWT

API REST pura (solo JSON, sin Thymeleaf) con autenticación real mediante **Spring Security + JWT**, CRUD protegido de la entidad `Alumno` (con su relación `ManyToOne` a `Escuela`, retomada de la Actividad 3), DTOs, validación con `@Valid` y colección de pruebas en **Bruno**.

- Antonio vazquez karla guadalupe

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
<img width="1601" height="961" alt="image" src="https://github.com/user-attachments/assets/a3cd0ae6-659c-47ff-8179-04cb8004eb8c" />

### Login
<img width="1600" height="962" alt="image" src="https://github.com/user-attachments/assets/c37f9163-af32-48c9-a0f2-e0e7a98f155d" />

### Crear (POST)
<img width="1605" height="962" alt="image" src="https://github.com/user-attachments/assets/2d79a005-7eb2-47ce-b16d-4f363f216d11" />
<img width="1600" height="965" alt="image" src="https://github.com/user-attachments/assets/f0d4645d-d395-48b1-9013-bb6b1a3a2218" />

### Listar con paginación (GET)
<img width="1598" height="962" alt="image" src="https://github.com/user-attachments/assets/90aa335a-1ddd-4943-803f-d556e2fe50e6" />

### Obtener por id (GET)
<img width="1598" height="963" alt="image" src="https://github.com/user-attachments/assets/7e2af41f-02bf-40cb-9375-ad8f5c2f4d28" />

### Actualizar (PUT)
<img width="1599" height="957" alt="image" src="https://github.com/user-attachments/assets/73a9cb93-94fc-4a89-933b-e52fc0513717" />

### Eliminar (DELETE)
<img width="1598" height="965" alt="image" src="https://github.com/user-attachments/assets/e8217fad-b7b5-4b13-8f2b-e71bf33d31ed" />

### Endpoint protegido sin token (401)
<img width="1599" height="962" alt="image" src="https://github.com/user-attachments/assets/d9700bbc-dbe7-42a2-a1b9-d4fc856d3676" />

