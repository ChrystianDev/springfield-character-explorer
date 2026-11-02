# Springfield Character Explorer

<!-- TODO: descripción de la app, qué contiene y cómo se usa -->

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
