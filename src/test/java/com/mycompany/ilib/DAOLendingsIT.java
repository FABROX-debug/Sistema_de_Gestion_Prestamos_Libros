package com.mycompany.ilib;

import com.mycompany.models.Books;
import com.mycompany.models.Lendings;
import com.mycompany.models.Users;
import com.mycompany.utils.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DAOLendingsIT extends IntegrationTestSupport {

    private DAOLendingsImpl dao;

    @Before
    public void setUp() throws Exception {
        cleanAutomationData();
        dao = new DAOLendingsImpl();
    }

    @After
    public void tearDown() throws Exception {
        cleanAutomationData();
    }

    @Test
    public void registrarDetectarPrestamoActivoYMarcarDevolucion() throws Exception {
        Users user = createUser(AUTO_PREFIX + "Usuario_Prestamo");
        Books book = createBook(AUTO_PREFIX + "Libro_Prestamo", 2, 2);

        Lendings lending = new Lendings();
        lending.setUser_id(user.getId());
        lending.setBook_id(book.getId());
        lending.setDate_out(Utils.getFechaActual());
        dao.registrar(lending);

        Lendings active = dao.getLending(user, book);
        assertNotNull(active);
        assertTrue(active.getId() > 0);
        assertNull(active.getDate_return());

        active.setDate_return(Utils.getFechaActual());
        dao.modificar(active);

        assertNull(dao.getLending(user, book));
    }
}
