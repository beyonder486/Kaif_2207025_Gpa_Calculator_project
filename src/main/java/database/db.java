package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.logging.Logger;

public class db {
    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;
    private Logger logger = Logger.getLogger(db.class.getName());
    public void getConnection() {
        try {
            if(con == null || con.isClosed()) {
                con=DriverManager.getConnection("jdbc:sqlite:data.db");
                logger.info("Database connection established");
            }
        }
        catch (SQLException e){
            logger.info(e.toString());
        }
    }
    public void createTable() {
//        getConnection();
//        try {}
    }
}
