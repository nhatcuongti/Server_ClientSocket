package com.company;

import java.io.*;
import java.net.Socket;

public class Server_ClientSocket {

    Socket socket = null;

    public Server_ClientSocket(Socket s) {
        socket = s;
    }

    public void ReceivedProcess(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                    File folderData = new File("Data");


                    while (true){
                        String requiredUser = br.readLine();

                        if (requiredUser.equals("Watch List")){
                            for (File file : folderData.listFiles()){
                                bw.write(file.getName());
                                bw.flush();
                            }
                        }
                        else if (requiredUser.equals("Download")){
                            String fileName ="/Data/" + br.readLine();


                            BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());

                            File file = new File(fileName);
                            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                            byte[] buffer = new byte[1024];
                            int bytesRead = 0;
                            long fileSize = file.length();
                            byte[] byteFileSize ;

                            while ((bytesRead = bis.read(buffer, 0, 1024)) >= 0){
                                bos.write(buffer, 0, bytesRead);
                                bos.flush();
                            }
                        }
                        else{
                            String keywords = br.readLine();
                            for (File file : folderData.listFiles())
                                if (file.getName().contains(keywords)){
                                    bw.write(file.getName());
                                    bw.flush();
                                }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        t.start();
    }

}
