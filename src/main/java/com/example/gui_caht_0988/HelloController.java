package com.example.gui_caht_0988;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class HelloController {
    Socket socket;
    @FXML
    public HBox msgBox;
    @FXML
    Button send;
    @FXML
    TextField textField;
    @FXML
    TextArea textArea;
    @FXML
    TextArea onlineUsers;

    @FXML //аннотация
    private void send(){
        try {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            String text = textField.getText();
            out.writeUTF(text);
            textField.clear();
            textField.requestFocus();
            textArea.appendText("Вы: "+text+"\n");
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    @FXML
    private void  connect(){
        try {
            socket = new Socket("localhost", 8168);
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        String response = "";
                        ArrayList<String> usersName = new ArrayList<String>();
                        try {
                            Object object = ois.readObject();
                            if(object.getClass().equals(usersName.getClass())){
                                usersName = ((ArrayList<String>) object);
                                System.out.println(usersName);
                                onlineUsers.clear(); // Очищает TextArea
                                for (String userName:usersName) {
                                    onlineUsers.appendText(userName+"\n");
                                }
                            }else if (object.getClass().equals(response.getClass())){
                                response = object.toString();
                                textArea.appendText(response+"\n");
                            }else{
                                textArea.appendText("Произошла ошибка");
                            }
                            send.setVisible(false);
                            msgBox.setDisable(false);
                        } catch (Exception e) {
                            e.printStackTrace();
                            send.setVisible(true);
                            msgBox.setDisable(true);
                        }
                    }
                }
            });
            thread.start();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

}
