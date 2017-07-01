package com.sinanaumarah.service.api;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintStream;

public interface FileDownloaderService {
    void downloadFile(File f, PrintStream pout, OutputStream out) throws FileNotFoundException;
}
