package database;
import java.sql.*;

public class DBConnector {

    static Connection connection = null;
    public static Connection getConnection() {
        try { // create a database connection
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:MCWM.db");
            //connection = DriverManager.getConnection("jdbc:sqlite::resource:MCWM.db");
            return connection;

        } catch (Exception e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
            System.out.println("Failed to create the database connection.");
            return null;
        }
    }
}

