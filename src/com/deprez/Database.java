package com.deprez;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * The database class that is used for read/write within the App Store project.
 *
 * @see com.deprez.Community
 * @see com.deprez.Store
 * @see com.deprez.UserAppReviews
 */
public class Database {
    
    /**
     * the url of the sql instance. is used in every method within the {@link com.deprez.Database} class
     */
    private String url;
    
    /**
     * Creates a database object. Uses a default url of "jdbc:sqlite:default.db".
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
                Driver.LOGGER.log(Level.FINE, "Error connecting to SQLite database, probably need to check your " +
                                              "libraries...", ex);
            }
        }
    }
    
    /**
     * Drops the old user_tb and replaces it with the list of {@link com.deprez.User}
     *
     * @param users the list of {@link com.deprez.User}
     *
     * @see Database#dropTable(String)
     * @see Database#createUserTable()
     * @see com.deprez.User
     * @see com.deprez.Community
     */
    void saveUsers(List<User> users) {
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
     *
     * @see Database#connect()
     * @see Database#saveUsers(List)
     * @see Database#loadUsers()
     * @see java.sql
     */
    void createUserTable() {
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
     * Drops the old app_tb and replaces it with the list of {@link com.deprez.App}
     *
     * @param apps the list of {@link com.deprez.App}
     *
     * @see Database#dropTable(String)
     * @see Database#createAppTable()
     * @see com.deprez.App
     * @see com.deprez.Store
     */
    void saveApps(List<App> apps) {
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
    
    /**
     * Creates the app_tb through SQLite
     *
     * @see Database#connect()
     * @see Database#saveApps(List)
     * @see Database#loadApps()
     * @see java.sql
     */
    void createAppTable() {
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
     * Drops the old userapp_tb and replaces it with the list of {@link com.deprez.UserAppReview}
     *
     * @param userAppReviews the list of {@link com.deprez.UserAppReview}
     *
     * @see Database#dropTable(String)
     * @see Database#createUserAppTable()
     * @see com.deprez.UserAppReview
     * @see com.deprez.UserAppReviews
     */
    void saveUserAppReviews(List<UserAppReview> userAppReviews) {
        dropTable("userapp_tb");
        createUserAppTable();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
    
            for (UserAppReview userAppReview : userAppReviews) {
                String sql = "INSERT INTO userapp_tb(userId, appId, reviewScore, reviewDetail) values ("
                             + userAppReview.getUserId() + ","
                             + userAppReview.getAppId() + ","
                             + userAppReview.getAppReviewScore() + ",'"
                             + userAppReview.getAppReviewDetail() + "');";
                Statement statement = connection.createStatement();
                statement.execute(sql);
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
    
    /**
     * TODO: Check SQLite api to see about linking appId and userId to user_tb and app_tb Creates the userapp_tb through
     * SQLite
     *
     * @see Database#connect()
     * @see Database#saveUserAppReviews(List)
     * @see Database#loadUserAppReviews()
     * @see java.sql
     */
    void createUserAppTable() {
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
    
    /**
     * Reads the users from the current user_tb and returns them as a List of {@link com.deprez.User}
     *
     * @return a new list of {@link com.deprez.User}
     *
     * @see com.deprez.User
     * @see com.deprez.Community
     */
    List<User> loadUsers() {
    
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
    
    /**
     * Reads the apps from the current app_tb and returns them as a List of {@link com.deprez.App}
     *
     * @return a new list of {@link com.deprez.App}
     *
     * @see com.deprez.App
     * @see com.deprez.Store
     */
    List<App> loadApps() {
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
    
    /**
     * Reads the apps from the current userapp_tb and returns them as a List of {@link com.deprez.UserAppReview}
     *
     * @return a new list of {@link com.deprez.UserAppReview}
     *
     * @see com.deprez.UserAppReview
     * @see com.deprez.UserAppReviews
     */
    List<UserAppReview> loadUserAppReviews() {
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
    
            Driver.LOGGER.log(Level.FINE, "Loaded UserAppReviews successfully.");
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
}
