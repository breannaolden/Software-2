package model;


public class User {

    private static String username;
    private static int userID;

/***
 * -------------------------
 * BEGIN SETTERS AND GETTERS
 * -------------------------
 */

    /***
     * @return Getter for User ID
     */
    public static int getUserID() {
        return userID;
    }

    /***
     *
     * @param userID Setter for userID
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /***
     *
     * @return Getter for username
     */
    public static String getUsername() {
        return username;
    }

    /***
     *
     * @param username Setter for username
     */
    public void setUserName(String username) {
        this.username = username;
    }

    /*
    * -----------------------
    * END SETTERS AND GETTERS
    * -----------------------
     */

    /***
     * Constructor for user
     * @param username username
     * @param userID ID of user
     */
    public User(int userID, String username){
        this.username = username;
        this.userID = userID;
    }


}