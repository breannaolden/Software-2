package controller;

import helper.JDBC;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;
import model.Login;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class LoginScreen implements Initializable {
    public Label userLocation;
    public Label usernameLabel;
    public TextField usernameTxt;
    public Label passwordLabel;
    public PasswordField passwordTxt;
    public Button loginButton;
    public Button exitButton;
    public Label welcomeLabel;
    public Label loginLabel;
    private boolean loginSuccess;

    /***
     *
     * @param actionEvent unused action
     */
    public void usernameTxtClick(ActionEvent actionEvent) {
    }

    /***
     *
     * @param actionEvent unused action
     */
    public void passwordTxtClick(ActionEvent actionEvent) {
    }

    /***
     *
     * @param actionEvent when login button is clicked, checks if user and pass are located in database.
     * @throws IOException writes file about login and if it was successful
     * @throws SQLException
     */
    public void loginButtonClick(ActionEvent actionEvent) throws IOException, SQLException {
        String username = usernameTxt.getText();
        String password = passwordTxt.getText();
        loginSuccess = Login.loggedIn(username, password);
        System.out.println(loginSuccess);
        ZonedDateTime zonedNow = ZonedDateTime.now(ZoneId.systemDefault());
        zonedNow.withZoneSameInstant(ZoneOffset.UTC);
        LocalDateTime now = zonedNow.toLocalDateTime();
        loginTracker.loginTracking(username, loginSuccess);

        if (loginSuccess) {
            Parent root = FXMLLoader.load(getClass().getResource("/view/MainDashboard.fxml"));
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Appointment Scheduler");
            stage.setScene(new Scene(root));
            stage.show();
            Appointment.nextAppointment();
        } else {
            if (userLocale.getLanguage().equals("fr")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Les identifiants de connexion ne sont pas valides.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Login credentials are invalid.");
                alert.setContentText("Please try again or contact your administrator for assistance.");
                alert.showAndWait();
            }
        }
    }

    /***
     *
     * @param actionEvent when exit button is clicked, confirms users intent to exit in both
     *                    french and english. If "OK" is clicked, application is closed.
     */
    public void exitButtonClick(ActionEvent actionEvent) {
        if (userLocale.getLanguage().equals("fr")) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmer quitter");
            alert.setHeaderText("Voulez-vous vraiment quitter le programme?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                JDBC.closeConnection();
                System.exit(0);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Quit");
            alert.setHeaderText("Are you sure you want to quit the program?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                JDBC.closeConnection();
                System.exit(0);
            }
        }
    }

    /***
     * establishes where the user is.
     */
    private static Locale userLocale = Locale.getDefault();
    private static ZoneId userZoneID = ZoneId.systemDefault();

    /***
     * Translates text descriptions to french. Utilized google translate for french.
     */
    public void frenchLabels() {
        usernameLabel.setText("Nom d'utilisateur");
        passwordLabel.setText("Mot de passe");
        loginButton.setText("Connexion");
        exitButton.setText("Sortir");
        welcomeLabel.setText("Bienvenue!");
        loginLabel.setText("Veuillez vous connecter.");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(userZoneID);
        if (userLocale.getLanguage().equals("fr")) {
            frenchLabels();
        }
        userLocation.setText(String.valueOf(userZoneID));
    }
}
