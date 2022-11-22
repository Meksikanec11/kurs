package com.example.kurs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HelloController {



    @FXML
    TextField loginField;
    @FXML
    PasswordField passwordField;
    @FXML
    private Button submitButton;
    @FXML
    private CheckBox pass_toggle;
    @FXML
    private Text timer1;
    @FXML
    private Text timer2;
    @FXML
    private TextField passwordHidden;

    int enter_counter = 0;


    @FXML
    protected void loginButtonClick() throws SQLException, IOException {
        Window owner = submitButton.getScene().getWindow();
        if (loginField.getText().isEmpty()) {
            if (enter_counter>0) {

                new Thread(()->{
                    submitButton.setDisable(true);
                    try {
                        Thread.sleep(10000);
                        submitButton.setDisable(false);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
                new Thread(()->{
                    timer1.setVisible(true);
                    timer2.setVisible(true);
                    try {
                        for (int i = 10; i > 0; i--) {
                            timer2.setText(Integer.toString(i));
                            Thread.sleep(1000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    timer1.setVisible(false);
                    timer2.setVisible(false);
                }).start();
                showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                        "Please enter your login");
                enter_counter = 0;
                return;
            } else {
                showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                        "Please enter your login");
                enter_counter++;
                return;
            }
        }
        if (passwordField.getText().isEmpty()) {
            if (enter_counter>0) {

                new Thread(()->{
                    submitButton.setDisable(true);
                    try {
                        Thread.sleep(10000);
                        submitButton.setDisable(false);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();

                new Thread(()->{
                    timer1.setVisible(true);
                    timer2.setVisible(true);
                    try {
                        for (int i = 10; i > 0; i--) {
                            timer2.setText(Integer.toString(i));
                            Thread.sleep(1000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    timer1.setVisible(false);
                    timer2.setVisible(false);
                }).start();

                showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                        "Please enter a password");
                enter_counter = 0;
                return;
            }
            else {
                showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                        "Please enter a password");
                enter_counter++;
                return;
            }
        }
        if (loginQuery(loginField.getText(), passwordField.getText())) {
            login();
        }
    }


    private void login() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        Stage stage = (Stage) loginField.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    private boolean loginQuery(String login, String password) throws SQLException {

        String query = "Select login,password from client";
        ResultSet resultSet = DBConnector.executeQuery(query);
        while (resultSet.next()) {
            if ((resultSet.getString(1).equals(login)) && (resultSet.getString(2).equals(password))) {
                LoginController.login = login;
                return true;
            }
        }
        DBConnector.closeConnection();
        return false;
    }

    public static void infoBox(String infoMessage, String headerText, String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();


    }

    private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    @FXML
    public void togglevisiblePassword(ActionEvent event) {
        if (pass_toggle.isSelected()) {
            passwordHidden.setText(passwordField.getText());
            passwordHidden.setVisible(true);
            passwordField.setVisible(false);
            return;
        }
        passwordField.setText(passwordHidden.getText());
        passwordField.setVisible(true);
        passwordHidden.setVisible(false);
    }
}
