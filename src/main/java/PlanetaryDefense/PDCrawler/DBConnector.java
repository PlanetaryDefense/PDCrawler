/**
 * 
 */
package PlanetaryDefense.PDCrawler;

import java.sql.*;

/**
 * @author Yongyao
 *
 */
public class DBConnector {

  /**
   * 
   */
  public DBConnector() {
  }
  
  public static Connection getDBconnection(String userName, String pwd, String dbName)
  {
    Connection connection = null;
    System.out.println("-------- PostgreSQL "
        + "JDBC Connection ------------");

    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      System.out.println("Where is your PostgreSQL JDBC Driver? "
          + "Include in your library path!");
      e.printStackTrace();
    }
    System.out.println("PostgreSQL JDBC Driver Registered!");
    
    try {
      connection = DriverManager.getConnection(
          "jdbc:postgresql://localhost:5432/" + dbName, userName,
          pwd);
    } catch (SQLException e) {
      System.out.println("Connection Failed! Check output console");
      e.printStackTrace();
    }
    
    return connection;
  }
}
