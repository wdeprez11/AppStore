package com.deprez;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

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

            Driver.LOGGER.log(Level.FINE, "Connection to SQLite established.");
        } catch (SQLException e) {
            Driver.LOGGER.log(Level.WARNING, "Failed to open connection with database.", e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Driver.LOGGER.log(Level.FINE, "Error connecting to SQLite database, probably need to check your libraries...", ex);
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
            Driver.LOGGER.log(Level.FINE, "Dropped " + tb_name + " successfully.");
        } catch (SQLException e) {
            Driver.LOGGER.log(Level.WARNING, "Failed to drop table: " + tb_name, e);
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
            Driver.LOGGER.log(Level.FINE, "createUserTable done.");
        } catch (SQLException e) {
            Driver.LOGGER.log(Level.WARNING, "Failed to create user table...", e);
        }
    }

    /**
     * Creates the app_tb through SQLite
     */
    public void createAppTable() {
        String sql = "CREATE TABLE IF NOT EXISTS app_tb(" +
                "appId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "appName VARCHAR(64) NOT NULL, " +
                "creatorId INTEGER);";
        try {
            Connection connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();
            statement.execute(sql);
            Driver.LOGGER.log(Level.FINE, "createAppTable done.");
        } catch (SQLException e) {
            Driver.LOGGER.log(Level.WARNING, "Failed to create app table...", e);
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
                "reviewScore INTEGER," +
                "reviewDetail VARCHAR(2000));";
        try {
            Connection connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();
            statement.execute(sql);
            Driver.LOGGER.log(Level.FINE, "createUserAppTable done.");
        } catch (SQLException e) {
            Driver.LOGGER.log(Level.WARNING, "Failed to create user app table...", e);
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

            Driver.LOGGER.log(Level.FINE, "Apps saved.");
        } catch (SQLException e) {
            Driver.LOGGER.log(Level.WARNING, "Failed to connect to database", e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Driver.LOGGER.log(Level.WARNING, "Failed to close connection", ex);
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

            Driver.LOGGER.log(Level.FINE, "Users saved.");

        } catch (SQLException e) {
            Driver.LOGGER.log(Level.SEVERE, "Failed to save users table!", e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Driver.LOGGER.log(Level.WARNING, "Failed to close connection to database...", ex);
            }
        }
    }

    public void saveUserAppReviews(List<User> users) {
        dropTable("userapp_tb");
        createUserAppTable();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);

            for (User user : users) {
                for (UserAppReview appReview : user.getUserApps()) {
                    String sql = "INSERT INTO userapp_tb(userId, appId, reviewScore, reviewDetail) values ("
                            + user.getUserId() + ","
                            + appReview.getAppId() + ","
                            + appReview.getAppReviewScore() + ",'"
                            + appReview.getAppReviewDetail() + "');";
                    Statement statement = connection.createStatement();
                    statement.execute(sql);

                }
            }

            Driver.LOGGER.log(Level.FINE, "App reviews saved.");
        } catch (SQLException e) {
            Driver.LOGGER.log(Level.WARNING, "Failed to save user app reviews table!", e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Driver.LOGGER.log(Level.WARNING, "Failed to close connection to database...", ex);
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

            Driver.LOGGER.log(Level.FINE, "Apps loaded.");

        } catch (SQLException e) {
            Driver.LOGGER.log(Level.WARNING, "Failed to connect to database...", e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Driver.LOGGER.log(Level.WARNING, "Failed to close connection with database...", ex);
            }
        }
        return apps;
    }

    public List<UserAppReview> loadUserAppReviews() {
        Connection connection = null;
        List<UserAppReview> userAppReviews = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(url);
            String sql = "SELECT * FROM userapp_tb;";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int userId = resultSet.getInt("userId");
                int appId = resultSet.getInt("appId");
                int reviewScore = resultSet.getInt("reviewScore");
                String reviewDetail = resultSet.getString("reviewDetail");
                userAppReviews.add(new UserAppReview(userId, appId, reviewScore, reviewDetail));
            }
        } catch (SQLException e) {
            Driver.LOGGER.log(Level.WARNING, "Failed to connect to database...", e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Driver.LOGGER.log(Level.WARNING, "Failed to close connection with database...", ex);
            }
        }
        return userAppReviews;
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

            Driver.LOGGER.log(Level.FINE, "Users loaded.");

        } catch (SQLException e) {
            Driver.LOGGER.log(Level.WARNING, "Failed to get users, couldn't connect to database...", e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Driver.LOGGER.log(Level.WARNING, "Failed to close connection of database", ex);
            }
        }
        return users;
    }


}
