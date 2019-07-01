import java.sql.SQLException;
import java.sql.Statement;

public class upass_DDL {
  // sql statement connected to UPass database
  private static Statement s = null;

  /**
   * Creates an instance of the class that has the sql statement variable for OrderManager
   * @param s the sql statement
   */
  public upass_DDL(Statement s){
    this.s = s;
  }

  /**
   * Creates all functions.
   */
  public void createFunctions() {
    int success = 0;
    try {
      String func_isPositive = "CREATE FUNCTION isPositive(" +
        " value INTEGER " +
        ") RETURNS BOOLEAN" +
        " PARAMETER STYLE JAVA" +
        " LANGUAGE JAVA" +
        " DETERMINISTIC" +
        " NO SQL" +
        " EXTERNAL NAME" +
        " 'upass_storedfunc.isPositive' ";

      s.executeUpdate(func_isPositive);
      success++;
    } catch (SQLException e) {
      System.err.printf("Did not create function isCountry: %s\n", e.getMessage());
    }
    try {
      String func_isStat = "CREATE FUNCTION isStat(" +
        " attractionID INTEGER " +
        ") RETURNS BOOLEAN" +
        " PARAMETER STYLE JAVA" +
        " LANGUAGE JAVA" +
        " DETERMINISTIC" +
        " NO SQL" +
        " EXTERNAL NAME" +
        " 'upass_storedfunc.isStat' ";

      s.executeUpdate(func_isStat);
      success++;
    } catch (SQLException e) {
      System.err.printf("Did not create function isStat: %s\n", e.getMessage());
    }

    try {
      String func_isStat = "CREATE FUNCTION isMax(" +
        " walletID INTEGER " +
        ") RETURNS BOOLEAN" +
        " PARAMETER STYLE JAVA" +
        " LANGUAGE JAVA" +
        " DETERMINISTIC" +
        " NO SQL" +
        " EXTERNAL NAME" +
        " 'upass_storedfunc.isMax' ";

      s.executeUpdate(func_isStat);
      success++;
    } catch (SQLException e) {
      System.err.printf("Did not create function isMax: %s\n", e.getMessage());
    }

  }

  /**
   * Creates all the tables.
   */
  public void createTables() {
    int success = 0;
    try {
      s.executeUpdate("create table Attraction(" +
        "Name VARCHAR(32) NOT NULL," +
        "no_of_upass INTEGER NOT NULL," +
        "attractionID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
        "status BOOLEAN NOT NULL," +
        "CHECK (isPositive(no_of_upass)),"+
        "PRIMARY KEY(attractionID)" +
        ")"
      );
      success++;
    }catch (SQLException e) {
      System.err.println("Unable to create");
    }
    try {
      s.executeUpdate("create table Guest(" +
          "Name VARCHAR(32) NOT NULL," +
          "guestID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
          "PRIMARY KEY(guestID)" +
          ")"
        );
      success++;
    }catch (SQLException e) {
      System.err.println("Unable to create");
    }
    try {
      s.executeUpdate("create table Wallet(" +
        "walletID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"+
        "max_pass INTEGER NOT NULL," +
        "max_per_hour INTEGER NOT NULL," +
        "guestid INTEGER NOT NULL references Guest(guestID) ON DELETE CASCADE," +
        "visit_date DATE NOT NULL," +
        "CHECK (isPositive(max_pass)),"+
        "CHECK (isPositive(max_per_hour))" +
        ")"
      );
      success++;
    }catch (SQLException e) {
      System.err.println("Unable to create");
    }
    try {
      s.executeUpdate("create table UPass(" +
        "walletID INTEGER references wallet(walletID) ON DELETE CASCADE," +
        "attractionID INTEGER NOT NULL references Attraction(attractionID) ON DELETE CASCADE," +
        "issue_time timestamp," +
        "entry_time timestamp," +
        "CHECK (isStat(attractionID))," +
        "CHECK (isMax(walletID))" +
        ")"
      );
      success++;
    }catch (SQLException e) {
      System.err.println("Unable to create");
    }
  }

  /**
   * Creates all the triggers.
   */
  public void createTriggers() {
    int success = 0;
    try {
      String del_upass = "create trigger del_upass" +
        "after insert into Attraction" +
        "referencing old as insertedattraction" +
        "for each row mode DB2SQL" +
        "WHEN(insertedattraction.status = false)" +
        "delete from UPass where attractionID = insertedattraction.attractionID";
      s.executeUpdate(del_upass);
      success++;
    }catch (SQLException e) {
      System.err.printf("Error creating trigger del_upass");
    }
  }

  /**
   * Drops all the tables
   * @param dbTables An array containing the names of tables.
   */
  public void dropTables(String dbTables[]) {
    // Drops tables if they already exist
    int dropped = 0;
    for(String table : dbTables) {
      try {
        s.execute("drop table " + table);
        dropped++;
      } catch (SQLException e) {
        System.out.println("did not drop " + table);
      }
    }
    if(dropped == dbTables.length){
      System.out.println("Successfully dropped all tables.");
    }

  }

  /**
   * Drops all the functions.
   * @param dbFunctions An array containing the names of all functions.
   */
  public void dropFunctions(String dbFunctions[]) {
    // Drop functions if they already exist
    int dropped = 0;
    for(String fn : dbFunctions) {
      try {
        s.execute("drop function " + fn);
        dropped++;
      } catch (SQLException e) {
        System.out.println("did not drop " + fn);
      }
    }
    if(dropped == dbFunctions.length) {
      System.out.println("Successfully dropped all functions.");
    }

  }
}
