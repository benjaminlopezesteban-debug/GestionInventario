# CASO: SISTEMA DE GESTIÓN DE INVENTARIO DE HERRAMIENTAS Y MATERIALES

> Documento de requerimientos preliminar (MVP). Las partes marcadas como **(propuesta)** fueron agregadas para completar el borrador y deben ser confirmadas. Al final del documento hay una lista de puntos por confirmar.

---

## 1. Resumen del caso

Desarrollar una plataforma móvil para gestionar el inventario de una bodega de materiales y herramientas de una empresa constructora.

La plataforma permitirá a los usuarios:

- Registrar solicitudes de materiales.
- Registrar solicitudes de préstamo de herramientas.
- Consultar la disponibilidad de ambos recursos (unidades, cantidad o volumen).
- Saber dónde está cada recurso: en qué lugar de la bodega (rack) o, si es una herramienta prestada, en qué obra.
- Registrarse e iniciar sesión según el rol del usuario.
- Contar con una plataforma de administración y un dashboard con métricas.

El dashboard mostrará:

- Préstamos de herramientas.
- Usuarios con más solicitudes.
- Herramientas fuera de plazo para reingreso.
- Volumen de materiales que han salido de bodega.
- Materiales solicitados sin stock.
- Obras con la cantidad de herramientas y materiales solicitados.

---

## 2. Problema actual

La empresa gestiona de forma manual los registros de materiales y herramientas. Las auditorías muestran que:

- Hay pérdida constante de herramientas y materiales en las obras a las que son asignados.
- Los registros están incompletos y en mal estado.
- Hay cambios de nombres en los registros y cambios en los datos de las solicitudes, por lo que no hay un historial confiable.
- No se sabe con claridad quién es responsable de cada recurso.

Esto retrasa la ejecución de las obras y encarece el costo del trabajo. El problema es mayor en las herramientas y en los materiales de valor, como cables de cobre, elementos de fierro y maderas.

---

## 3. Mejoras propuestas

El proyecto busca desarrollar un MVP que centralice digitalmente la gestión de herramientas y materiales, desde la solicitud hasta la entrega y devolución. El sistema permitirá:

- Tener un solo registro, completo y consistente, de todas las solicitudes.
- Seguir el recorrido de cada recurso (trazabilidad): dónde está, quién lo tiene y en qué estado está.
- Vincular cada recurso con un responsable (maestro de obra o jefe de obra).
- Controlar la cantidad y el estado de herramientas y materiales.
- Dar al administrador general una vista de todas las obras y del consumo de recursos.

---

## 4. Perfiles

El sistema trabaja con **4 perfiles**. Los perfiles son asignados fuera del sistema, no es necesario pensar en quien o como se asignan los roles, para este trabajo ya están predefinidos. 

### 01. Maestro de obra (líder de equipo)

Solicita las **herramientas** necesarias para el trabajo de su equipo. Calcula la cantidad según el tamaño del grupo.

Puede:

- Crear solicitudes de herramientas.
- Ver el estado de sus solicitudes.
- Ver las herramientas que tiene a su cargo y su fecha de devolución.

### 02. Jefe de obra

Solicita los **materiales** de la obra según el estado de avance y los requerimientos de la obra.

Puede:

- Crear solicitudes de materiales.
- Ver el estado de sus solicitudes.
- Ver los materiales entregados a su obra.

### 03. Administrador de inventario

Administra el inventario de la bodega de la empresa.

Puede:

- Registrar herramientas y materiales, y asignarles una ubicación en la bodega (rack).
- Llevar la cuenta de los recursos (stock).
- Revisar las solicitudes y aceptarlas o rechazarlas según stock, estado de los recursos y prioridad de la obra.
- Registrar la entrega y la devolución de recursos.
- Dar de baja herramientas y materiales en mal estado.
- Registrar los ingresos a la bodega y las salidas hacia las obras.
- Levantar solicitudes de compra cuando falten recursos.
- Revisar el movimiento general de la bodega.

### 04. Administrador general (ejecutivo)

Administra todo el sistema de forma general.

Puede:

- Ver un dashboard general con todas las obras en proceso, su nivel de consumo y el estado del inventario.
- Definir la prioridad de cada obra, según la información interna del contrato.
- Revisar las solicitudes de compra del administrador de inventario y realizar las compras.
- Enviar un mensaje con los recursos adquiridos.


---

## 5. Flujo principal

### 5.1 Herramientas

```
MAESTRO DE OBRA   → Crea solicitud de herramientas                    → Estado PENDIENTE
ADMIN INVENTARIO  → Revisa la solicitud (stock, estado, prioridad)
                  → Acepta                                            → Estado APROBADA
                  → o rechaza (sin stock, queda hasta reposición)     → Estado SIN_STOCK / RECHAZADA
                  → Entrega la herramienta y registra los datos
                    (solicitante, obra, número, tipo, ubicación)      → Estado ENTREGADA
                                                                        Herramienta: PRESTADA
MAESTRO DE OBRA   → Usa la herramienta en la obra
                  → Devuelve la herramienta
ADMIN INVENTARIO  → Registra la devolución y revisa su estado         → Estado DEVUELTA
                                                                        Herramienta: DISPONIBLE o MAL_ESTADO
```

Si pasa la fecha de devolución y la herramienta no ha vuelto, la solicitud pasa a **FUERA_DE_PLAZO** y aparece en el dashboard.

### 5.2 Materiales

El proceso es similar al de herramientas, pero lo realiza el jefe de obra y los materiales no se devuelven:

```
JEFE DE OBRA      → Crea solicitud de materiales                      → Estado PENDIENTE
ADMIN INVENTARIO  → Revisa la solicitud (cantidad, stock, prioridad)
                  → Acepta                                            → Estado APROBADA
                  → o rechaza (sin stock, queda hasta reposición)     → Estado SIN_STOCK / RECHAZADA
                  → Entrega el material y registra los datos
                    (solicitante, obra, cantidad o volumen)           → Estado ENTREGADA
                                                                        Stock del material: baja
```

### 5.3 Compras

```
ADMIN INVENTARIO  → Detecta falta de recursos o solicitudes sin stock
                  → Crea solicitud de compra                          → Estado COMPRA_PENDIENTE
ADMIN GENERAL     → Recibe la notificación en la aplicación
                  → Acepta la compra
                  → Envía un mensaje con los recursos adquiridos      → Estado COMPRA_REALIZADA
                  → No aprueba envía un mensaje que notifica el rechazo de la compra Estado COMPRA_RECHAZADA
ADMIN INVENTARIO  → Registra el ingreso a bodega y asigna ubicación
                  → Actualiza el stock
                  → Las solicitudes que estaban SIN_STOCK pueden volver a revisarse
```


### 5.4 Bajas de recursos

```
ADMIN INVENTARIO  → Identifica herramienta o material en mal estado
                  → Cambia su estado a MAL_ESTADO
                  → Si no se puede usar más, lo da de baja            → Estado DADO_DE_BAJA
```

---

## 6. Estados del sistema

El borrador indica que hay estados para las solicitudes y para el estado funcional de herramientas y materiales. Se proponen los siguientes: 
 
 EstadoObra: EnProceso, Finalizada. 

 PrioridadObra: Baja, Media, Alta. 

 SolicitudeMateriales: Prendiente, Aprobada, SinStock, Rechazada, Entregada. 

 SolicitudHerramientas: Pendiente, Aprobada, SinStrock, Entregada, Devuelte, Fuera de plazo.

 SolicitudComprada: CompraPendiente, CompraRealizada, 

 Herramientas: Disponible, Prestada, MalEstado, DadaDeBaja.

 Materiales: BuenEstado, MalEstado, DadoDeBaja. 

### Estados de la solicitud (herramientas y materiales)

| Estado | Significado |
|---|---|
| PENDIENTE | La solicitud fue creada y espera revisión. |
| APROBADA | El administrador de inventario aceptó la solicitud. |
| SIN_STOCK | No hay stock; la solicitud queda en espera hasta la reposición. |
| RECHAZADA | La solicitud fue rechazada (se indica el motivo). |
| ENTREGADA | El recurso salió de bodega hacia la obra. |
| DEVUELTA | La herramienta volvió a bodega (solo herramientas). |
| FUERA_DE_PLAZO | Pasó la fecha de devolución y la herramienta no ha vuelto (solo herramientas). |

### Estado funcional de la herramienta

| Estado | Significado |
|---|---|
| DISPONIBLE | Está en bodega y se puede prestar. |
| PRESTADA | Está en una obra, a cargo de un responsable. |
| MAL_ESTADO | Está dañada y no se puede prestar. |
| DADA_DE_BAJA | Ya no forma parte del inventario. |

### Estado funcional del material

| Estado | Significado |
|---|---|
| BUEN_ESTADO | Se puede entregar. |
| MAL_ESTADO | No se puede usar. |
| DADO_DE_BAJA | Se retiró del inventario. |

### Estados de la solicitud de compra

| Estado | Significado |
|---|---|
| COMPRA_PENDIENTE | El administrador de inventario la creó y espera respuesta. |
| COMPRA_REALIZADA | El administrador general aceptó y compró. |
| COMPRA_RECHAZADA | El administrador general no aprobó la compra. (ojito)| 

### Prioridad de la obra

El administrador general asigna a cada obra una prioridad: **ALTA, MEDIA o BAJA**. El administrador de inventario la usa al decidir qué solicitudes atender primero cuando hay poco stock.

---

## 7. Funcionalidades principales del sistema

### 7.1 Autenticación y usuarios

- Registro de usuario.
- Login.
- Recuperación de contraseña.
- Almacenamiento seguro de contraseña.
- Autorización según rol: cada perfil solo ve y usa lo que le corresponde.
- Endpoints protegidos.

### 7.2 Gestión de solicitudes (maestro de obra y jefe de obra)

**Maestro de obra** (herramientas):

- Crear solicitud indicando: obra, tipo de herramienta, cantidad, fecha en que la necesita y fecha de devolución estimada.
- Consultar sus solicitudes y su estado.
- Consultar las herramientas que tiene prestadas.

**Jefe de obra** (materiales):

- Crear solicitud indicando: obra, material, cantidad o volumen, fecha en que lo necesita y fecha de devolución.
- Consultar sus solicitudes y su estado.
- Consultar los materiales que se han entregado a su obra.

Una solicitud ya creada no se puede cambiar libremente. Los cambios de estado quedan registrados con fecha y usuario, para evitar cambios de nombres o de datos sin respaldo.

### 7.3 Gestión de inventario (administrador de inventario)

- Registrar herramientas y materiales (nombre, tipo, unidad de medida, cantidad o volumen, estado).
- Asignar y modificar la ubicación en bodega (rack).
- Ver la disponibilidad (stock) de cada recurso y asignar disponibilidad cuando se repone el stock.
- Revisar solicitudes y aceptarlas o rechazarlas.
- Registrar entregas y devoluciones.
- Registrar ingresos a bodega y salidas hacia las obras.
- Dar de baja recursos en mal estado.
- Crear solicitudes de compra.
- Ve las herramientas fuera de plazo.

### 7.4 Monitoreo central (administrador general)

- Dashboard general (ver sección 7.5).
- Ver todas las obras en proceso, su consumo y el estado del inventario.
- Asignar la prioridad de cada obra.
- Recibir solicitudes de compra, aceptarlas y enviar el mensaje con los recursos adquiridos.
- Asignar roles a los usuarios **(propuesta)**. Para mi inescesaria, podriamos quitarla, haciendolo menos complejo.... benja:)

### 7.5 Dashboard con métricas

| Métrica | Qué muestra |
|---|---|
| Préstamo de herramientas | Cantidad de herramientas prestadas y por obra. |
| Herramientas fuera de plazo | Herramientas que debían devolverse y no han vuelto. |
| Volumen de materiales salidos | Cantidad o volumen de materiales que salieron de bodega. |
| Materiales solicitados sin stock | Materiales pedidos que no se pudieron entregar. |
| Obras con herramientas y materiales | Cantidad de herramientas y materiales solicitados por cada obra. |

### 7.6 Notificaciones

- Al administrador de inventario cuando llega una solicitud nueva.
- Al maestro de obra o jefe de obra cuando su solicitud cambia de estado.
- Al administrador general cuando hay una solicitud de compra.
- Al administrador de inventario cuando se realiza una compra.

Las notificaciones son mensajes dentro de la aplicación. El envío por correo se puede agregar más adelante.

---

## 8. Integración con lector de código de barras / QR

Cada herramienta y material tendrá un código de identificación (código de barras o QR). El usuario puede escanearlo con el celular para trabajar más rápido y con menos errores.

El escaneo se usa para:

- **Ingresar** un recurso nuevo a la bodega.
- **Solicitar** un recurso.
- **Recepcionar** una herramienta o material.
- **Cambiar el estado** de un recurso (por ejemplo, de PRESTADA a DISPONIBLE al devolverla).

Al escanear un código, el sistema muestra los datos del recurso: nombre, tipo, estado, ubicación y responsable actual.

> La tecnología específica del lector se define más adelante.

---

## 9. Datos principales del sistema (propuesta)

Lista simple de la información que el sistema debe guardar. Sirve como base para el modelo entidad-relación.

| Dato | Información principal |
|---|---|
| Usuario | nombre, correo, contraseña (cifrada), rol |
| Obra | nombre, ubicación, prioridad, estado |
| Herramienta | nombre, tipo, código QR, estado, ubicación (rack u obra) |
| Material | nombre, tipo, cantidad o volumen disponible, código QR, estado, ubicación (rack) |
| Ubicación en bodega (rack) | código del rack, descripción |
| Solicitud de herramienta | maestro de obra, obra, tipo y cantidad, fecha de solicitud, fecha de devolución estimada, estado |
| Solicitud de material | jefe de obra, obra, material y cantidad, fecha de solicitud, estado |
| Movimiento de bodega | tipo (ingreso o salida), recurso, cantidad, fecha, usuario |
| Solicitud de compra | recurso, cantidad, solicitante, estado, fecha, aprueba|

---

## 10. Arquitectura tecnológica exigida

| Capa | Tecnología |
|---|---|
| Plataforma | Android |
| Lenguaje | Kotlin |
| IDE | Android Studio |
| UI | Jetpack Compose |
| Diseño | Material Design 3 |
| Arquitectura app | MVVM |
| Persistencia local | Room / SQLite |
| Backend | Spring Boot (puede ser otra tecnología) |
| Persistencia backend | JPA |
| Seguridad | Spring Security + JWT + BCrypt |
| Comunicación | API REST |
| Cliente HTTP | Retrofit |
| Pruebas | Pruebas unitarias, Postman para la API |
| Repositorio | Git / GitHub |
| Gestión | Trello / Jira (puede ser otra) |
| Entregable | APK firmado |

---

## 11. Flujo funcional propuesto para el MVP

```
Inicio de sesión (JWT)
        ↓
Menú según rol
        ↓
Maestro de obra: crear solicitud de herramientas   |  Jefe de obra: crear solicitud de materiales
        ↓
POST API REST → Estado PENDIENTE
        ↓
Administrador de inventario: listado de solicitudes pendientes
        ↓
Revisar stock, estado y prioridad de la obra
        ↓
Aprobar → Estado APROBADA   |   Sin stock → SIN_STOCK   |   Rechazar → RECHAZADA
        ↓
Entregar recurso (escanear QR) → Estado ENTREGADA
        ↓
Herramientas: devolución (escanear QR) → Estado DEVUELTA
        ↓
Si falta stock: solicitud de compra → Administrador general → compra realizada
        ↓
Administrador general: dashboard con las métricas
```

En paralelo:

- Solicitudes e inventario → API REST.
- Sesión / token → Room.

---

## 12. Consideraciones importantes

| Tema | Detalle |
|---|---|
| Autenticación | Incluye registro, login, recuperación de contraseña y contraseña guardada de forma segura. Cada usuario ve solo lo que le corresponde según su rol. |
| Actualización de datos | No se exige tiempo real. Basta con actualizar la información al entrar o refrescar la pantalla (llamadas REST). |
| Ubicación de los recursos | La ubicación se registra como texto: rack en la bodega u obra donde está la herramienta. No se usa GPS ni mapas. |
| Identificación de recursos | Se usa código de barras / QR para agilizar el trabajo. Ver sección 8. |
| Trazabilidad | Cada cambio de estado de una solicitud o recurso queda registrado con fecha y usuario. |
| Alcance | Es un MVP. Las funciones más complejas se agregan en etapas posteriores. |

---

## 13. Restricciones importantes

- La aplicación debe funcionar con **datos ficticios**, que cada equipo debe crear.
- Cada equipo debe crear todo: base de datos, backend y aplicación. No se entregan datos ni backend de ejemplo.
- Se usan solo las tecnologías indicadas en la sección 10.

---

## 14. Cronograma

Cronograma estimado, basado en las semanas del curso (28 de septiembre al 29 de noviembre). Las semanas son una propuesta y se pueden ajustar.

| N° | Actividad | Semanas |
|---|---|---|
| 1 | Análisis del caso / requerimientos | 28 Sep – 11 Oct | Listo |
| 2 | Historias de usuario / casos de uso | 5 – 11 Oct | 
| 3 | Modelo Entidad-Relación | 5 – 18 Oct |
| 4 | Modelo relacional / scripts BD | 12 – 18 Oct |
| 5 | Estructura Spring Boot | 12 – 18 Oct |
| 6 | Entidades / Repository / Service | 12 – 25 Oct |
| 7 | API REST de inventario y solicitudes | 19 Oct – 1 Nov |
| 8 | Spring Security + JWT | 26 Oct – 1 Nov |
| 9 | Roles y autorización | 26 Oct – 8 Nov |
| 10 | Pruebas API / Postman | 26 Oct – 8 Nov |
| 11 | Diseño UI/UX Android | 12 – 25 Oct |
| 12 | Compose + MVVM | 19 Oct – 1 Nov |
| 13 | Login Android + JWT | 26 Oct – 8 Nov |
| 14 | Retrofit / API REST | 26 Oct – 8 Nov |
| 15 | Gestión de solicitudes y dashboard | 2 – 15 Nov |
| 16 | Persistencia Room | 2 – 15 Nov |
| 17 | Flujo de estados | 2 – 15 Nov |
| 18 | Lector de código de barras / QR | 9 – 15 Nov |
| 19 | Integración completa | 9 – 22 Nov |
| 20 | Manejo de errores / validaciones | 9 – 22 Nov |
| 21 | Pruebas | 16 – 22 Nov |
| 22 | Documentación | 16 – 29 Nov |
| 23 | Correcciones finales | 16 – 29 Nov |
| 24 | APK firmado | 23 – 29 Nov |
| 25 | Presentación final | 23 – 29 Nov |

---

## 15. Alcance funcional mínimo

| Área | Contenido |
|---|---|
| Base de datos | MER + modelo relacional + implementación. |
| Backend | Spring Boot + JPA + API REST + Spring Security + JWT + roles. |
| Android | Kotlin + Compose + Material Design 3 + MVVM + Retrofit + Room. |
| Funcional | Autenticación, creación de solicitudes de herramientas y materiales, revisión y aprobación por el administrador de inventario, entrega y devolución, estados de solicitud y de recursos, control de stock y ubicación, solicitud de compra, dashboard con métricas y lector de código de barras / QR. |
| Ingeniería | Git/GitHub, validaciones, manejo de errores, pruebas, documentación y APK firmado. |

---



**Anotaciones**
Va con persistencia de datos local, no incluye backend y api rest. Esto limmita y simplifica.

Con respecto a los datos: Material agrega volumen_disponible, para precisar a las definciones de cantidad_disponible y unidad_medida.... Ineficiente prefiero unidad + cantidad (generalizar en esos dos conceptos)

Tabla historial de estado, para la trazabilidad. 

No ocupare BCrypt no entrara en la pauta de evaluación .

Sin recuperación de constraseña del MVP 



