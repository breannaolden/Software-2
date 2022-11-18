package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddCustomers implements Initializable {
    public Label usernameDisplay;
    public Label locationDisplay;
    public Button logoutButton;
    public Button apptButton;
    public Button custButton;
    public Button reportsButton;
    public TextField customerNameTxt;
    public TextField customerPhoneTxt;
    public TextField customerStreet;
    public ComboBox customerCountryDrop;
    public ComboBox customerCityState;
    public TextField customerZipTxt;
    public TextField customerID;
    public Button addCustomerButton;
    public Button cancelButton;

    /*
     * ---------------------------
     * BEGIN NAVIGATION AND LOGOUT
     * ---------------------------
     */

    /***
     *
     * @param actionEvent Confirms user intent to log out, then closes application
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
     * @param actionEvent Navigates to main appointment page
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
     * @param actionEvent Navigates to main customer page
     * @throws IOException .
     */
    public void custButtonClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Customers.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Customer Log");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /***
     *
     * @param actionEvent Navigates to main reports page
     * @throws IOException .
     */
    public void reportsButtonClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Reports.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Reports Generator");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /*
     * ---------------------------
     * END NAVIGATION AND LOGOUT
     * ---------------------------
     */

    /*
     * ---------------------------
     * BEGIN APP FUNCTIONALITY
     * ---------------------------
     */

    /***
     *
     * @param actionEvent When add button is clicked, parses through data from text fields/combo boxes/date picker
     *                    and creates new customer based on this. Adds this new customer to SQL databases.
     *                    Will throw variety of errors if anything is left blank.
     */
    public void addCustomerButtonClick(ActionEvent actionEvent) {
        try {
            boolean customerAdded = false;
            int customerID;
            String name;
            String address;
            String customerCountry;
            String zipcode;
            String phone;
            int customerDivision = 0;
            LocalDate currentDate = LocalDate.now();
            LocalTime currentTime = LocalTime.now();
            LocalDateTime currentDateTime = LocalDateTime.of(currentDate,currentTime);
            // parse data from textfields and dropdown combo
            customerID = Integer.parseInt(this.customerID.getText());
            name = customerNameTxt.getText();
            address= customerStreet.getText();
            customerCountry = (String) customerCountryDrop.getValue();
            zipcode = customerZipTxt.getText();
            phone = customerPhoneTxt.getText();
            customerDivision = FirstLevelDatabase.getFirstLevelDivisionID((String) customerCityState.getValue());
            String user = User.getUsername();
            if (name.isBlank()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No name entered.");
                alert.setContentText("Please enter name of customer.");
                alert.show();
            } else if (address.isBlank()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No address entered.");
                alert.setContentText("Please enter address of customer.");
                alert.show();
            } else if (zipcode.isBlank()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No zipcode entered.");
                alert.setContentText("Please enter zipcode of customer.");
                alert.show();
            } else if (phone.isBlank()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No phone number entered.");
                alert.setContentText("Please enter phone number of customer.");
                alert.show();
            } else if (customerDivision == 0){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Location Unknown");
                alert.setContentText("Please try again.");
                alert.show();
            } else {
                Customer newCustomer = new Customer(customerID, name, address, zipcode, phone, (String) customerCityState.getValue(),customerDivision,customerCountry);
                CustomerDatabase.addNewCustomer(name, address, zipcode, phone, currentDateTime, user, currentDateTime, user, customerDivision);
                Customer.addCustomers(newCustomer);
                customerAdded = true;
            }
            if (customerAdded) {
                Parent root = FXMLLoader.load(getClass().getResource("/view/Customers.fxml"));
                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                stage.setTitle("Customers");
                stage.setScene(new Scene(root));
                stage.show();
            }
        } catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid entry or blank information.");
            alert.setContentText("Please try again.");
            alert.show();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /***
     *
     * @param actionEvent Populates customer country drop box with name of countries.
     */
    public void customerCountryDropClick(ActionEvent actionEvent) {
        customerCityState.setDisable(false);
        try {
            customerCityState.setItems(FirstLevelDatabase.getDivision((String) customerCountryDrop.getValue()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /***
     *
     * @param actionEvent when cancel button is clicked, verifies user's intent to cancel, then navigates
     *                    to main customers page.
     * @throws IOException .
     */
    public void cancelButtonClick(ActionEvent actionEvent) throws IOException {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Cancel");
        confirm.setHeaderText("Are you sure you would like to cancel?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            Parent root = FXMLLoader.load(getClass().getResource("/view/Customers.fxml"));
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Customer Log");
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    /*
     * ---------------------------
     * END APP FUNCTIONALITY
     * ---------------------------
     */

    /***
     *
     * @param url autopopulates username and location to upper left corner of app
     * @param resourceBundle autopopulates customer ID and customer country.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        locationDisplay.setText(String.valueOf(Login.getUserZoneID()));
        usernameDisplay.setText(String.valueOf(User.getUsername()));
        try {
            customerID.setText(String.valueOf(CustomerDatabase.getMaxID() + 1));
            customerCountryDrop.setItems(FirstLevelDatabase.getCountries());
            //customerCityState.setItems(CustomerDatabase.getCityState());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}