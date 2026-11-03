# Springfield Character Explorer

App Android (Jetpack Compose) para explorar personajes, ubicaciones y episodios de Los Simpson,
con datos de [The Simpsons API](https://thesimpsonsapi.com).

| | |
|---|---|
| Versión | `1.1.0-demo` (`psVersionCode = 5`, `psVersionName = "1.1.0"` en `app/build.gradle.kts`) |
| SDK | minSdk 24 · targetSdk 36 |
| Package | `com.mobilitydevai.sce` |

---

## Funcionalidades

### Navegación

- `MainPager`: `HorizontalPager` con 4 pestañas (Personajes, Ubicaciones, Episodios, Favoritos)
  y una barra de botones inferior (`IOSStyleSegmentedControl`).
- Pantalla edge-to-edge.
- El botón atrás (botón o gesto) **nunca cierra la app**: en otra pestaña vuelve a Personajes;
  en Personajes no hace nada.

### Personajes (`view/simpsons`)

- Carrusel de personajes con **carga automática** de la siguiente página al llegar al final.
- El ítem seleccionado queda **centrado**.
- Los huecos de los extremos del carrusel muestran una dona (`Modifier.carouselEdgeDonuts`,
  `drawable/simpsons_donut.png`).
- Cambio de tema claro/oscuro.
- Detalle del personaje (`view/detail/CharacterScreen.kt`): lectura en voz alta (Text-to-Speech)
  y añadir/quitar de favoritos.

### Ubicaciones (`view/location`)

- Lista paginada de ubicaciones con **likes** anónimos guardados en Firestore
  (ver [Firestore: modelo de datos](#firestore-modelo-de-datos)).

### Episodios (`view/episode`)

- Carrusel con los episodios de la **temporada seleccionada**. Cada card muestra imagen, sinopsis,
  `Chapter n / Season n` y fecha de emisión; el título del episodio centrado aparece arriba.
- Debajo, dos filas (`LazyRow`) con chips `OutlinedLabel`:
  - **Temporadas**: `Season 01` … `Season 35`.
  - **Capítulos** de la temporada seleccionada: `Chapter 01` … `Chapter n`.
  - El chip seleccionado es sólido (fondo `YellowDarkDev`, texto `DarkBackground`); el resto, solo borde.
- Pulsar una **temporada**: se muestra el loading de la app (`CustomHomerLoading`) sobre el carrusel
  y se carga desde su primer capítulo. El loading es un retardo de `SEASON_LOADING_MILLIS` (600 ms):
  los episodios ya están en memoria.
- Pulsar un **capítulo**: el carrusel se desliza desde el ítem actual hasta ese capítulo
  (`animateScrollBy` con `tween`, 300 ms + 120 ms por ítem, máximo 1,5 s).
- Mover el carrusel a mano cambia el capítulo seleccionado, y la fila de capítulos se desplaza
  lo justo para que el chip seleccionado **siempre quede a la vista**.

### Favoritos (`view/favorite`)

- Personajes guardados en local con **Room** (`FavouriteCharacter.db`).

### Actualización forzada

- Pantalla de bloqueo si la versión instalada es menor que la mínima configurada en Remote Config
  (ver [Remote Config: actualización forzada](#remote-config-actualización-forzada)).

---

## The Simpsons API

`BASE_URL` = `https://thesimpsonsapi.com/api/` (prod y dev).
Imágenes: `https://cdn.thesimpsonsapi.com/500` y `/200` (`config/Constants.kt`).

| Endpoint | Cliente | Paginación |
|---|---|---|
| `characters` | Retrofit (`SimpsonsClient`) | 20 por página, 60 páginas |
| `locations` | Retrofit (`SimpsonsClient`) | Paginado |
| `episodes` | OkHttp + Gson (`EpisodesOkHttpClient`) | 20 por página, 39 páginas (768 episodios) |

### Episodios y temporadas

- La API **no tiene endpoint de temporadas ni filtro por temporada** (`?season=` se ignora).
- `EpisodesRepositoryImpl.fetchSeasons()` pide la página 1, lee `pages` y descarga el resto
  **en paralelo**; después agrupa los episodios por `season` (`SeasonEntity(number, episodes)`).
- Si falla **cualquier** página devuelve `null` y no se muestran temporadas (unos conteos
  incompletos serían incorrectos).
- `episode_number` es **global** (no se reinicia en cada temporada): el número de capítulo que
  muestra la app es la posición del episodio dentro de su temporada (`index + 1`).

---

## Arquitectura

```
com.mobilitydevai.sce
├── view/        Pantallas Compose + ViewModels (MVVM, StateFlow)
├── domain/      entity/, repository/ (interfaces), usecase/
├── data/        network/, response/ (DTO + toDomain), repository/ (local Room, remote Firebase), di/
├── update/      Actualización forzada (Remote Config + In-App Updates)
├── notification/, analytics/, config/, ui/theme/
```

- Inyección de dependencias con **Hilt** (`data/di/DataModule.kt`).
- Flujo: `Screen` → `ViewModel` → `UseCase` → `Repository` (interfaz en `domain`, implementación en `data`).

### Stack

| Área | Librerías |
|---|---|
| UI | Jetpack Compose (BOM), Navigation Compose, Coil 3, Lottie, Accompanist SystemUiController |
| DI | Hilt |
| Red | Retrofit + Gson, OkHttp |
| Persistencia | Room, DataStore Preferences |
| Firebase | Auth (anónima), Firestore, Remote Config, Crashlytics, Analytics, Cloud Messaging |
| Google Play | In-App Updates |
| Tests | JUnit, MockK, kotlinx-coroutines-test, Espresso |

---

## Compilar y ejecutar

**Requisito: JDK 17.** Gradle 8.13 falla con JDK 25 (error que solo muestra `25.0.1`).
Usar el JBR de Android Studio:

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
```

### Flavors y build types

| Flavor | `applicationIdSuffix` | `BASE_URL` |
|---|---|---|
| `prod` | — | `https://thesimpsonsapi.com/api/` |
| `dev` | `.dev` | `https://thesimpsonsapi.com/api/` |

Build types: `debug` (sufijo `.debug`) y `release` (sufijo `.release`, minify activado).
Ejemplo: `devDebug` → `com.mobilitydevai.sce.dev.debug`.

```bash
./gradlew :app:installDevDebug          # instalar en el dispositivo/emulador
./gradlew :app:compileDevDebugKotlin    # solo compilar
./gradlew :app:testDevDebugUnitTest     # tests unitarios
```

Notificaciones push: ver [`FCM_DEBUG_GUIDE.md`](FCM_DEBUG_GUIDE.md).

---

## Pendientes y problemas conocidos

- [ ] **Desplegar las reglas de Firestore** (`firebase deploy --only firestore:rules`). Las reglas de
      producción caducaron el 2025-12-20: hasta desplegar, Firestore deniega todas las peticiones de la app.
- [ ] **Habilitar el proveedor Anónimo** en Firebase Authentication (necesario para los likes).
- [ ] Subir `psVersionCode` a **6** para la próxima publicación en Play.
- [ ] El build type `release` usa `applicationIdSuffix = ".release"`, que no está registrado en
      `google-services.json`: `assembleProdRelease` falla, y Play lo trataría como otra app.
- [ ] `LocationsViewModelTest` no compila (constructor y campos antiguos); revisar también
      `SimpsonsViewModelTest` tras `loadNextPage`.
- [ ] Borrar las colecciones antiguas de Firestore (`images-sce`, `images-vote`).
- [ ] `EpisodesEntity` y `EpisodesResponse.toDomain()` ya no se usan.
- [ ] Verificar en el emulador las donas del final del carrusel de personajes (después de la última página).

---

## Firebase

| | |
|---|---|
| Proyecto | `springfield-character-explorer` |
| Apps registradas | `com.mobilitydevai.sce` (prod), `com.mobilitydevai.sce.dev.debug`, `com.mobilitydevai.sce.dev.release` |
| Configuración en el repo | `firebase.json`, `.firebaserc`, `firestore.rules` |
| Autenticación | **Anónima** (Authentication → Método de inicio de sesión → Anónimo debe estar **habilitado**) |

La app no tiene login: al dar like se crea en silencio una sesión anónima de Firebase
(`UserRepository.getOrCreateAnonymousUid()`), que las reglas de Firestore exigen para escribir.

---

## Remote Config: actualización forzada

La app compara su `versionCode` con el parámetro **`min_version_code`** de Remote Config.
Si `versionCode < min_version_code`, muestra una pantalla de bloqueo (`ForceUpdateScreen`) y
el usuario solo puede pulsar **Actualizar** (Google Play In-App Updates o, si no está disponible,
la ficha de Play Store).

### Parámetro

| Campo | Valor |
|---|---|
| Plantilla | **Cliente** (⚠️ no "Servidor": los parámetros de Servidor nunca llegan a la app) |
| Nombre | `min_version_code` |
| Tipo | Number |
| Valor | `versionCode` mínimo permitido (p. ej. `5`) |
| Valor por defecto en la app | `0` (no bloquea nada si no hay configuración) |

### Cuándo se comprueba

- Solo al **abrir la app** (no en tiempo real ni al volver de segundo plano).
- **Debug**: sin caché, el cambio se aplica en el siguiente arranque.
- **Release**: caché de **1 hora** (`ForceUpdateRepository`).
- Sin conexión se usa el último valor descargado.

Código: `update/ForceUpdateRepository.kt`, `update/ForceUpdateViewModel.kt`, `update/ForceUpdateScreen.kt`, `MainActivity.kt`.

### Directivas al cambiar `versionCode`

El `versionCode` (`psVersionCode` en `app/build.gradle.kts`) está ligado a `min_version_code`:

1. **Nunca bajar** `psVersionCode`: si queda por debajo de `min_version_code`, la app se bloquea.
2. Subir `psVersionCode` en cada versión que se publique en Play.
3. Subir `min_version_code` en la consola **solo cuando Play ya haya distribuido** la nueva versión
   (el despliegue puede tardar horas). Si se sube antes, se bloquea a usuarios que todavía no pueden actualizar.
4. Solo subir `min_version_code` cuando la versión antigua sea **incompatible** (cambio de modelo de datos,
   reglas, API...). Para cambios menores no hace falta forzar.
5. Tras cada cambio en la consola: **Publicar cambios**.

### Verificación

Logcat, tag `ForceUpdate`:

```
I  fetchAndActivate ok, new values activated = true
I  raw value = '5', source = REMOTE
I  min_version_code = 5
```

| `source` | Significado |
|---|---|
| `REMOTE` | Valor descargado de la consola ✅ |
| `DEFAULT (in-app)` | La consola no envía el parámetro: revisar que esté en **Cliente**, publicado y sin condiciones que excluyan la app |
| `STATIC (not found)` | No existe ni en remoto ni como valor por defecto |

### Probar el bloqueo

1. Poner `min_version_code` mayor que el `versionCode` instalado y publicar.
2. Cerrar la app por completo y volver a abrirla → aparece la pantalla de bloqueo.
3. Restaurar el valor, publicar y reabrir.

> El flujo nativo de Google Play (In-App Updates) solo funciona con builds instalados desde Play
> (Internal app sharing / pruebas internas). En el emulador el botón abre la ficha de Play Store.

---

## Firestore: modelo de datos

Un **único documento** con un contador por imagen:

```
likes (colección)
└── locations (documento)
      ├── img001: 12
      ├── img002: 3
      ├── img017: 45
      ├── ...
      └── lastImageId: "img017"
```

| Campo | Tipo | Descripción |
|---|---|---|
| `imgXXX` | int | Número de likes de la ubicación con id `XXX` (rellenado a 3 dígitos: `1` → `img001`) |
| `lastImageId` | string | Último contador modificado. Lo necesitan las reglas para validar la escritura; la app lo ignora al leer |

- El documento y cada contador se crean solos con el primer like (`set` + `merge`).
- Un contador que no existe equivale a `0`.
- **Qué imágenes ha marcado el usuario se guarda solo en el dispositivo** (DataStore `likes`,
  clave `liked_location_ids`). Si se desinstala la app o se borran sus datos, se pierde ese estado
  (los likes ya sumados en Firestore se mantienen).

### Operaciones y coste

| Acción | Peticiones |
|---|---|
| Abrir Locations (todas las páginas) | **1 lectura** |
| Like / quitar like | **1 escritura** (`FieldValue.increment(±1)` + `lastImageId`) |

Código: `data/repository/remote/LikesRepositoryImpl.kt`, `view/location/LocationViewModel.kt`.

### Límites

- Tamaño máximo de documento: 1 MiB (miles de contadores).
- Recomendado ~1 escritura/segundo sostenida por documento. Si hubiera mucho tráfico simultáneo,
  migrar a *sharded counters*.

### Colecciones antiguas (sin uso)

`images-sce`, `images-vote` (modelo anterior like/unlike por usuario, app ≤ v5) y `users`
(login con Google, eliminado). Están cerradas en las reglas y se pueden borrar desde la consola.

---

## Firestore: reglas de seguridad

Las reglas viven en **`firestore.rules`** y se despliegan desde el repo. No editarlas en la consola
(el siguiente despliegue las sobrescribiría).

### Qué permiten

| Ruta | Lectura | Escritura |
|---|---|---|
| `likes/locations` | Cualquiera | Con sesión de Firebase (anónima incluida), validada (abajo) |
| Todo lo demás | ❌ | ❌ |

Validaciones de escritura en `likes/locations`:

- **Crear**: un único contador `imgXXX` con valor `1` + `lastImageId` apuntando a él.
- **Actualizar**: solo cambia el contador indicado en `lastImageId` (y `lastImageId`), en **+1 o −1**, valor entero.
- **Borrar**: no permitido.

### Desplegar

Requiere Firebase CLI con sesión iniciada (`firebase login`) y acceso al proyecto.

```bash
# Comprobar que compilan (no despliega nada)
firebase deploy --only firestore:rules --dry-run

# Desplegar a producción
firebase deploy --only firestore:rules
```

- El despliegue afecta **inmediatamente** a todas las versiones de la app en producción.
- Rollback: Firebase Console → Firestore → Reglas → historial de versiones, o revertir
  `firestore.rules` en git y volver a desplegar.

### Directivas al cambiar el modelo o las reglas

1. Cambiar `firestore.rules` y el código en el mismo commit.
2. Comprobar con `--dry-run` antes de desplegar.
3. Si el cambio rompe versiones antiguas de la app, publicar primero la nueva versión y
   después subir `min_version_code` en Remote Config (ver arriba).
4. Actualizar la sección **Modelo de datos** de este README.
