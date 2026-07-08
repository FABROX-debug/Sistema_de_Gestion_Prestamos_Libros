package com.mycompany.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {

    protected Connection conexion;

    private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private final String DB_URL = getConfig("ilib.db.url", "ILIB_DB_URL", "jdbc:mysql://localhost:3306/ilib");
    private final String USER = getConfig("ilib.db.user", "ILIB_DB_USER", "root");
    private final String PASS = getConfig("ilib.db.password", "ILIB_DB_PASSWORD", "1234");

    public void Conectar() throws ClassNotFoundException {
        try {
            Class.forName(JDBC_DRIVER);
            conexion = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Conexion exitosa");
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Cerrar() throws SQLException {
        if (conexion != null) {
            if (!conexion.isClosed()) {
                conexion.close();
            }
        }
    }

    private String getConfig(String propertyName, String envName, String defaultValue) {
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
