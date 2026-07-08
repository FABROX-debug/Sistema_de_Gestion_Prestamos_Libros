# Item 3 - Disenio de Casos de Prueba de iLib

## 1. Introduccion del disenio de casos de prueba
El presente diseno de casos de prueba fue elaborado tomando como referencia exclusiva el comportamiento real implementado en el proyecto iLib. Para esta version del documento no se incorporan reglas ideales ni validaciones esperadas por criterio externo, sino unicamente aquellas funcionalidades y respuestas que actualmente se observan en el codigo fuente del sistema.

La finalidad de este enfoque es que la matriz represente con fidelidad lo que hoy hace la aplicacion, de modo que el Item 3 del informe pueda usarse como una base de prueba alineada con la implementacion real del programa.

## 2. Modulos evaluados
El analisis se realizo sobre los modulos funcionales visibles y sus clases asociadas:

- Usuarios: [Users.java](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/Users.java), [UpUsers.java](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/UpUsers.java), [DAOUsersImpl.java](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/ilib/DAOUsersImpl.java).
- Libros: [Books.java](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/Books.java), [UpBooks.java](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/UpBooks.java), [DAOBooksImpl.java](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/ilib/DAOBooksImpl.java).
- Prestamos: [Lendings.java](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/Lendings.java), [DAOLendingsImpl.java](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/ilib/DAOLendingsImpl.java).
- Devoluciones: [Returns.java](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/Returns.java).
- Reportes: [Reports.java](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/Reports.java).
- Utilidades de soporte: [Utils.java](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/utils/Utils.java).

## 3. Aplicacion de caja negra en iLib
La tecnica de caja negra se aplico en aquellas funciones donde el comportamiento puede verificarse desde la interfaz y sus resultados observables. En iLib esto ocurre, por ejemplo, en el registro de usuarios y libros cuando todos los campos estan completos, en los mensajes emitidos ante campos vacios, en la carga de datos de tablas y en la visualizacion del reporte de prestamos.

Tambien se utilizo caja negra para reflejar comportamientos actuales que el sistema acepta tal como esta implementado, por ejemplo el registro de usuarios con telefono alfanumerico o el registro de libros con relaciones de stock y disponibilidad que no son contrastadas entre si.

## 4. Aplicacion de caja blanca en iLib
La tecnica de caja blanca se uso cuando el codigo muestra rutas internas claramente diferenciadas que solo pueden justificarse revisando la implementacion. Dos ejemplos importantes son:

- El bloqueo de prestamos duplicados mediante la consulta `getLending(user, book)` en [DAOLendingsImpl.java](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/ilib/DAOLendingsImpl.java).
- La aplicacion de sanciones por devolucion tardia en [Returns.java](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/Returns.java), donde se usa `MAX_DAYS_RETURN = 5` y `COST_DAY_SANC = 10`.

En estos casos, el diseno de prueba se apoya directamente en las ramas y condiciones internas del programa.

## 5. Aplicacion de pruebas basadas en riesgo
Las pruebas basadas en riesgo se concentraron en las operaciones que impactan inventario, prestamos activos y trazabilidad de movimientos. Bajo el comportamiento real del sistema, las areas con mayor sensibilidad operativa son:

- Prestamo de libros sin disponibilidad.
- Prestamo duplicado del mismo libro al mismo usuario.
- Devolucion sin prestamo activo.
- Devolucion tardia con generacion de sancion.
- Reporte de prestamos como vista consolidada de movimientos.

Estas rutas fueron priorizadas porque un error en ellas afecta directamente el control de la biblioteca.

## 6. Justificacion de prioridad para prestamos y devoluciones
Prestamos y devoluciones se consideran de prioridad alta porque involucran mas de una actualizacion en la logica del sistema. En prestamos, el programa registra el movimiento y luego descuenta una unidad de `available` del libro. En devoluciones, el programa registra `date_return`, incrementa `available` y, si corresponde, actualiza `sanctions` y `sanc_money` del usuario.

Debido a que estas operaciones modifican simultaneamente varias entidades, su correcto funcionamiento resulta mas critico que las operaciones de mantenimiento simple de usuarios o libros.

## 7. Relacion entre casos de prueba y calidad del sistema
Los casos de prueba construidos permiten documentar de manera verificable el estado actual del sistema. Esto aporta valor porque:

- Hace visible que validaciones existen realmente y cuales no.
- Permite comprobar si los mensajes y flujos de interfaz corresponden a la implementacion actual.
- Facilita distinguir entre comportamiento correcto segun codigo y oportunidades de mejora futuras.
- Genera una base objetiva para ejecucion manual o para futuras pruebas automatizadas.

En consecuencia, la calidad del sistema puede evaluarse no solo por lo que deberia hacer, sino por lo que efectivamente hace en su version actual.

## 8. Observaciones tecnicas encontradas durante el analisis
Las siguientes observaciones estan sustentadas en el codigo revisado y se expresan como descripcion del comportamiento actual:

- En [UpUsers.java](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/UpUsers.java:217) el telefono se valida solo como campo no vacio. Por ello, una cadena con letras puede ser registrada si los demas campos estan completos.
- En [UpBooks.java](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/UpBooks.java:321) `stock` y `available` se validan unicamente con `Utils.isNumeric()`. Como dicho metodo usa `Integer.parseInt`, acepta enteros negativos.
- En [UpBooks.java](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/UpBooks.java) no existe una comparacion entre `available` y `stock`, por lo que el sistema puede guardar libros con disponibles mayores al stock.
- En [DAOUsersImpl.java](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/ilib/DAOUsersImpl.java:72) y [DAOBooksImpl.java](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/ilib/DAOBooksImpl.java:84) las busquedas se construyen concatenando texto en la consulta SQL `LIKE`.
- En [ilib.sql](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/database/ilib.sql) no se definen claves foraneas entre `lendings`, `users` y `books`; por tanto, la relacion entre estas tablas depende de las validaciones de la aplicacion.
- En [Returns.java](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/Returns.java:222) el mensaje del bloque `catch` dice `Ocurrio un error al prestar el libro`, aunque se encuentre dentro del modulo de devoluciones.

Como observacion adicional, la fecha de salida y la fecha de devolucion se almacenan como texto en formato `dd-MM-yyyy` a partir de [Utils.java](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/utils/Utils.java), lo cual influye directamente en el diseno de pruebas sobre reportes y sanciones.

## 9. Sustento exacto de casos de prueba en el codigo
A continuacion se documentan 10 casos de la matriz con el punto exacto del codigo que los respalda.

### CP-US-002 - Intentar registrar usuario con al menos un campo vacio
Sustento:
- [UpUsers.java:217](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/UpUsers.java:217) valida `nombre`, `apP`, `apM`, `dom` y `tel` con `isEmpty()`.
- [UpUsers.java:218](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/UpUsers.java:218) muestra el mensaje `Debe llenar todos los campos.`.
- [UpUsers.java:220](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/UpUsers.java:220) corta el flujo con `return`.

### CP-US-003 - Registrar usuario con telefono que contiene letras
Sustento:
- [UpUsers.java:217](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/UpUsers.java:217) solo comprueba vacios; no existe validacion numerica para `tel`.
- [UpUsers.java:228](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/UpUsers.java:228) asigna el valor de `tel` al modelo sin transformaciones.
- [DAOUsersImpl.java:17](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/ilib/DAOUsersImpl.java:17) y [DAOUsersImpl.java:22](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/ilib/DAOUsersImpl.java:22) insertan el telefono tal como llega.
Error o riesgo detectado:
- El sistema permite telefonos con letras porque no hay control de formato en la vista ni en el DAO.

### CP-US-006 - Intentar eliminar o editar usuario sin seleccionar fila
Sustento:
- [Users.java:216](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/Users.java:216) verifica si no hay filas seleccionadas para borrar.
- [Users.java:217](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/Users.java:217) muestra el mensaje para seleccionar usuarios antes de eliminar.
- [Users.java:231](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/Users.java:231) valida si hay fila seleccionada para editar.
- [Users.java:240](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/Users.java:240) muestra el mensaje para seleccionar el usuario a editar.

### CP-LB-003 - Intentar registrar libro con stock no numerico
Sustento:
- [UpBooks.java:321](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/UpBooks.java:321) ejecuta `!Utils.isNumeric(stock) || !Utils.isNumeric(disp)`.
- [UpBooks.java:322](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/UpBooks.java:322) muestra el mensaje de que Stock y Disponibles deben ser numeros enteros.
- [Utils.java:10](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/utils/Utils.java:10) a [Utils.java:19](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/utils/Utils.java:19) implementan la validacion numerica real.

### CP-LB-005 - Registrar libro con stock negativo
Sustento:
- [Utils.java:15](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/utils/Utils.java:15) usa `Integer.parseInt(strNum)`, lo que acepta `-1` como entero valido.
- [UpBooks.java:321](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/UpBooks.java:321) solo valida que `stock` y `disp` sean numericos.
- [UpBooks.java:337](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/UpBooks.java:337) convierte `stock` a entero.
- [DAOBooksImpl.java:17](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/ilib/DAOBooksImpl.java:17) y [DAOBooksImpl.java:27](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/ilib/DAOBooksImpl.java:27) persisten el valor.
Error o riesgo detectado:
- No hay validacion que bloquee enteros negativos para `stock`.

### CP-LB-006 - Registrar libro con disponibles mayores que stock
Sustento:
- [UpBooks.java:327](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/UpBooks.java:327) crea o reutiliza el modelo del libro.
- [UpBooks.java:337](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/UpBooks.java:337) asigna `stock`.
- [UpBooks.java:338](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/UpBooks.java:338) asigna `available`.
- [UpBooks.java:316](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/UpBooks.java:316) a [UpBooks.java:325](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/UpBooks.java:325) no incluyen comparacion entre ambos campos.
Error o riesgo detectado:
- El programa puede guardar inventarios inconsistentes porque no compara `available` contra `stock`.

### CP-PR-001 - Registrar prestamo valido con usuario existente y libro disponible
Sustento:
- [Lendings.java:158](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/Lendings.java:158) obtiene el usuario por folio.
- [Lendings.java:168](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/Lendings.java:168) obtiene el libro por ID.
- [Lendings.java:195](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/Lendings.java:195) asigna `date_out` con `Utils.getFechaActual()`.
- [DAOLendingsImpl.java:19](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/ilib/DAOLendingsImpl.java:19) inserta `user_id`, `book_id` y `date_out`.
- [Lendings.java:199](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/Lendings.java:199) reduce `available` en 1.
- [Lendings.java:200](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/Lendings.java:200) actualiza el libro.

### CP-PR-008 - Intentar prestamo duplicado del mismo libro al mismo usuario
Sustento:
- [Lendings.java:184](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/Lendings.java:184) consulta `getLending(currentUser, currentBook)`.
- [DAOLendingsImpl.java:57](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/ilib/DAOLendingsImpl.java:57) busca prestamos activos con `date_return IS NULL`.
- [Lendings.java:185](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/Lendings.java:185) entra al control si ya existe un prestamo activo.
- [Lendings.java:186](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/Lendings.java:186) muestra el mensaje de duplicidad.

### CP-DV-001 - Registrar devolucion valida de un prestamo activo
Sustento:
- [Returns.java:190](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/Returns.java:190) consulta el prestamo activo con `getLending`.
- [Returns.java:198](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/Returns.java:198) asigna `date_return` con `Utils.getFechaActual()`.
- [DAOLendingsImpl.java:36](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/ilib/DAOLendingsImpl.java:36) actualiza `date_return`.
- [Returns.java:202](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/Returns.java:202) incrementa `available` en 1.
- [Returns.java:203](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/Returns.java:203) guarda el libro actualizado.

### CP-DV-008 - Registrar devolucion tardia con sancion
Sustento:
- [Returns.java:15](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/Returns.java:15) define `MAX_DAYS_RETURN = 5`.
- [Returns.java:16](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/Returns.java:16) define `COST_DAY_SANC = 10`.
- [Returns.java:210](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/Returns.java:210) calcula la diferencia de dias.
- [Returns.java:211](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/Returns.java:211) aplica la condicion de retraso mayor a 5 dias.
- [Returns.java:212](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/Returns.java:212) y [Returns.java:213](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/Returns.java:213) calculan dias de retraso y monto de sancion.
- [Returns.java:216](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/Returns.java:216) a [Returns.java:218](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/Returns.java:218) actualizan `sanctions` y `sanc_money`.

### CP-RP-001 - Cargar reporte de prestamos al abrir la vista
Sustento:
- [Reports.java:10](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/Reports.java:10) a [Reports.java:13](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/Reports.java:13) muestran que el constructor invoca `LoadLendings()`.
- [Reports.java:26](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/Reports.java:26) define el metodo de carga.
- [Reports.java:31](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/Reports.java:31) limpia la tabla.
- [Reports.java:32](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/Reports.java:32) agrega a la tabla `user_id`, `book_id`, `date_out` y `date_return`.
- [Reports.java:72](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/Reports.java:72) a [Reports.java:74](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/Reports.java:74) definen las columnas visibles del reporte.

## 10. Errores o defectos confirmados en el codigo
Durante la revision se confirmaron defectos o inconsistencias que conviene mencionar explicitamente en el informe:

- Error funcional de validacion: el telefono del usuario acepta letras porque [UpUsers.java:217](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/UpUsers.java:217) solo verifica vacios.
- Error funcional de inventario: se aceptan valores negativos en `stock` y `available` porque [Utils.java:15](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/utils/Utils.java:15) acepta enteros negativos y [UpBooks.java:321](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/UpBooks.java:321) no agrega otra restriccion.
- Error funcional de consistencia: se aceptan libros con `available` mayor que `stock` porque no existe comparacion entre ambos campos en [UpBooks.java:316](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/UpBooks.java:316) a [UpBooks.java:338](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/UpBooks.java:338).
- Error de mensaje: el modulo de devoluciones muestra `Ocurrió un error al prestar el libro` en [Returns.java:222](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/views/Returns.java:222), aunque la accion real es devolver.
- Riesgo de seguridad basico: las busquedas concatenan texto en SQL en [DAOUsersImpl.java:72](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/ilib/DAOUsersImpl.java:72) y [DAOBooksImpl.java:84](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/src/main/java/com/mycompany/ilib/DAOBooksImpl.java:84).
- Riesgo de integridad de datos: la base definida en [ilib.sql:52](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/database/ilib.sql:52) a [ilib.sql:58](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/database/ilib.sql:58) y [ilib.sql:66](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/database/ilib.sql:66) a [ilib.sql:75](C:/Users/User/Desktop/Prestamo%20de%20libros/iLib/database/ilib.sql:75) no declara claves foraneas entre `lendings`, `users` y `books`.
