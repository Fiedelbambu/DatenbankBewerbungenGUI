package com.example.demoprojekt;

import java.sql.*;


public class conn {
    public static Connection getConnection() throws SQLException {
        // Register the MySQL JDBC driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC driver not found.");
        }

        return DriverManager.getConnection("jdbc:mysql://localhost:3306/bewerbung", "ubuntu", "ubuntu");
    }


}
