package com.example.clientserver;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import com.example.clientserver.ConnectionSocket;

public class AuthController {

    public Label errorLabel;
    public Socket socket;
    public DataInputStream in;
    public DataOutputStream out;
    public static ConnectionSocket clientSocket = new ConnectionSocket();


    public TextField loginField;
    public PasswordField passwordField;

    private boolean authOK;
    private String authString = "";

    public void initialize(){
        authOK = false;
        socket = clientSocket.getSocket();
        in = clientSocket.getIn();
        out = clientSocket.getOut();
    }

    public void sumbitButton(ActionEvent actionEvent) throws IOException {
        try {
            out.writeUTF("/auth " + loginField.getText() + " " + passwordField.getText());
            loginField.clear();
            passwordField.clear();
            Thread.sleep(1000);
            authString = in.readUTF();
            if (authString.startsWith("/authok")){
                authOK = true;
                FXMLLoader loader = new FXMLLoader(AuthController.class.getResource("Main-view.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(this.loginField.getScene().getWindow());
                stage.showAndWait();
            } else {
                errorLabel.setVisible(true);
                errorLabel.setText(authString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
