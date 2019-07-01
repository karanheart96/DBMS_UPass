import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class upass_DML {
  // database connection variables
  private static Connection conn = null;
  private static Statement s = null;


  private static String guest = "data/guest.txt";
  private static String wallet = "data/wallet.txt";
  private static String attraction = "data/attraction.txt";
  private static String UPass = "data/UPass.txt";

  private static String insertIntoGuest = "insert into Guest(Name) values(?)";
  private static String insertIntoWallet = "insert into Wallet(max_pass,max_per_hour,guestid,visit_date) values(?,?,?,?)";
  private static String insertIntoAttraction = "insert into Attraction(Name,no_of_upass,status) values(?,?,?)";
  private static String insertIntoUPass = "insert into UPass(walletID,attractionID,issue_time,entry_time) values(?,?,?,?)";


  /**
   * Create the DML class with the specified database connection and statement
   *
   * @param conn the connection to database
   * @param s    the sql statement
   */
  public upass_DML(Connection conn, Statement s) {
    this.conn = conn;
    this.s = s;
  }

  /**
   * Truncate tables, clearing them of existing data
   *
   * @param dbTables an array of table names
   */
  public static void truncateTables(String dbTables[]) {
    int deleted = 0;
    for(String table : dbTables) {
      try {
        s.executeUpdate("delete from " + table);
        deleted++;
      }
      catch (SQLException e) {
        System.out.println("Did not truncate table " + table);
      }
    }
    if(deleted == dbTables.length){
      System.out.println("Successfully truncated all tables.");
    }
  }

  /**
   * Calls all insert into table functions that insert data from respective files into the database.
   */
  public static void insertAll() {
    int success = 0;
    try {
      insertGuestFile();
      success++;
    } catch (SQLException e) {
      System.err.printf("Unable to insert into Guest\n");
      System.err.println(e.getMessage());

    }

    try {
      insertWalletFile();
      success++;
    } catch (SQLException e) {
      System.err.printf("Unable to insert into Wallet\n");
      System.err.println(e.getMessage());
    }

    try {
      insertAttractionFile();
      success++;
    } catch (SQLException e) {
      System.err.printf("Unable to insert into Attraction\n");
      System.err.println(e.getMessage());
    }

    try {
      insertUPassFile();
      success++;
    } catch (SQLException e) {
      System.err.printf("Unable to insert into UPass\n");
      System.err.println(e.getMessage());
    }
  }

  /**
   * Inserts the given information into the guest table.Returns the guest id on success or -1 if unable to insert the guest.
   * @param Name
   * @return
   */
  public int insertGuest(String Name) {
    int guest_id = -1;
    try {
      PreparedStatement insertRow_guest = conn.prepareStatement(insertIntoGuest,PreparedStatement.RETURN_GENERATED_KEYS);
      insertRow_guest.setString(1,Name);

      insertRow_guest.executeUpdate();
      ResultSet rs = insertRow_guest.getGeneratedKeys();

      if (rs.next()) {
        guest_id = rs.getInt(1);
      }
      insertRow_guest.close();
      rs.close();
    }catch (SQLException e) {
      System.err.println("unable to insert into guest"+e.getMessage());
    }
    return guest_id;
  }

  /**
   * Inserts the given information into the wallet table.Returns the wallet id on success or -1 if unable to insert the wallet.
   * @param max_pass
   * @param max_per_hour
   * @param guestid
   * @return
   */
  public int insertWallet(int max_pass,int max_per_hour,int guestid) {
    int walletid = -1;
    try {
      PreparedStatement insertRow_wallet = conn.prepareStatement(insertIntoWallet,PreparedStatement.RETURN_GENERATED_KEYS);
      insertRow_wallet.setInt(1,max_pass);
      insertRow_wallet.setInt(2,max_per_hour);
      insertRow_wallet.setInt(3,guestid);

      insertRow_wallet.executeUpdate();
      ResultSet rs = insertRow_wallet.getGeneratedKeys();

      if (rs.next()) {
        walletid = rs.getInt(1);
      }
      insertRow_wallet.close();
      rs.close();

    }catch (SQLException e) {
      System.err.println("unable to insert into wallet"+e.getMessage());
    }
    return walletid;
  }


  /**
   * Inserts the given information into the upass table.Returns 1 on success or -1 on failure.
   * @param walletID
   * @param attractionID
   * @return Returns 1 on success or -1 on failure
   */
  public int insertupass(int walletID,int attractionID) {
    int value = -1;
    try {
      PreparedStatement insertRow_upass = conn.prepareStatement(insertIntoUPass,PreparedStatement.RETURN_GENERATED_KEYS);
      insertRow_upass.setInt(1,walletID);
      insertRow_upass.setInt(2,attractionID);
      value = 1;

    }catch (SQLException e) {
      System.err.println("unable to insert into upass"+e.getMessage());
    }
    return value;
  }

  /**
   * Inserts the given information into the attraction table
   * @param name
   * @param no_of_upass
   * @param status
   * @return Returns -1 on failure or 1 on success.
   */
  public int insertattraction(String name,int no_of_upass,Boolean status)  {
    int value = -1;
    try {
      PreparedStatement insertRow_attrac = conn.prepareStatement(insertIntoAttraction,PreparedStatement.RETURN_GENERATED_KEYS);
      insertRow_attrac.setString(1,name);
      insertRow_attrac.setInt(2,no_of_upass);
      insertRow_attrac.setBoolean(3,status);
      insertRow_attrac.executeUpdate();
      value = 1;

    }catch (SQLException e){
      System.err.println("Unable to insert into attraction"+e.getMessage());
    }
    return value;
  }


  /**
   * Inserts information into the guest table from a file.
   * @throws SQLException
   */
  public static void insertGuestFile() throws SQLException {
    PreparedStatement insertRow_guest = conn.prepareStatement(insertIntoGuest);
    try (
      BufferedReader br = new BufferedReader(new FileReader(new File(guest)));
    ) {
      String line;
      while((line = br.readLine()) != null) {
        String[] data = line.split("\t");
        String name = data[0];
        insertRow_guest.setString(1,name);
        insertRow_guest.executeUpdate();

        if (insertRow_guest.getUpdateCount() != 1) {
          System.err.printf("Unable to insert Wallet table");
        }
      }

      } catch (FileNotFoundException e) {
      System.err.printf("Unable to open the file: %s\n", guest);
    } catch (IOException e) {
      System.err.printf("Error reading line.\n");
    }

    }

  /**
   * Inserts information into the wallet table from a file.
   * @throws SQLException
   */
  public static void insertWalletFile() throws SQLException {
    PreparedStatement insertRow_wallet = conn.prepareStatement(insertIntoWallet);
    try (
      BufferedReader br = new BufferedReader(new FileReader(new File(wallet)));
    ) {
      String line;
      while((line = br.readLine()) != null) {
        String[] data = line.split("\t");
        String max_pass = data[0];
        String max_per_hour = data[1];
        String guestid = data[2];
        String visit_date = data[3];
        Date date1 = Date.valueOf(visit_date);
        int mp = Integer.parseInt(max_pass);
        int mph = Integer.parseInt(max_per_hour);
        int gid = Integer.parseInt(guestid);

        insertRow_wallet.setInt(1,mp);
        insertRow_wallet.setInt(2,mph);
        insertRow_wallet.setInt(3,gid);
        insertRow_wallet.setDate(4,date1);

        insertRow_wallet.executeUpdate();

        if (insertRow_wallet.getUpdateCount() != 1) {
          System.err.printf("Unable to insert Guest table");
        }
      }

    } catch (FileNotFoundException e) {
      System.err.printf("Unable to open the file: %s\n", guest);
    } catch (IOException e) {
      System.err.printf("Error reading line.\n");
    }


  }

  /**
   * Inserts information into the attraction table from a file.
   * @throws SQLException
   */
  public static void insertAttractionFile() throws SQLException {
    PreparedStatement insertRow_attraction = conn.prepareStatement(insertIntoAttraction);
    try (
      BufferedReader br = new BufferedReader(new FileReader(new File(attraction)));
    ) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] data = line.split("\t");
        String name = data[0];
        String no_of_pass = data[1];
        String status = data[2];
        int nop = Integer.parseInt(no_of_pass);
        Boolean status1 = Boolean.parseBoolean(status);

        insertRow_attraction.setString(1,name);
        insertRow_attraction.setInt(2,nop);
        insertRow_attraction.setBoolean(3,status1);

        insertRow_attraction.executeUpdate();

        if (insertRow_attraction.getUpdateCount() != 1) {
          System.err.printf("Unable to insert Attraction table");
        }
      }

    }catch (FileNotFoundException e) {
      System.err.printf("Unable to open the file: %s\n", guest);
    } catch (IOException e) {
      System.err.printf("Error reading line.\n");
    }
    }

  /**
   * Inserts information into the upass table from a file.
   * @throws SQLException
   */
  public static void insertUPassFile() throws SQLException {
    PreparedStatement insertRow_UPass = conn.prepareStatement(insertIntoUPass);
    try (
      BufferedReader br = new BufferedReader(new FileReader(new File(UPass)));
      ) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] data = line.split("\t");
        String walletID = data[0];
        String attractionID = data[1];
        String issue_time = data[2];
        String entry_time = data[3];

        int wid = Integer.parseInt(walletID);
        int aid = Integer.parseInt(attractionID);

        insertRow_UPass.setInt(1,wid);
        insertRow_UPass.setInt(2,aid);




      }
    }catch (FileNotFoundException e) {
      System.err.printf("Unable to open the file: %s\n", guest);
    } catch (IOException e) {
      System.err.printf("Error reading line.\n");
    }

  }

}
