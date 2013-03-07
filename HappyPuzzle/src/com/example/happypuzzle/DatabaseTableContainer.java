package com.example.happypuzzle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;

/**
 * Simple container for a single table in SQLite for storing ResultTuples. Each
 * row is represented by DbItem object.
 * 
 * @author Team HappyPuzzle
 * 
 */
public class DatabaseTableContainer implements Container {

    private String databaseName = "";
    private String tablename = "";

    /**
     * Constructor
     * 
     * @param table
     *            Name of the table.
     * @param db
     *            Name of SQLite db file
     * @throws ClassNotFoundException
     *             If SQLite Driver not installed
     * @throws SQLException
     *             If database queries fail
     */
    public DatabaseTableContainer(String table, String db)
            throws ClassNotFoundException, SQLException {

        this.databaseName = db;
        this.tablename = table;

        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:"
                + databaseName);
        Statement stat = conn.createStatement();
        stat.executeUpdate("create table  if not exists " + tablename
                + " (id integer, name string, score integer)");

    }

    /**
     * Removes a row from the table if its content is equal to a given
     * ResultTuple.
     * 
     * @param rt
     *            ResultTuple representing the Item to be removed
     * @return boolean whether operation was successful
     */
    public boolean removeItem(ResultTuple rt) {
        return this.removeItem(getItemId(rt));
    }

    /**
     * Finds item ID for a row with content that is equal to rt.
     * 
     * @param rt
     *            ResultTuple representing the Item to be found
     * @return Integer Item Id
     */
    public Integer getItemId(ResultTuple rt) {

        ArrayList<Integer> ids = new ArrayList<Integer>();
        Integer res = null;

        // TODO Auto-generated method stub
        // Get a Row
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:"
                    + databaseName);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30); // set timeout to 30 sec.
            ResultSet rs = statement.executeQuery("select * from " + tablename);
            while (rs.next()) {
                // read the result set
                if ((rt.getTotalMoves() == rs.getInt("score"))
                        && (rt.getName().equals(rs.getString("name")))) {
                    res = rs.getInt("id");
                }

            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e);
            }
        }

        return res;
    }

    @Override
    public Item getItem(Object itemId) {
        return new DbItem(this.databaseName, this.tablename, itemId);
    }

    @Override
    public Collection<?> getContainerPropertyIds() {
        // TODO Auto-generated method stub
        ArrayList<String> res = new ArrayList<String>();
        res.add("Result");
        return res;
    }

    @Override
    public Collection<?> getItemIds() {

        ArrayList<Integer> ids = new ArrayList<Integer>();

        // TODO Auto-generated method stub
        // Get a Row
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        Connection connection = null;
        ResultTuple rt = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:"
                    + databaseName);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30); // set timeout to 30 sec.
            ResultSet rs = statement.executeQuery("select * from " + tablename);
            while (rs.next()) {
                // read the result set
                ids.add(rs.getInt("id"));

            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e);
            }
        }

        return ids;
    }

    @Override
    public Property getContainerProperty(Object itemId, Object propertyId) {
        Item item = getItem(itemId);
        return item.getItemProperty(propertyId);
    }

    @Override
    public Class<?> getType(Object propertyId) {
        // TODO Auto-generated method stub
        try {
            return Class.forName("ResultTuple");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int size() {
        // TODO Auto-generated method stub
        return getItemIds().size();
    }

    @Override
    public boolean containsId(Object itemId) {
        return getItemIds().contains(itemId);
    }

    @Override
    public Item addItem(Object itemId) throws UnsupportedOperationException {
        // TODO Auto-generated method stub
        return new DbItem(this.databaseName, this.tablename, itemId);
    }

    @Override
    public Object addItem() throws UnsupportedOperationException {
        // TODO Auto-generated method stub
        ArrayList<Integer> ids = (ArrayList<Integer>) this.getItemIds();
        Integer max = 0;
        if (ids.size() > 0) {
            max = Collections.max(ids) + 1;
        }
        new DbItem(this.databaseName, this.tablename, max);
        return max;
    }

    @Override
    public boolean removeItem(Object itemId)
            throws UnsupportedOperationException {

        // load the sqlite-JDBC driver using the current class loader
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:"
                    + databaseName);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30); // set timeout to 30 sec.
            int id = 0;
            Integer int_id = (Integer) itemId;
            statement.executeUpdate("delete from " + this.tablename
                    + " where id=" + int_id);

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e);
            }
        }

        return true;
    }

    @Override
    public boolean addContainerProperty(Object propertyId, Class<?> type,
            Object defaultValue) throws UnsupportedOperationException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean removeContainerProperty(Object propertyId)
            throws UnsupportedOperationException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean removeAllItems() throws UnsupportedOperationException {
        // TODO Auto-generated method stub
        for (Object itemId : this.getItemIds()) {
            removeItem(itemId);
        }
        return true;
    }
}