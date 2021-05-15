package chatting;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.*;

public class Client implements Runnable {

    @FXML
    private TextArea mess;

    @FXML
    private TextArea history;

    Socket socketOfClient = null;
    DataInputStream is;
    DataOutputStream os;

    @FXML
    void initialize() {
        assert history != null : "fx:id=\"history\" was not injected: check your FXML file 'client.fxml'.";
        assert mess != null : "fx:id=\"mess\" was not injected: check your FXML file 'client.fxml'.";

        try {
            socketOfClient = new Socket("localhost", 9999);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread t = new Thread(Client.this);
        t.start();
    }

    public void action(ActionEvent event) throws Exception {

        os = new DataOutputStream(socketOfClient.getOutputStream());

        String inFromUser = mess.getText();
        os.writeUTF(inFromUser);
        os.flush();

        history.appendText(inFromUser + "\n");
        mess.clear();
    }

    @Override
    public void run() {
        try {
            is = new DataInputStream(socketOfClient.getInputStream());
            while (true) {
                if (socketOfClient != null) {
                    String fromServer = is.readUTF();
                    history.appendText("Server say: " + fromServer + "\n");
                }
            }
        } catch (Exception e) {

        }
    }
}
