import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class upass_DQL {
  Connection conn = null;
  Statement s = null;

  // holds results from queries
  private ResultSet rs = null;

  /**
   * Creates an instance of the class with the sql statement for UPass database.
   * @param s the sql statement
   */
  public upass_DQL(Connection conn, Statement s) {
    this.conn = conn;
    this.s = s;
  }

  /**
   * Selects and displays all guests.
   */
  public void selectAllGuest() {
    try {
      ResultSet rs = s.executeQuery("select * from Guest");
      while (rs.next()) {
        System.out.println(rs.getString("Name"));
        System.out.println(rs.getInt("guestID"));
      }
    }catch (SQLException e) {
      System.err.println(e.getMessage());
    }
  }

  /**
   * Selects and displays guest information that match the guest id.
   * @param guest_ID The guest id
   */
  public void selectGuestID(int guest_ID) {
    try {
      rs = s.executeQuery("Select * from Guest where guestID = "+guest_ID);
      while (rs.next()) {
        System.out.println(rs.getString("Name"));
        System.out.println(rs.getInt("guestID"));
      }
    }
    catch (SQLException e) {
      System.out.println("Guest with guestid = " +guest_ID+"was not found");
      System.err.printf("Guest ID : %d not found \n",guest_ID);
    }
  }

  /**
   * Selects and displays all wallet information.
   */
  public void selectWallet() {
    try {
      rs = s.executeQuery("Select * from Wallet");
      while (rs.next()) {
        System.out.println(rs.getInt("walletID"));
        System.out.println(rs.getInt("max_pass"));
        System.out.println(rs.getInt("max_per_hour"));
        System.out.println(rs.getInt("guestid"));
        System.out.println(rs.getDate("visit_date"));
      }

    }catch (SQLException e) {
      System.err.println(e.getMessage());
    }
  }

  /**
   * Selects and displays wallet information that match the wallet id.
   * @param walletID The wallet id.
   */
  public void selectWalletID(int walletID) {
    try {
      rs = s.executeQuery("Select * from Wallet where walletID = " + walletID);
      while (rs.next()) {
        System.out.println(rs.getInt("walletID"));
        System.out.println(rs.getInt("max_pass"));
        System.out.println(rs.getInt("max_per_hour"));
        System.out.println(rs.getInt("guestid"));
        System.out.println(rs.getDate("visit_date"));
      }

    }catch (SQLException e) {
      System.out.println("Wallet with walletid = " + walletID + "was not found");
      System.err.println(e.getMessage());
    }
  }

  /**
   * Selects and displays all upass information.
   */
  public void selectUPass() {
    try {
      rs = s.executeQuery("Select * from UPass");
      while(rs.next()) {
        System.out.println(rs.getInt("walletID"));
        System.out.println(rs.getInt("attarctionID"));
        System.out.println(rs.getTime("issue_time"));
        System.out.println(rs.getTime("entry_time"));
      }

    }catch (SQLException e) {
      System.err.println(e.getMessage());
    }
  }

  /**
   * Selects and displays all attraction information.
   */
  public void selectattraction() {
    try {
      rs = s.executeQuery("Select * from Attraction");
      while (rs.next()) {
        System.out.println(rs.getString("Name"));
        System.out.println(rs.getInt("no_of_upass"));
        System.out.println(rs.getInt("attractionID"));
      }
    }catch (SQLException e) {
      System.err.println(e.getMessage());
    }
  }


  /**
   * Selects and displays all attraction information.
   * @param attractionid The particular attraction id.
   */
  public void selectattractionID(int attractionid) {
    try{
      rs = s.executeQuery("Select * from Attraction where attractionID = "+attractionid);
      while (rs.next()) {
        System.out.println(rs.getString("Name"));
        System.out.println(rs.getInt("no_of_upass"));
        System.out.println(rs.getInt("attractionID"));
      }
    }catch (SQLException e) {
      System.out.println("Attarction with attraction id =" + attractionid + "was not found");
      System.err.println(e.getMessage());
    }
  }

  /**
   * Finds if the particular attraction is up or down.
   * @param attractionid The attraction id.
   * @return true(up) or false(down)
   */
  public boolean stat(int attractionid) {
    boolean stat = false;
    try{
      rs = s.executeQuery("Select * from Attraction where attractionID = "+attractionid);
      while (rs.next()) {
         stat = rs.getBoolean("status");
      }
    }catch (SQLException e) {
      System.out.println("Attarction with attraction id =" + attractionid + "was not found");
      System.err.println(e.getMessage());
    }
    return stat;
  }

  /**
   * Finds the maximum number of issued passes for a wallet.
   * @param walletid The wallet id.
   * @return the count of issued passes.
   */
  public int max_pass_count(int walletid) {
    int count = 0;
    try{
      rs = s.executeQuery("Select * from UPass where walletID = "+walletid);
      while (rs.next()) {
        int cp = rs.getInt("walletID");
        count += 1 ;
      }
    }catch (SQLException e) {
      System.out.println("UPass with wallet id =" + walletid + "was not found");
      System.err.println(e.getMessage());
    }
    return count;
  }

  /**
   * Finds the maximum number of passes for a particular wallet
   * @param walletid The wallet id.
   * @return the maximum number of passes.
   */
  public int wallet_count(int walletid) {
    int count = 0;
    try{
      rs = s.executeQuery("Select * from Wallet where walletID = "+walletid);
      while (rs.next()) {
        count = rs.getInt("max_pass");
      }
    }catch (SQLException e) {
      System.out.println("UPass with wallet id =" + walletid + "was not found");
      System.err.println(e.getMessage());
    }
    return count;
  }

  /**
   * Finds the maximum number of passes per hour for an attraction.
   * @param attractionid The attraction id.
   * @return the maximum number of passes per hour.
   */
  public int max_upass(int attractionid) {
    int max = 0;
    try {
      rs = s.executeQuery("Select * from Attraction where attractionID = "+attractionid);
      while (rs.next()) {
        max = rs.getInt("no_of_upass");

      }
    }catch (SQLException e) {
      System.out.println("Attraction with id = "+attractionid+" was not found");
      System.err.println(e.getMessage());
    }
    return max;
  }
}
