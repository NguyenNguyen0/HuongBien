package com.huongbien.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static Connection connection = null;
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=HuongBien";
    private static final String USER = "sa";
    private static final String PASSWORD = "sapassword";

    private Database() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Load SQL Server JDBC driver (optional if driver is not auto-loaded)
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connection established!");
            } catch (ClassNotFoundException e) {
                System.out.println("SQLServer JDBC Driver not found.");
                e.printStackTrace();
                throw new SQLException(e);
            }
        }

        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connection closed!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
