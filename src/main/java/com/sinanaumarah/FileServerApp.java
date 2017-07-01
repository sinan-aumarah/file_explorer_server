package com.sinanaumarah;

import com.sinanaumarah.service.api.DisplayService;
import com.sinanaumarah.service.api.FileDownloaderService;
import com.sinanaumarah.service.impl.FileDownloaderServiceImpl;
import com.sinanaumarah.service.impl.FileExplorerDisplayServiceImpl;
import com.sinanaumarah.service.impl.HttpFileServerImpl;

public class FileServerApp {
    private static final int SOCKET_PORT = 3222;

    public static void main(String[] args) {
        final String currentDir;

        if (args.length == 1) {
            currentDir = args[0];
        } else {
            currentDir = System.getProperty("user.dir");
        }
        System.out.println("Working directory: " + currentDir);
        DisplayService service = new FileExplorerDisplayServiceImpl(currentDir);
        FileDownloaderService downloaderService = new FileDownloaderServiceImpl();

        HttpFileServerImpl server = new HttpFileServerImpl(SOCKET_PORT, currentDir, service, downloaderService);
        server.doRun();
    }
}