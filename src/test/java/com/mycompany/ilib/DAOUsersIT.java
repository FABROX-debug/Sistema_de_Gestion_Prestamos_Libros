package com.mycompany.ilib;

import com.mycompany.models.Users;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DAOUsersIT extends IntegrationTestSupport {

    private DAOUsersImpl dao;

    @Before
    public void setUp() throws Exception {
        cleanAutomationData();
        dao = new DAOUsersImpl();
    }

    @After
    public void tearDown() throws Exception {
        cleanAutomationData();
    }

    @Test
    public void registrarListarModificarYEliminarUsuario() throws Exception {
        String originalName = AUTO_PREFIX + "Usuario_DAO";
        Users user = new Users();
        user.setName(originalName);
        user.setLast_name_p("Perez");
        user.setLast_name_m("Rojas");
        user.setDomicilio("Av. Pruebas 123");
        user.setTel("987654321");

        dao.registrar(user);

        Users saved = findUserByName(originalName);
        assertNotNull(saved);
        assertTrue(saved.getId() > 0);
        assertEquals("Perez", saved.getLast_name_p());

        saved.setName(AUTO_PREFIX + "Usuario_DAO_Editado");
        saved.setTel("900111222");
        dao.modificar(saved);

        Users updated = dao.getUserById(saved.getId());
        assertEquals(AUTO_PREFIX + "Usuario_DAO_Editado", updated.getName());
        assertEquals("900111222", updated.getTel());

        List<Users> results = dao.listar("Usuario_DAO_Editado");
        assertFalse(results.isEmpty());

        dao.eliminar(updated.getId());
        assertNull(dao.getUserById(updated.getId()));
    }
}
