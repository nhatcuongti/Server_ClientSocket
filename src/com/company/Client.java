package com.company;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;

public class Client {
    Socket clientSocket = null;

    public Client(){
        try {
            clientSocket = new Socket("localhost", 3200);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getS() {
        return clientSocket;
    }

    public static void main(String[] args){
        Client client = new Client();
        Socket s = client.getS();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Client_GUI(s);
            }
        });
    }
}
