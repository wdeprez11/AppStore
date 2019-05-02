package com.deprez;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private String url;
    // TODO: Add class attribute in order to store the tables created.

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
     * @param tb_name   the name of the table created in the database (E.g. user_tb)
     * @param columnIds the id of each column created within the table.
     * @param types     an array with the sql type of each column created (E.g. INTEGER)
     * @param args      a two dimensional array, with each row being the list of arguments for the corresponding columnId index.
     */
    /*
    void createTable(String tb_name, String[] columnIds, String[] types, String[][] args) {
        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        sql.append(tb_name).append("(");
        for (int j = 0; j < columnIds.length; j++) {
            sql.append(columnIds[j]).append(" ");
            sql.append(types[j]).append(" ");
            for (int i = 0; i < args[j].length; i++) {
                sql.append(args[j][i]).append((i + 1 != args[j].length) ? " " : ((j + 1 != columnIds.length) ? ", " : ""));
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
    */

    /**
     * Drops a table from the database.
     *
     * @param tb_name the table to drop from the database
     */
    void dropTable(String tb_name) {
        String sql = "DROP TABLE " + tb_name;
        try {
            Connection connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();
            statement.execute(sql);
            System.out.println("Dropped " + tb_name + " successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Creates the user_tb through SQLite
     */
    public void createUserTable() {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS user_tb(" +
                "userId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userName varchar(50) NOT NULL);";
        try {
            Connection connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();
            statement.execute(sql);
            System.out.println("createUserTable done.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Creates the app_tb through SQLite
     */
    public void createAppTable() {
        String sql = "CREATE TABLE IF NOT EXISTS app_tb(" +
                "appId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "appName VARCHAR(64) NOT NULL UNIQUE, " +
                "creatorId INTEGER);";
        try {
            Connection connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();
            statement.execute(sql);
            System.out.println("createAppTable done.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * TODO: Check SQLite api to see about linking appId and userId to user_tb and app_tb
     * Creates the userapp_tb through SQLite
     */
    public void createUserAppTable() {
        String sql = "CREATE TABLE IF NOT EXISTS userapp_tb(" +
                "appId INTEGER," +
                "userId INTEGER," +
                "review VARCHAR(2000));";
        try {
            Connection connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();
            statement.execute(sql);
            System.out.println("createUserAppTable done.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveApps(List<App> apps) {
        dropTable("app_tb");
        createAppTable();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);

            for (App app : apps) {
                String sql = "INSERT INTO app_tb (appName) values ('" + app.getAppName() + "');";
                Statement statement = connection.createStatement();
                statement.execute(sql);
            }

            System.out.println("Apps saved.");

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

    public void saveUsers(List<User> users) {
        dropTable("user_tb");
        createUserTable();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);

            for (User user : users) {
                String sql = "INSERT INTO user_tb (userName) values ('" + user.getUserName() + "');";
                Statement statement = connection.createStatement();
                statement.execute(sql);
            }

            System.out.println("Users saved.");

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

    public List<App> loadApps() {
        Connection connection = null;
        List<App> apps = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(url);

            String sql = "SELECT * FROM app_tb";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                apps.add(new App(resultSet.getInt("appId"), resultSet.getString("appName")));
            }

            System.out.println("Apps loaded.");

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
        return apps;
    }

    public List<User> loadUsers() {

        Connection connection = null;
        List<User> users = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(url);

            String sql = "SELECT * FROM user_tb;";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                users.add(new User(resultSet.getInt("userId"), resultSet.getString("userName")));
            }

            System.out.println("Users loaded.");

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
        return users;
    }

}
