package model;


public class Contacts {

    private static int contactID;
    private static String contactName;
    private static String email;

    /**
     * This creates a new contact
     * @param contactID id of the contact
     * @param contactName name of the contact
     * @param email email of the contact
     */
    public Contacts(int contactID, String contactName, String email) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.email = email;
    }

    /*
    --------------------------
    BEGIN GETTERS AND SETTERS
    --------------------------
     */

    /**
     * Getter for contact Id
     * @return ID
     */
    public static int getContactID() {
        return contactID;
    }

    /**
     *
     * @param contactID Setter for contactID
     */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /**
     * Getter for contact name
     * @return name
     */
    public static String getContactName() {
        return contactName;
    }

    /**
     *
     * @param contactName Setter for contactName
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * Getter for contact email
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /***
     *
     * @param email Setter for email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /*
    ------------------------
    END GETTERS AND SETTERS
    ------------------------
     */

}

