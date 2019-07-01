import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

/**
 * upass: A Database for Managing Products and Orders
 * @author Karan Machado
 *
 * The RDBMS maintains information about products that can be ordered by costumers,
 * tracks inventory levels of each product, and handles orders for product by customers.
 * It enables a business to track sales of products, and allows customers to determine product availability and place orders.
 *
 */
public class upass {
  // connection variables
  private static Connection conn = null;
  private static Statement s = null;

  /** Name of the database */
  private static String dbName = "upass";

  /** Contains the names of the tables in upass */
  private static String dbTables[]= {
    "Attraction","Guest","Wallet","Upass"
  };

  /** Contains names of stored functions in upass */
  private static String dbFunctions[]={
    "isPositive"
  };

  /* Contains names of triggers in upass (unused)*/
  private static String dbTriggers[] = {
    "del_upass","isStat","isMax"
  };

  /* Contains names of procedures in upass (unused)*/
  private static String dbProcedures[] = {
    // none
  };

  /** Initializes the database and creates the tables */
  public static void main(String[] args)
  {
    // start connection
    upass_connection upass = new upass_connection();
    upass.startConnection("user1", "password", dbName);
    conn = upass.getConnection();
    s = upass.getStatement();


    upass_DDL ddl = new upass_DDL(s);
    upass_DML dml = new upass_DML(conn, s);
    upass_DQL dql = new upass_DQL(conn, s);
    upass_API api = new upass_API(dml, dql);



    System.out.println("Initializing database...");
    System.out.println("\nDropping tables and functions and triggers:");
    ddl.dropTables(dbTables);
    ddl.dropFunctions(dbFunctions);
    // trigger drop not needed, because dropping tables drops the triggers as well
    // ddl.dropTriggers(s, dbTriggers);


    System.out.println("\nCreating functions:");
    ddl.createFunctions();


    System.out.println("\nCreating tables:");
    ddl.createTables();

    System.out.println("\nCreating triggers:");
    ddl.createTriggers();

    // truncate, then insert data into tables
    System.out.println("\nTruncating tables:");
    dml.truncateTables(dbTables);
    System.out.println("\nInserting values:");
    dml.insertAll();



   

    // close connection
    upass.closeConnection(dbName);

  }

}