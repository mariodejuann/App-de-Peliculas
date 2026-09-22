# MoviesApp-Android

Aplicación Android nativa (Kotlin) para gestionar un catálogo de películas: alta, edición, borrado y consulta de fichas con detalle de actores y géneros. Incluye caché local para funcionar sin conexión y carga automática de carátulas mediante la API de The Movie Database (TMDB). Proyecto final de la asignatura de Desarrollo de Aplicaciones Móviles — Universidad San Jorge.

## Características

- **CRUD completo de películas** contra una API REST propia (crear, ver, editar y eliminar).
- **Ficha de detalle** con director, año, valoración, descripción y carátula obtenida automáticamente desde TMDB según el título.
- **Navegación a actores y géneros** de cada película, con sus respectivas películas relacionadas.
- **Caché offline**: los datos de películas, actores y géneros se guardan en `SharedPreferences` y se muestran automáticamente si el servidor no está disponible.
- **Pantalla de contacto** con acciones directas a email, web y llamada telefónica mediante `Intent`s del sistema.
- Interacción táctil sobre la lista: toque para ver detalle, pulsación larga para editar.

## Estructura del proyecto

```
app/src/main/java/es/usj/alu163300/mdj_moviesapp/
├── MainActivity.kt          # Lista de películas (RecyclerView) y punto de entrada
├── SplashActivity.kt        # Pantalla de carga inicial
├── AddMovieActivity.kt      # Alta de una nueva película (POST)
├── EditMovieActivity.kt     # Edición y borrado de una película (PUT / DELETE)
├── ViewMovieActivity.kt     # Detalle de la película + carátula vía TMDB
├── GenresActivity.kt        # Géneros de la película y sus películas relacionadas
├── ActorsActivity.kt        # Actores de la película y sus películas relacionadas
├── ContactActivity.kt       # Pantalla de contacto (email / web / teléfono)
├── models/
│   ├── Movie.kt
│   ├── Actor.kt
│   └── Genre.kt
└── adapters/
    └── MovieAdapter.kt      # Adapter del RecyclerView de la lista principal
```

## Arquitectura y funcionamiento

La app sigue un patrón sencillo de Activities por pantalla, sin capa de repositorio separada: cada Activity que necesita datos remotos lanza una corrutina (`CoroutineScope(Dispatchers.IO)`) que llama directamente a la API REST usando `HttpURLConnection`, serializa/deserializa con **Gson**, y actualiza la UI en el hilo principal (`runOnUiThread`).

Para tolerar la falta de conexión, cada pantalla que consulta datos remotos guarda la última respuesta en `SharedPreferences` y, si la petición falla, recurre a esa copia local en lugar de mostrar un error.

### Backend

La app consume una API REST en `http://10.0.2.2:8080` (la IP con la que el emulador de Android accede al `localhost` de la máquina anfitriona), con los siguientes endpoints:

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/movies` | Lista todas las películas |
| POST | `/movies` | Crea una película |
| PUT | `/movies` | Actualiza una película existente |
| DELETE | `/movies/{id}` | Elimina una película |
| GET | `/genres` | Lista los géneros disponibles |
| GET | `/actors` | Lista los actores disponibles |

Este repositorio contiene únicamente el cliente Android. Para probar la app con datos reales, es necesario levantar el servicio `movies-service` (proporcionado como imagen Docker para la asignatura) en el puerto `8080`:

```bash
docker pull anselm82/movies-service:latest
docker run -d --name movies-service -p 8080:8080 anselm82/movies-service:latest
```

Una vez levantado, la documentación interactiva de la API (Swagger) está disponible en:
```
http://localhost:8080/swagger-ui/index.html
```

Sin este backend corriendo, la app se apoya en la caché local (`SharedPreferences`) si ya se cargaron datos previamente en una sesión anterior.

## Requisitos

- Android Studio (Ladybug o superior recomendado).
- SDK de Android: mínimo API 26, compilado con API 36.
- JDK 11.
- Docker (para levantar el backend `movies-service`).
- Una API key gratuita de [TMDB](https://www.themoviedb.org/settings/api) para la carga de carátulas.

## Configuración

Antes de ejecutar el proyecto, añade tu propia API key de TMDB en:

```
app/src/main/java/es/usj/alu163300/mdj_moviesapp/ViewMovieActivity.kt
```

```kotlin
companion object {
    const val TMDB_API_KEY = "TU_API_KEY_AQUI"
}
```

## Ejecución

1. Abre la carpeta del proyecto en Android Studio.
2. Añade tu API key de TMDB como se indica arriba.
3. Levanta el backend con `docker run -d --name movies-service -p 8080:8080 anselm82/movies-service:latest` (necesario para cargar y guardar películas reales).
4. Ejecuta la app sobre un emulador o dispositivo físico (`Run ▶` en Android Studio).

## Dependencias principales

- [Gson](https://github.com/google/gson) — serialización/deserialización JSON.
- [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines) — llamadas de red en segundo plano.
- [Glide](https://github.com/bumptech/glide) — carga y caché de imágenes (carátulas).
- AndroidX RecyclerView.

## Autor

**Mario De Juan Sánchez Flor**
Estudiante de Doble Grado en Ingeniería en Ciberseguridad e Ingeniería Informática — Universidad San Jorge
[LinkedIn](https://www.linkedin.com/in/mariodejuan)

## Licencia

Proyecto académico desarrollado con fines educativos para la asignatura de Desarrollo de Aplicaciones Móviles.
