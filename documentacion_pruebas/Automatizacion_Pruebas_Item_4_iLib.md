# Automatizacion de pruebas - Item 4 - iLib

## Objetivo

Se implemento una suite automatizada para validar el sistema iLib desde tres niveles:

- Pruebas unitarias para reglas y utilidades.
- Pruebas de integracion contra MySQL para DAOs.
- Pruebas automatizadas de interfaz Swing con AssertJ-Swing.

Tambien se agrego integracion CI/CD con GitHub Actions para ejecutar la suite completa y guardar evidencia de ejecucion.

## Herramientas usadas

- JUnit 4: ejecucion de pruebas unitarias e integracion.
- AssertJ-Swing: automatizacion de interfaz Java Swing.
- Maven Surefire: ejecucion de pruebas unitarias `*Test`.
- Maven Failsafe: ejecucion de pruebas de integracion e interfaz `*IT`.
- Maven Surefire Report: generacion de reporte HTML.
- GitHub Actions: pipeline CI/CD.
- MySQL 5.7: servicio de base de datos para pruebas de integracion.
- Xvfb: pantalla virtual para ejecutar pruebas Swing en Linux.

## Casos automatizados

### Pruebas unitarias

Archivos principales:

- `ValidationTest.java`
- `UtilsTest.java`

Cobertura:

- Prestamo permitido cuando hay disponibilidad.
- Prestamo rechazado sin disponibilidad.
- IDs invalidos de usuario/libro.
- Devolucion solo con prestamo existente.
- Disponibilidad aumenta al devolver.
- Sancion por devolucion fuera de plazo.
- Validacion de titulo, usuario, telefono, stock y disponibilidad.
- Utilidades de numeros y fechas.

### Pruebas de integracion DAO

Archivos principales:

- `DAOUsersIT.java`
- `DAOBooksIT.java`
- `DAOLendingsIT.java`

Cobertura:

- Registrar, listar, buscar, modificar y eliminar usuarios.
- Registrar, listar, buscar, modificar y eliminar libros.
- Registrar prestamo.
- Detectar prestamo activo.
- Marcar devolucion y verificar que ya no quede prestamo activo.

### Pruebas de interfaz

Archivo principal:

- `DashboardSwingIT.java`

Cobertura:

- Abrir `Dashboard`.
- Navegar entre Principal, Usuarios, Libros, Prestamos, Devoluciones y Reportes.
- Registrar usuario desde la interfaz.
- Registrar libro desde la interfaz.
- Realizar prestamo y devolucion desde la interfaz.
- Buscar usuarios y libros en tablas.
- Capturar screenshots automaticos ante fallos en `target/screenshots`.

## Como ejecutar localmente

Si Windows muestra el error `JAVA_HOME not found`, configurar temporalmente el JDK antes de ejecutar Maven Wrapper:

```powershell
$env:JAVA_HOME="C:\Program Files\Java\jdk-17"
```

Antes de ejecutar pruebas de integracion o interfaz, se debe tener MySQL activo con:

- Base de datos: `ilib`
- Usuario: `root`
- Password: `1234`

Importar la base:

```powershell
mysql -u root -p1234 ilib < database\ilib.sql
```

Ejecutar solo pruebas unitarias:

```powershell
.\mvnw.cmd test
```

Ejecutar suite completa:

```powershell
.\mvnw.cmd clean verify
```

Generar reporte HTML:

```powershell
.\mvnw.cmd surefire-report:report-only
```

Ejecutar con credenciales personalizadas:

```powershell
.\mvnw.cmd clean verify "-Dilib.db.url=jdbc:mysql://localhost:3306/ilib" "-Dilib.db.user=root" "-Dilib.db.password=1234"
```

## Donde encontrar evidencia

Despues de ejecutar pruebas:

- Reportes unitarios: `target/surefire-reports`
- Reportes integracion/UI: `target/failsafe-reports`
- Reporte HTML: `target/site/surefire-report.html`
- Screenshots de fallos UI: `target/screenshots`

En GitHub Actions:

1. Entrar al repositorio en GitHub.
2. Abrir la pestana `Actions`.
3. Seleccionar el workflow `Pruebas automatizadas iLib`.
4. Abrir una ejecucion.
5. Descargar el artifact `evidencia-pruebas-ilib`.

## Configuracion CI/CD

El pipeline esta en:

```text
.github/workflows/tests.yml
```

El flujo realiza:

- Checkout del repositorio.
- Configuracion de Java Temurin.
- Levantamiento de MySQL 5.7.
- Importacion de `database/ilib.sql`.
- Ejecucion de `./mvnw -B clean verify` dentro de `xvfb-run`.
- Generacion de reporte HTML.
- Publicacion de reportes y screenshots como artifact.

## Nota tecnica

iLib es una aplicacion de escritorio Java Swing. Por ese motivo, la automatizacion de interfaz se realizo con AssertJ-Swing. Selenium, Playwright y Cypress se usan principalmente para aplicaciones web, por lo que no eran la opcion mas adecuada para este proyecto.
