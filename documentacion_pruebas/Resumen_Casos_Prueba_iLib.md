# Resumen de Casos de Prueba de iLib

## 1. Cantidad de casos por modulo
- Usuarios: 6 casos
- Libros: 8 casos
- Prestamos: 9 casos
- Devoluciones: 8 casos
- Reportes: 4 casos
- Total general: 35 casos

## 2. Cantidad de casos por tecnica
- Caja negra: 10 casos
- Particion de equivalencia: 10 casos
- Valores limite: 4 casos
- Tabla de decision: 5 casos
- Transicion de estados: 2 casos
- Caja blanca: 2 casos
- Pruebas basadas en riesgo: 2 casos

## 3. Cantidad de casos por prioridad
- Alta: 8 casos
- Media: 26 casos
- Baja: 1 caso

## 4. Lista de casos criticos recomendados para demo
- CP-PR-001: prestamo valido con registro de fecha de salida y reduccion de disponibilidad.
- CP-PR-007: intento de prestamo con libro sin disponibilidad.
- CP-PR-008: bloqueo de prestamo duplicado del mismo libro al mismo usuario.
- CP-DV-001: devolucion valida con fecha de entrega y aumento de disponibilidad.
- CP-DV-007: intento de devolucion sin prestamo activo.
- CP-DV-008: devolucion tardia con sancion.
- CP-RP-001: carga del reporte de prestamos al abrir la vista.

## 5. Riesgos principales cubiertos por la matriz
- Registro de datos incompletos en usuarios, libros, prestamos y devoluciones.
- Aceptacion actual de telefonos con letras y de algunos valores de inventario inconsistentes.
- Prestamos sobre libros sin disponibilidad.
- Prestamos duplicados del mismo libro al mismo usuario.
- Devoluciones sin prestamo activo.
- Calculo de sanciones por devolucion tardia.
- Dependencia de la logica de aplicacion para mantener consistencia entre usuarios, libros y prestamos.
