package com.mycompany.ilib;

import com.mycompany.models.Books;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DAOBooksIT extends IntegrationTestSupport {

    private DAOBooksImpl dao;

    @Before
    public void setUp() throws Exception {
        cleanAutomationData();
        dao = new DAOBooksImpl();
    }

    @After
    public void tearDown() throws Exception {
        cleanAutomationData();
    }

    @Test
    public void registrarListarModificarYEliminarLibro() throws Exception {
        String originalTitle = AUTO_PREFIX + "Libro_DAO";
        Books book = new Books();
        book.setTitle(originalTitle);
        book.setDate("2026");
        book.setAuthor("Autor DAO");
        book.setCategory("Automatizacion");
        book.setEdit("Primera");
        book.setLang("ES");
        book.setPages("250");
        book.setDescription("Libro de prueba DAO");
        book.setEjemplares("L-001");
        book.setStock(5);
        book.setAvailable(5);

        dao.registrar(book);

        Books saved = findBookByTitle(originalTitle);
        assertNotNull(saved);
        assertTrue(saved.getId() > 0);
        assertEquals(5, saved.getAvailable());

        saved.setTitle(AUTO_PREFIX + "Libro_DAO_Editado");
        saved.setAvailable(4);
        dao.modificar(saved);

        Books updated = dao.getBookById(saved.getId());
        assertEquals(AUTO_PREFIX + "Libro_DAO_Editado", updated.getTitle());
        assertEquals(4, updated.getAvailable());

        List<Books> results = dao.listar("Libro_DAO_Editado");
        assertFalse(results.isEmpty());

        dao.eliminar(updated.getId());
        assertNull(dao.getBookById(updated.getId()));
    }
}
