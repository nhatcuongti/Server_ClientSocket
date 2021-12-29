package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.WildcardType;
import java.net.Socket;

public class Client_MainPanel extends JPanel implements Runnable, ActionListener {
    Socket clientSocket = null;
    BufferedReader br = null;
    BufferedWriter bw = null;

    GridBagConstraints gbc = new GridBagConstraints();

    public Client_MainPanel(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(500, 400));
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        createGUI();

    }

    DefaultListModel<String> dlm = new DefaultListModel<>();
    JList<String> listFile = new JList<>(dlm);
    Client_MyProgressBar progressBar = new Client_MyProgressBar();

    JButton searchBtn = new JButton("Find");
    JTextField searchField = new JTextField("Search ...");
    JButton downloadBtn = new JButton("Download");
    JButton watchBtn = new JButton("Watch List");


    private void createGUI() {
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(searchField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        add(searchBtn, gbc);
        searchBtn.addActionListener(this);

        gbc.gridx = 2;
        gbc.gridy = 0;
        add(downloadBtn, gbc);
        downloadBtn.addActionListener(this);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        add(watchBtn, gbc);
        watchBtn.addActionListener(this);

        JScrollPane sp = new JScrollPane(listFile);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        add(sp, gbc);

        progressBar.setStringPainted(true);
        progressBar.setValue(0);
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(progressBar, gbc);

    }


    File outputFile = null;

    @Override
    public void run() {
        String line = "";
        BufferedWriter bwFile = null;
        try {
            bwFile = new BufferedWriter(new FileWriter(outputFile));
            while (!(line = br.readLine()).equals("/End/")){
                bwFile.write(line);

                byte[] bytes = line.getBytes();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.updateValue(bytes.length);
                    }
                });

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bwFile != null) {
                try {
                    bwFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == watchBtn){
                bw.write("Watch List");
                bw.newLine();
                bw.flush();

                watchListHandle();
            }
            else if (e.getSource() == searchBtn){
                bw.write("find");
                bw.newLine();
                bw.flush();

                bw.write(searchField.getText());
                bw.newLine();
                bw.flush();
                watchListHandle();
            }
            else{
                bw.write("Download");
                bw.newLine();
                bw.flush();

                downloadHandle();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    private void watchListHandle() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String fileName = "";
                dlm.clear();
                try {
                    System.out.println("-------------------------");
                    System.out.println("FileName");


                    while (!(fileName = br.readLine()).equals("/End/")) {
                        System.out.println(fileName);
                        String finalFileName = fileName;
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                dlm.addElement(finalFileName);
                            }
                        });
                    }
                    System.out.println("-------------------------");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }




    private void downloadHandle() {
        String fileName = listFile.getSelectedValue();
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int choice = fileChooser.showSaveDialog(null);
            if (choice == JFileChooser.APPROVE_OPTION){
                bw.write(fileName);
                bw.newLine();
                bw.flush();

                long fileSize = Long.valueOf(br.readLine());
                progressBar.setValue(0);
                progressBar.setFileSize(fileSize);

                outputFile =new File(fileChooser.getSelectedFile().getAbsolutePath() + "\\" + fileName);

                Thread receiveThread = new Thread(this);
                receiveThread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
