package com.mycompany.ilib;

import com.mycompany.models.Books;
import com.mycompany.models.Lendings;
import com.mycompany.models.Users;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Pruebas unitarias de validación académica para el curso de Pruebas de Software.
 * Se prueban las reglas de negocio de forma aislada sin conectar a la base de datos.
 */
public class ValidationTest {

    // --- 1. Pruebas de validación de préstamo ---

    @Test
    public void testPrestamoLibroConDisponibilidad() {
        // Verificar que un libro con disponibilidad mayor a 0 pueda prestarse.
        Books book = new Books();
        book.setAvailable(5);
        
        boolean canLend = book.getAvailable() > 0;
        assertTrue("El libro debería poder prestarse si la disponibilidad es mayor a 0", canLend);
    }

    @Test
    public void testPrestamoLibroSinDisponibilidad() {
        // Verificar que un libro con disponibilidad 0 no debería prestarse.
        Books book = new Books();
        book.setAvailable(0);
        
        boolean canLend = book.getAvailable() > 0;
        assertFalse("El libro NO debería poder prestarse si la disponibilidad es 0", canLend);
    }

    @Test
    public void testIdUsuarioInvalido() {
        // Verificar que no se acepte un ID de usuario vacío, negativo o no numérico.
        // Simulamos la entrada como String antes de parsearla al modelo.
        String emptyId = "";
        String negativeId = "-5";
        String nonNumericId = "abc";
        
        assertFalse("ID vacío no es válido", isValidId(emptyId));
        assertFalse("ID negativo no es válido", isValidId(negativeId));
        assertFalse("ID no numérico no es válido", isValidId(nonNumericId));
        assertTrue("ID válido", isValidId("10"));
    }

    @Test
    public void testIdLibroInvalido() {
        // Verificar que no se acepte un ID de libro vacío, negativo o no numérico.
        String emptyId = "";
        String negativeId = "-1";
        String nonNumericId = "xyz";
        
        assertFalse("ID de libro vacío no es válido", isValidId(emptyId));
        assertFalse("ID de libro negativo no es válido", isValidId(negativeId));
        assertFalse("ID de libro no numérico no es válido", isValidId(nonNumericId));
        assertTrue("ID de libro válido", isValidId("25"));
    }

    // --- 2. Pruebas de validación de devolución ---

    @Test
    public void testDevolucionRequierePrestamoActivo() {
        // Verificar que una devolución solo sea válida si existe un préstamo activo.
        Lendings prestamoActivo = new Lendings();
        prestamoActivo.setId(1); // Simula que existe
        
        Lendings prestamoInexistente = null; // Simula que no existe
        
        assertTrue("Devolución válida porque el préstamo existe", isValidReturn(prestamoActivo));
        assertFalse("Devolución inválida porque el préstamo no existe", isValidReturn(prestamoInexistente));
    }

    @Test
    public void testDevolucionAumentaDisponibilidad() {
        // Verificar que al devolver un libro se debería aumentar la disponibilidad.
        Books book = new Books();
        book.setStock(10);
        book.setAvailable(4);
        
        int disponibilidadInicial = book.getAvailable();
        // Simulamos la lógica de devolución
        book.setAvailable(book.getAvailable() + 1);
        
        assertEquals("La disponibilidad debería aumentar en 1", disponibilidadInicial + 1, book.getAvailable());
    }

    @Test
    public void testPrestamoVencidoGeneraSancion() {
        // Verificar que si el préstamo supera el plazo permitido, se debería generar sanción.
        // Asumiendo un plazo máximo de 7 días.
        LocalDate dateOut = LocalDate.now().minusDays(10); // Salió hace 10 días
        LocalDate dateReturn = LocalDate.now(); // Devuelto hoy
        
        long daysBetween = ChronoUnit.DAYS.between(dateOut, dateReturn);
        boolean hasSanction = daysBetween > 7;
        
        assertTrue("Debería generar sanción porque han pasado " + daysBetween + " días", hasSanction);
    }

    // --- 3. Pruebas de libros ---

    @Test
    public void testLibroTituloVacio() {
        // Verificar que un libro no debería registrarse con título vacío.
        Books book1 = new Books();
        book1.setTitle("");
        
        Books book2 = new Books();
        book2.setTitle("El Quijote");
        
        assertFalse("El título del libro no puede estar vacío", isValidString(book1.getTitle()));
        assertTrue("El título del libro es válido", isValidString(book2.getTitle()));
    }

    @Test
    public void testStockLibroNegativo() {
        // Verificar que el stock no debería ser negativo.
        Books book = new Books();
        book.setStock(-5);
        
        assertFalse("El stock no debería ser negativo", book.getStock() >= 0);
        
        book.setStock(10);
        assertTrue("El stock es válido", book.getStock() >= 0);
    }

    @Test
    public void testDisponibilidadNoMayorQueStock() {
        // Verificar que la disponibilidad no debería ser mayor al stock.
        Books book = new Books();
        book.setStock(5);
        book.setAvailable(6); // Inválido
        
        assertFalse("La disponibilidad no puede ser mayor al stock", book.getAvailable() <= book.getStock());
        
        book.setAvailable(5); // Válido
        assertTrue("La disponibilidad es menor o igual al stock", book.getAvailable() <= book.getStock());
    }

    // --- 4. Pruebas de usuarios ---

    @Test
    public void testUsuarioNombreVacio() {
        // Verificar que un usuario no debería registrarse con nombre vacío.
        Users user = new Users();
        user.setName("");
        
        assertFalse("El nombre del usuario no puede estar vacío", isValidString(user.getName()));
        
        user.setName("Juan");
        assertTrue("El nombre del usuario es válido", isValidString(user.getName()));
    }

    @Test
    public void testTelefonoUsuarioSinLetras() {
        // Verificar que el teléfono no debería aceptar letras.
        Users user1 = new Users();
        user1.setTel("1234567890"); // Válido
        
        Users user2 = new Users();
        user2.setTel("12345abc90"); // Inválido
        
        assertTrue("El teléfono solo debe contener números", isValidPhone(user1.getTel()));
        assertFalse("El teléfono no puede contener letras", isValidPhone(user2.getTel()));
    }

    // --- Métodos auxiliares de validación (simulan lógica de la capa de servicio) ---

    private boolean isValidId(String idStr) {
        if (idStr == null || idStr.trim().isEmpty()) {
            return false;
        }
        try {
            int id = Integer.parseInt(idStr);
            return id > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidReturn(Lendings prestamo) {
        return prestamo != null && prestamo.getId() > 0;
    }

    private boolean isValidString(String str) {
        return str != null && !str.trim().isEmpty();
    }

    private boolean isValidPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return false;
        }
        // Expresión regular que valida que el string contiene solo dígitos
        return phone.matches("^[0-9]+$");
    }
}
