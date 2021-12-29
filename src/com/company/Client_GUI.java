package com.company;

import javax.swing.*;
import java.net.Socket;

public class Client_GUI extends JFrame {

    Socket clientSocket = null;

    public Client_GUI(Socket clientSocket){
        this.clientSocket = clientSocket;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setDefaultLookAndFeelDecorated(true);

        add(new Client_MainPanel(clientSocket));

        setVisible(true);
        pack();
    }


}
