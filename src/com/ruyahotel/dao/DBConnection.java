package com.ruyahotel.dao;

import java.sql.*;

/**
 * Database connection utility for MySQL via JDBC.
 * Update credentials to match your XAMPP setup.
 */
public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/ruya_hotel";
    private static final String USER = "root";       // XAMPP default
    private static final String PASSWORD = "";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found. Add mysql-connector-j JAR to project.", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        try { if (rs != null) rs.close(); } catch (SQLException ignored) {}
        try { if (stmt != null) stmt.close(); } catch (SQLException ignored) {}
        try { if (conn != null) conn.close(); } catch (SQLException ignored) {}
    }

    public static void close(Connection conn, Statement stmt) {
        close(conn, stmt, null);
    }
}
