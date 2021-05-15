package chatting;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.*;

public class Server implements Runnable {

    @FXML
    private TextArea mess;

    @FXML
    private TextArea history;

    ServerSocket serverSocket = null;
    Socket socketOfServer = null;
    DataInputStream is = null;
    DataOutputStream os = null;

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert history != null : "fx:id=\"history\" was not injected: check your FXML file 'server.fxml'.";
        assert mess != null : "fx:id=\"mess\" was not injected: check your FXML file 'server.fxml'.";

        try {
            serverSocket = new ServerSocket(9999);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socketOfServer = serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread t = new Thread(Server.this);
        t.start();
    }

    public void action(ActionEvent event) throws Exception {

        os = new DataOutputStream(socketOfServer.getOutputStream());

        String inFromUser = mess.getText();

        os.writeUTF(inFromUser);
        os.flush();

        history.appendText(inFromUser + "\n");
        mess.clear();
    }

    @Override
    public void run() {
        try {
            is = new DataInputStream(socketOfServer.getInputStream());
            while (true) {
                if (socketOfServer != null) {
                    String fromClient = is.readUTF();
                    history.appendText("Client say: " + fromClient + "\n");
                }
            }
        } catch (Exception e) {

        }
    }
}
