package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    ServerSocket ss = null;


    public Server(){
        try {
            ss = new ServerSocket(3200);
            while (true){
                Socket s = ss.accept();
                Server_ClientSocket scs = new Server_ClientSocket(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
