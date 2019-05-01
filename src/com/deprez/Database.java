package com.deprez;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private String url;

    /**
     * Creates a database object.
     * Uses a default url of "jdbc:sqlite:default.db".
     */
    public Database() {
        url = "jdbc:sqlite:default.db";
    }

    /**
     * Creates a database object.
     *
     * @param url The url used for connecting to the SQLite database (E.g. "jdbc:sqlite:MyDatabase.db")
     */
    public Database(String url) {
        this.url = url;
    }

    /**
     * Connects to the SQLite database, primary use is debugging.
     */
    void connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    /**
     * Builds an sql statement from user input and executes it, creating the table if it does not already exist.
     *
     * TODO: Validate input, check if lengths of arrays are equal and throw something if incorrect
     * @param tb_name the name of the table created in the database (E.g. user_tb)
     * @param columnIds the id of each column created within the table.
     * @param types an array with the sql type of each column created (E.g. INTEGER)
     * @param args a two dimensional array, with each row being the list of arguments for the corresponding columnId index.
     */
    void createTable(String tb_name, String[] columnIds, String[] types, String[][] args) {
        if(columnIds.length != types.length || columnIds.length != args.length) {
            // TODO: throw an error
        }
        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        sql.append(tb_name + "(");
        for(int j = 0; j < columnIds.length; j++) {
            sql.append(columnIds[j] + " ");
            sql.append(types[j] + " ");
            for(int i = 0; i < args[j].length; i++) {
                sql.append(args[j][i] + ((i+1 != args[j].length) ? " " : ((j+1 != columnIds.length) ? ", " : "")));
            }
        }
        sql.append(");");
        System.out.println(sql);

        try {
            Connection connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();
            statement.execute(sql.toString());
            System.out.println("Created " + tb_name + " successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
