package com.company;

import javax.swing.*;

public class Client_MyProgressBar extends JProgressBar {
    private long fileSize = 0;
    private long currentDownload = 0;

    public void setFileSize(long fileSize){
        this.currentDownload = 0;
        this.fileSize = fileSize;
    }

    public void updateValue(int length) {
        currentDownload += length;
        int percentDownload = (int) ((currentDownload * 100) / fileSize);
        setValue(percentDownload);
    }
}
