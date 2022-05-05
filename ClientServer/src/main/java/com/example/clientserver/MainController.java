package com.example.clientserver;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MainController {

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public TextArea messageArea;
    public TextField messageInput;

    public void initialize(){
        socket = AuthController.clientSocket.getSocket();
        in = AuthController.clientSocket.getIn();;
        out = AuthController.clientSocket.getOut();

        Thread thread = new Thread(() -> {
            while (true){
                try {
                    in.readUTF();
                    messageArea.appendText(in.readUTF() + "\n");
                    if (in.readUTF().equalsIgnoreCase("/end")){
                        AuthController.clientSocket.closeConnection();
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }


    public synchronized void sendButton(ActionEvent actionEvent) {
        try {
            out.writeUTF(messageInput.getText());
            messageInput.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}