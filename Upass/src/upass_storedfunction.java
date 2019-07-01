import java.sql.Connection;
import java.sql.Statement;

public class upass_storedfunction {
  private static Connection conn = null;
  private static Statement s = null;

  /** Name of the database */
  private static String dbName = "OrderManager";

  /**
   * checks if the value is positive or not.
   * @param value The integer value
   * @return True if positive , false if negative.
   */
  public static boolean isPositive(int value) {
    if(value >= 0)
      return true;
    else
      return false;
  }

  /**
   * Checks if the attraction is up or down
   * @param attractionID The attraction ID
   * @return True if up or false if down
   */
  public static boolean isStat(int attractionID) {
    upass_connection om = new upass_connection();
    om.startConnection("user1", "password", dbName);
    conn = om.getConnection();
    s = om.getStatement();
    upass_DQL dql = new upass_DQL(conn, s);
    boolean stat = dql.stat(attractionID);
    return stat;
  }

  /**
   * Checks if the pass to be issued is less than the maximum number of passes allowed.
   * @param walletID The wallet ID.
   * @return True if possible or false if not possible
   */
  public static boolean isMax(int walletID) {
    upass_connection om = new upass_connection();
    om.startConnection("user1", "password", dbName);
    conn = om.getConnection();
    s = om.getStatement();
    upass_DQL dql = new upass_DQL(conn, s);
    int upass_count = dql.max_pass_count(walletID);
    int wallet_count = dql.wallet_count(walletID);
    if (upass_count < wallet_count)
      return true;
    else
      return false;
  }

//  public static boolean isTime(int attractionID) {
//    upass_connection om = new upass_connection();
//    om.startConnection("user1", "password", dbName);
//    conn = om.getConnection();
//    s = om.getStatement();
//    upass_DQL dql = new upass_DQL(conn, s);
//    int max_count =dql.max_upass(attractionID);
//  }
}
