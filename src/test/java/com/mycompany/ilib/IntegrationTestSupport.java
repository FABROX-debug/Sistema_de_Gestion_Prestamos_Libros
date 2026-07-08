package com.mycompany.ilib;

import com.mycompany.models.Books;
import com.mycompany.models.Users;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

abstract class IntegrationTestSupport {

    protected static final String AUTO_PREFIX = "AUTO_";
    protected static final String UI_PREFIX = "UI_";

    protected static Connection openConnection() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection(
                getConfig("ilib.db.url", "ILIB_DB_URL", "jdbc:mysql://localhost:3306/ilib"),
                getConfig("ilib.db.user", "ILIB_DB_USER", "root"),
                getConfig("ilib.db.password", "ILIB_DB_PASSWORD", "1234"));
    }

    protected static void cleanAutomationData() throws Exception {
        try (Connection connection = openConnection()) {
            execute(connection, "DELETE FROM lendings WHERE user_id IN (SELECT id FROM users WHERE name LIKE ? OR name LIKE ?)",
                    AUTO_PREFIX + "%", UI_PREFIX + "%");
            execute(connection, "DELETE FROM lendings WHERE book_id IN (SELECT id FROM books WHERE title LIKE ? OR title LIKE ?)",
                    AUTO_PREFIX + "%", UI_PREFIX + "%");
            execute(connection, "DELETE FROM users WHERE name LIKE ? OR name LIKE ?", AUTO_PREFIX + "%", UI_PREFIX + "%");
            execute(connection, "DELETE FROM books WHERE title LIKE ? OR title LIKE ?", AUTO_PREFIX + "%", UI_PREFIX + "%");
        }
    }

    protected static Users createUser(String name) throws Exception {
        Users user = new Users();
        user.setName(name);
        user.setLast_name_p("Paterno");
        user.setLast_name_m("Materno");
        user.setDomicilio("Direccion automatizada");
        user.setTel("999888777");

        DAOUsersImpl dao = new DAOUsersImpl();
        dao.registrar(user);
        return findUserByName(name);
    }

    protected static Books createBook(String title, int stock, int available) throws Exception {
        Books book = new Books();
        book.setTitle(title);
        book.setDate("2026");
        book.setAuthor("Autor automatizado");
        book.setCategory("Pruebas");
        book.setEdit("Primera");
        book.setLang("ES");
        book.setPages("120");
        book.setDescription("Libro creado por pruebas automatizadas");
        book.setEjemplares("A-001");
        book.setStock(stock);
        book.setAvailable(available);

        DAOBooksImpl dao = new DAOBooksImpl();
        dao.registrar(book);
        return findBookByTitle(title);
    }

    protected static Users findUserByName(String name) throws Exception {
        DAOUsersImpl dao = new DAOUsersImpl();
        List<Users> users = dao.listar(name);
        for (Users user : users) {
            if (name.equals(user.getName())) {
                return user;
            }
        }
        return null;
    }

    protected static Books findBookByTitle(String title) throws Exception {
        DAOBooksImpl dao = new DAOBooksImpl();
        List<Books> books = dao.listar(title);
        for (Books book : books) {
            if (title.equals(book.getTitle())) {
                return book;
            }
        }
        return null;
    }

    private static void execute(Connection connection, String sql, String firstPattern, String secondPattern) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, firstPattern);
            statement.setString(2, secondPattern);
            statement.executeUpdate();
        }
    }

    private static String getConfig(String propertyName, String envName, String defaultValue) {
        String propertyValue = System.getProperty(propertyName);
        if (propertyValue != null && !propertyValue.trim().isEmpty()) {
            return propertyValue;
        }

        String envValue = System.getenv(envName);
        if (envValue != null && !envValue.trim().isEmpty()) {
            return envValue;
        }

        return defaultValue;
    }
}
