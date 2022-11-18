package model;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class CustomerDatabase {

    /*
    * -----------------------
    * BEGIN OBSERVABLE LISTS
    *  ----------------------
     */

    /**
     * Creates observable list of all customers
     * @return array list of customers
     */
    public static ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT cust.Customer_ID, cust.Customer_Name, cust.Address, cust.Postal_Code, cust.Phone, cust.Division_ID, fld.Division, fld.COUNTRY_ID, ctry.Country " +
                    "FROM customers as cust JOIN first_level_divisions " +
                    "as fld on cust.Division_ID = fld.Division_ID " +
                    "JOIN countries as ctry ON fld.COUNTRY_ID = ctry.Country_ID";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                int customerID = resultSet.getInt("Customer_ID");
                String customerName = resultSet.getString("Customer_Name");
                String address = resultSet.getString("Address");
                String zipCode = resultSet.getString("Postal_Code");
                String division = resultSet.getString("Division");
                int divisionID = resultSet.getInt("Division_ID");
                String phoneNumber = resultSet.getString("Phone");
                String customerCountry = resultSet.getString("Country");

                Customer customer = new Customer(customerID, customerName, address, zipCode, phoneNumber, division, divisionID, customerCountry);
                customerList.add(customer);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return customerList;
    }

    /**
     * Creates observable list of customer IDs
     * @return list of customer ID.
     */
    public static ObservableList<Integer> getCustomerID() throws SQLException {
        ObservableList<Integer> customerID=FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT Customer_ID FROM customers";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            customerID.add(resultSet.getInt("Customer_ID"));
        } return customerID;
    }

    /*
    * --------------------
    * END OBSERVABLE LISTS
    * --------------------
     */

    /*
    * --------------------
    * BEGIN FUNCTIONALITY
    * --------------------
     */

    /**
     * Finds highest ID number currently
     * @return Customer ID
     * @throws SQLException .
     */
    public static int getMaxID() throws SQLException {
        int nextID = 0;
        String sql = "SELECT max(Customer_ID) AS Max_Cust_ID FROM customers";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            nextID = resultSet.getInt("Max_Cust_ID");
        } return nextID;
    }

    /**
     * Resets auto increment
     * @throws SQLException .
     */
    public static void resetAutoIncrement() throws SQLException {
        String sql ="ALTER TABLE customers AUTO_INCREMENT = 1";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.executeUpdate();
    }

    /**
     * Add new customer
     * @param customerName  customerName
     * @param address       address
     * @param zipcode       zip
     * @param phone         phone
     * @param createdDate   date it was created (auto)
     * @param createdBy     who created it (auto)
     * @param lastUpdate    when it was last updated (auto)
     * @param lastUpdatedBy who updated it last (auto)
     * @param divisionID    divisionID
     * @throws SQLException .
     */
    public static void addNewCustomer(String customerName, String address, String zipcode, String phone, LocalDateTime createdDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy, int divisionID) throws SQLException {
        // create new row for a new customer
        String sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By,Last_Update, Last_Updated_By, Division_ID) VALUES (?, ?, ?, ?, ?, ?, ? ,? ,?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, customerName);
        ps.setString(2, address);
        ps.setString(3,zipcode);
        ps.setString(4,phone);
        ps.setTimestamp(5, Timestamp.valueOf(createdDate));
        ps.setString(6,createdBy);
        ps.setTimestamp(7,Timestamp.valueOf(lastUpdate));
        ps.setString(8,lastUpdatedBy);
        ps.setInt(9,divisionID);
        ps.executeUpdate();
    }

    /**
     * Modify exisiting customer
     * @param customerName customerName
     * @param address address
     * @param zipcode zipcode
     * @param phone phone
     * @param lastUpdate when it was last updated
     * @param lastUpdatedBy who updated it last
     * @param divisionID divisionID
     * @param customerID customerID
     * @throws SQLException .
     */
    public static void modifyCustomer(int customerID, String customerName, String address, String zipcode, String phone, LocalDateTime lastUpdate, String lastUpdatedBy, int divisionID) throws SQLException {
        // update the customers table using the below specified columns in order with the above specified pieces of data
        String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ? ,Phone = ?,Last_Update = ?, Last_Updated_By = ?,Division_ID = ? WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, customerName);
        ps.setString(2, address);
        ps.setString(3,zipcode);
        ps.setString(4,phone);
        ps.setTimestamp(5,Timestamp.valueOf(lastUpdate) );
        ps.setString(6,lastUpdatedBy);
        ps.setInt(7,divisionID);
        ps.setInt(8,customerID);
        ps.executeUpdate();
    }

    /**
     * Delete customer by ID number
     * @param customerID customerID
     * @throws SQLException .
     */
    public static void deleteChosenCust(int customerID) throws SQLException {
        // Delete from customer table any customer that has the selected customerID integer
        String sql = "DELETE FROM customers WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerID);
        ps.executeUpdate();
    }
}


