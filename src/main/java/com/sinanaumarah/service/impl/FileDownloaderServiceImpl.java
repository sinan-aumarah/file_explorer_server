package com.sinanaumarah.service.impl;

import com.sinanaumarah.service.api.FileDownloaderService;

import java.io.*;
import java.util.Date;

public class FileDownloaderServiceImpl implements FileDownloaderService {
    private static final int BUFFER_SIZE = 1000;

    @Override
    public void downloadFile(File f, PrintStream pout, OutputStream out) throws FileNotFoundException {
        InputStream file = new FileInputStream(f);
        pout.print("HTTP/1.1 200 OK\r\n" +
                "Date: " + new Date() + "\r\n" +
                "Server: FileServerApp 1.1\r\n\r\n");
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            while (file.available() > 0) {
                out.write(buffer, 0, file.read(buffer));
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

}
