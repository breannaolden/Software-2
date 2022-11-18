package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FirstLevel {
    private int divisionID;
    private static String divisionName;
    private int countryID;

    /*
    * --------------------------
    * BEGIN SETTERS AND GETTERS
    * --------------------------
     */

    /***
     * @return Getter for division ID
     */
    public int getDivisionID() {
        return divisionID;
    }

    /***
     * @param divisionID Setter for divisionID
     */
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    /***
     * @return getter for country ID
     */
    public int getCountryID() {
        return countryID;
    }

    /***
     * @param countryID Setter for countryID
     */
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    /***
     * @return Getter for division name
     */
    public static String getDivisionName() {
        return divisionName;
    }

    /***
     * @param divisionName Setter for division name
     */
    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    /*
    * -------------------------
    * END SETTERS AND GETTERS
    * -------------------------
     */




    /*
    * -----------------------
    * BEGIN OBSERVABLE LISTS
    * -----------------------
     */

    /***
     * Private - creates observable list of all divisions
     */
    private static ObservableList<FirstLevel> getAllDivisions = FXCollections.observableArrayList();

    /**
     *
     * @return Observable list of all divisions
     */
    public static ObservableList<FirstLevel> getGetAllDivisions() {
        return getAllDivisions;
    }
    /*
    * ---------------------
    * END OBSERVABLE LISTS
    * ---------------------
     */




    /*
    * --------------------
    * BEGIN FUNCTIONALITY
    * --------------------
     */

    /***
     * Constructor for FirstLevel
     * @param divisionID   divisionID
     * @param divisionName divisionName
     * @param countryID    countryID
     */
    public FirstLevel(int divisionID, String divisionName, int countryID) {
        this.divisionID = divisionID;
        this.countryID = countryID;
        this.divisionName = divisionName;
    }

    /*
    * ---------------------
    * END FUNCTIONALITY
    * ---------------------
     */

}