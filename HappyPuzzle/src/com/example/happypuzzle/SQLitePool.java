package com.example.happypuzzle;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.sqlite.SQLiteConfig;

import com.vaadin.addon.sqlcontainer.connection.JDBCConnectionPool;

/**
 * This code is a modification from http://dev.vaadin.com/ticket/10458
 */
public class SQLitePool implements JDBCConnectionPool {

    private final Connection conn;
    private boolean res;

    public SQLitePool(String url) throws SQLException {
        SQLiteConfig config = new SQLiteConfig();
        conn = config.createConnection(url);
    }

    @Override
    public Connection reserveConnection() throws SQLException {
        if (!res) {
            res = true;
            return conn;
        }

        return null;
    }

    @Override
    public void releaseConnection(Connection conn) {
        res = false;
    }

    @Override
    public void destroy() {
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(SQLitePool.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
}
