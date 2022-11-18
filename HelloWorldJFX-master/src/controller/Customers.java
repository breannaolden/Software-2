package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class Customers implements Initializable {
    public Label usernameDisplay;
    public Label locationDisplay;
    public Button logoutButton;
    public Button apptButton;
    public Button custButton;
    public Button reportsButton;
    public TextField searchCustomer;
    public TableView<Customer> customerTable;
    public TableColumn<Customer, Integer>  custIDColumn;
    public TableColumn<Customer, String>  nameColumn;
    public TableColumn<Customer, String>  addressColumn;
    public TableColumn<Customer, String>  zipCodeColumn;
    public TableColumn<Customer, String>  phoneColumn;
    public Button addCustomerButton;
    public Button modifyCustomerButton;
    public Button deleteCustomerButtonClick;

    /*
     * ---------------------------
     * BEGIN NAVIGATION AND LOGOUT
     * ---------------------------
     */

    /***
     *
     * @param actionEvent When clicked, verifies users intent to log out, then closes application
     */
    public void logoutButtonClick(ActionEvent actionEvent) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Exit Action");
        confirm.setHeaderText("Are you sure you want to logout and close application?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    /***
     *
     * @param actionEvent when Appointment button clicked, navigates to appointment page
     * @throws IOException .
     */
    public void apptButtonClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainDashboard.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Appointment Scheduler");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /***
     *
     * @param actionEvent When customer button clicked, navigates to customer page
     * @throws IOException
     */
    public void custButtonClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Customers.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Add New Customer");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /***
     *
     * @param actionEvent when reports button is clicked, navigates to the reports page
     * @throws IOException .
     */
    public void reportsButtonClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Reports.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Reports");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /***
     *
     * @param actionEvent when add button is clicked, navigates to the add customers page
     * @throws IOException .
     */
    public void addCustomerButtonClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddCustomers.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Add New Customer");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /*
     * ---------------------------
     * END  NAVIGATION AND LOGOUT
     * ---------------------------
     */


    /*
     * ---------------------------
     * BEGIN APP FUNCTIONALITY
     * ---------------------------
     */


    /***
     *
     * @param actionEvent when the modify button is clicked, navigates to the modify customer page.
     * @throws IOException .
     */
    public void modifyCustomerButtonClick(ActionEvent actionEvent) throws IOException {
        try {
            // load correct page
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyCustomers.fxml"));
            loader.load();
            // send customer data from main customer page to modify customer page
            ModifyCustomers modifyCustomer = loader.getController();
            modifyCustomer.sendCustData(customerTable.getSelectionModel().getSelectedItem());
            modifyCustomer.sendIndex(customerTable.getSelectionModel().getSelectedIndex());
            // create new scene with this data
            Parent root = loader.getRoot();
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Modify Existing Customer");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (NullPointerException | IOException e) {
            // if no customer selected on main customer page
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setTitle("No customer selected.");
            alert.setContentText("Please selected customer to modify.");
            alert.show();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /***
     *
     * @param actionEvent deletes chosen customer
     */
    public void deleteCustomerButtonClick(ActionEvent actionEvent) throws SQLException {
        Customer deleteCustomer = (Customer) customerTable.getSelectionModel().getSelectedItem();
        if (deleteCustomer == null) {
            // if no customer selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No customer selected to delete.");
            alert.setContentText("Please select customer.");
            alert.show();
        } else {
            // delete the customer ID to help with autoincrementation
            int deleteCustID = deleteCustomer.getCustomerID();
            if (ApptDatabase.custHasAppt(deleteCustID)) {
                // if the customer has appointments associated
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText("Customer not deleted.");
                alert.setContentText("Please remove appointments associated with this customer before attempting to delete customer.");
                alert.showAndWait();
            } else {
                // pop up to ensure customer is intending to delete a customer
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Confirm Delete Action");
                confirm.setHeaderText("Are you sure you want to delete this customer?");
                Optional<ButtonType> result = confirm.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // if customer confirms intent, delete customer, reset autoincrement, show popup to confirm deletion
                    CustomerDatabase.deleteChosenCust(deleteCustID);
                    CustomerDatabase.resetAutoIncrement();
                    Customer.removeCustomer(deleteCustomer);
                    Alert inform = new Alert(Alert.AlertType.INFORMATION);
                    inform.setHeaderText("Customer successfully deleted!");
                    inform.show();
                }
            }
        }
    }

    /*
     * ---------------------------
     * END APP FUNCTIONALITY
     * ---------------------------
     */

    /***
     *
     * @param url fills the customer table upon opening of customer page
     * @param resourceBundle .
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        locationDisplay.setText(String.valueOf(Login.getUserZoneID()));
        usernameDisplay.setText(String.valueOf(User.getUsername()));
        customerTable.setItems(Customer.getAllCustomers());
        custIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        zipCodeColumn.setCellValueFactory(new PropertyValueFactory<>("zipCode"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
    }


}