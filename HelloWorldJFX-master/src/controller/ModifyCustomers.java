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

public class ModifyCustomers implements Initializable {
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
    public Button modifyCustomerButton;
    public Button cancelButton;
    int i;

    /*
    * -------------------------
    BEGIN NAVIGATION AND LOGOUT
    * -------------------------
    */

    /***
     *
     * @param actionEvent When logout clicked, verifies users intent to log out, if "OK" clicked,
     *                    closes application.
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
     * @param actionEvent When appointment button clicked, navigates to main dashboard/appts page.
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
     * @param actionEvent When reports button clicked, navigates to reports page.
     * @throws IOException .
     */
    public void reportsButtonClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Reports.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Reports");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /*
    * -------------------------
    END NAVIGATION AND LOGOUT
    * -------------------------
    */



    /*
    * -------------------------
    BEGIN APP FUNCTIONALITY
    * -------------------------
    */

    /***
     *
     * @param actionEvent When modify button clicked, parses thru data from text fields/combo boxes/date pickers
     *                    and modifies the customer in the SQL database. Provides variety of errors for blank
     *                    or invalid entries. If successfully modified, navigates back to main customer page.
     */
    public void modifyCustomerButtonClick(ActionEvent actionEvent) {
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
            } else if (customerDivision == 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Location Unknown");
                alert.setContentText("Please try again.");
                alert.show();
            } else {
                Customer customer = new Customer(customerID, name, address, zipcode, phone, (String) customerCityState.getValue(),customerDivision,customerCountry);
                CustomerDatabase.modifyCustomer(customerID, name, address, zipcode, phone, currentDateTime, user, customerDivision);
                Customer.modifyCustomer(i, customer);
                customerAdded = true;
            } if (customerAdded) {
                Parent root = FXMLLoader.load(getClass().getResource("/view/Customers.fxml"));
                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                stage.setTitle("Customers");
                stage.setScene(new Scene(root));
                stage.show();
            }
        } catch (NumberFormatException e) {
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
     * @param actionEvent Unused action
     */
    public void customerCountryDropClick(ActionEvent actionEvent) {
    }

    /***
     *
     * @param actionEvent when cancel button is clicked, verifies users intent to cancel, if "ok" is clicked,
     *                    navigates to main customer page
     * @throws IOException
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

    /***
     *
     * @param chosenCustomer
     * @throws SQLException
     */
    public void sendCustData(Customer chosenCustomer) throws SQLException {
        customerID.setText(String.valueOf(chosenCustomer.getCustomerID()));
        customerNameTxt.setText(chosenCustomer.getName());
        customerPhoneTxt.setText(chosenCustomer.getPhone());
        customerStreet.setText(chosenCustomer.getAddress());
        customerZipTxt.setText(chosenCustomer.getZipCode());
        customerCityState.setValue(chosenCustomer.getCustomerDivision());
        customerCountryDrop.setValue(chosenCustomer.getCustomerCountry());
        customerCountryDrop.setItems(FirstLevelDatabase.getCountries());
        customerCityState.setItems(FirstLevelDatabase.getDivision((String) customerCountryDrop.getValue()));
    }

    public void sendIndex(int chosenIndex) {
        i = chosenIndex;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        locationDisplay.setText(String.valueOf(Login.getUserZoneID()));
        usernameDisplay.setText(String.valueOf(User.getUsername()));
    }
}