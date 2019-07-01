import java.sql.Connection;
import java.sql.Statement;

public class upass_API {
  // holds an instance of the DML class for insert statements
  private upass_DML dml = null;

  // holds an instance of the DQL class for select statements
  private upass_DQL dql = null;

  /**
   * Creates an instance of the API class with instances of the DML and DQL classes
   */
  public upass_API(upass_DML dml, upass_DQL dql){
    this.dml = dml;
    this.dql = dql;
  }

  /**
   * Creates a guest with the given attributes.
   * @param s sql statement
   * @param conn Connection to the database
   * @param Name The name of the guest
   * @return
   */
  public int createguest(Statement s, Connection conn,String Name) {
    int guest_id = dml.insertGuest(Name);
    if(guest_id != -1) {
      System.out.println("Successfully added "+Name);
    }
    return guest_id;
  }

  /**
   * Creates a pass with the given attributes.
   * @param s sql statement
   * @param conn Connection to the database
   * @param walletID The unique wallet ID
   * @param attractionID The unique attraction ID
   */
  public void createupass(Statement s ,Connection conn,int walletID,int attractionID) {
    int val = dml.insertupass(walletID,attractionID);
    if(val != -1 ) {
      System.out.println("Successfully added");
    }
  }

  /**
   * Creates a wallet with the given attributes.
   * @param s sql statement
   * @param conn Connection to the database
   * @param max_pass The maximum number of passes for the wallet
   * @param max_per_hour The maximum number of passes per hour.
   * @param guestid The guest ID to which the wallet is tied.
   * @return
   */
  public int createwallet(Statement s,Connection conn,int max_pass,int max_per_hour,int guestid) {
    int val = dml.insertWallet(max_pass, max_per_hour, guestid);
    if(val != -1) {
      System.out.println("Succesfully added");
    }
    return val;
  }

  /**
   * Creates an attraction with the given attributes.
   * @param s sql statement
   * @param conn Connection to the database
   * @param name Name of the attraction
   * @param no_of_upass The maximum number of passes that can be issued.
   * @param status The status if its up or down.
   * @return
   */
  public int createattraction(Statement s ,Connection conn,String name,int no_of_upass,Boolean status) {
    int val = dml.insertattraction(name, no_of_upass, status);
    if(val != -1) {
      System.out.println("Successfully added");
    }
    return val;
  }
}
