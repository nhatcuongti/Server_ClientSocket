package com.company;

import java.io.*;
import java.net.Socket;

public class Server_ClientSocket {

    Socket socket = null;
    String pathData = "C:\\Users\\CSM\\IdeaProjects\\Server_ClientSocket\\src\\com\\company\\Data";

    public Server_ClientSocket(Socket s) {
        socket = s;
        ReceivedProcess();
    }

    public void ReceivedProcess(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                    File folderData = new File(pathData);


                    while (true){
                        String requiredUser = br.readLine();

                        if (requiredUser.equals("Watch List")){
                            for (File file : folderData.listFiles()){
                                bw.write(file.getName());
                                bw.newLine();
                                bw.flush();
                            }
                            bw.write("/End/");
                            bw.newLine();
                            bw.flush();
                        }
                        else if (requiredUser.equals("Download")){
                            String fileName =pathData + "\\" + br.readLine();

                            File file = new File(fileName);
                            bw.write(String.valueOf(file.length()));
                            bw.newLine();
                            bw.flush();

                            BufferedReader brFile = new BufferedReader(new FileReader(file));
                            String line = "";

                            while ((line = brFile.readLine()) != null){
                                bw.write(line);
                                bw.newLine();
                                bw.flush();
                            }

                            brFile.close();

                            bw.write("/End/");
                            bw.newLine();
                            bw.flush();

                        }
                        else{
                            String keywords = br.readLine();
                            for (File file : folderData.listFiles())
                                if (file.getName().contains(keywords)){
                                    bw.write(file.getName());
                                    bw.newLine();
                                    bw.flush();
                                }

                            bw.write("/End/");
                            bw.newLine();
                            bw.flush();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        if (socket != null)
                            socket.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

            }
        });
        t.start();
    }

}
