package com.example.happypuzzle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;

/**
 * Represents a single row in an SQLite table that contains high scores.
 * 
 * @author Team HappyPuzzle
 * 
 */
public class DbItem implements Item {

    private final String databaseName;
    private final String tablename;
    private final Object id;
    private final Integer int_id;

    /**
     * Constructor
     * 
     * @param databaseName
     *            Path to the database file
     * @param tablename
     *            Tablename in database, should be empty or have columns int id,
     *            String name, int score
     * @param itemId
     *            Id of the Item
     */
    public DbItem(String databaseName, String tablename, Object itemId) {
        // TODO Auto-generated constructor stub
        this.databaseName = databaseName;
        this.tablename = tablename;
        this.id = itemId;
        this.int_id = (Integer) itemId;

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    /**
     * Generates a property based on rt.
     * 
     * @param rt
     *            ResultTuple to be generated into a Property
     * @return boolean whether operation was successful
     */
    public boolean addResultTuple(ResultTuple rt) {

        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:"
                    + databaseName);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30); // set timeout to 30 sec.
            String name = rt.getName();
            int score = rt.getTotalMoves();

            statement.executeUpdate("insert into " + tablename + " values("
                    + this.int_id + ", '" + name + "', " + score + ")");

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
                return false;
            }
        }
        return true;

    }

    @Override
    public Property getItemProperty(Object id) {
        if (((String) id).equals("Result")) {

            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e1) { // TODO Auto-generated catch
                                                  // block
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
                ResultSet rs = statement.executeQuery("select * from "
                        + tablename);
                while (rs.next()) {
                    // read the result set
                    if (rs.getInt("id") == this.int_id) {
                        rt = new ResultTuple(rs.getString("name"),
                                rs.getInt("score"));
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
            return new ObjectProperty<ResultTuple>(rt);
        }
        return null;
    }

    @Override
    public Collection<?> getItemPropertyIds() {
        // TODO Auto-generated method stub
        ArrayList<String> res = new ArrayList<String>();
        res.add("Result");
        return res;
    }

    @Override
    public boolean addItemProperty(Object id, Property property)
            throws UnsupportedOperationException {

        emptyItem();
        ResultTuple ret = (ResultTuple) property.getValue();
        addResultTuple(ret);

        return true;
    }

    @Override
    public boolean removeItemProperty(Object id)
            throws UnsupportedOperationException {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * Empties the item.
     * 
     */
    private void emptyItem() {

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
            statement.executeUpdate("delete from " + this.tablename
                    + " where id=" + this.int_id);

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
            // return f;
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

    }

}
