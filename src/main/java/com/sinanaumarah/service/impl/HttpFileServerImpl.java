package com.sinanaumarah.service.impl;


import com.sinanaumarah.service.api.DisplayService;
import com.sinanaumarah.service.api.FileDownloaderService;
import com.sinanaumarah.service.api.HttpFileServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.Date;

public class HttpFileServerImpl implements HttpFileServer {
    private final int portNum;
    private final String workingDir;
    private DisplayService displayService;
    private FileDownloaderService downloaderService;

    public HttpFileServerImpl(int portNum, String workingDir,
                              DisplayService displayService,
                              FileDownloaderService downloaderService) {
        this.portNum = portNum;
        this.workingDir = workingDir;
        this.displayService = displayService;
        this.downloaderService = downloaderService;
    }

    public void doRun() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(portNum);
        } catch (IOException e) {
            System.err.println("Could not start server: " + e);
            System.exit(-1);
        }
        System.out.println("FileServerApp accepting connections on port " + portNum);
        System.out.println("Open the browser paste go to: http://localhost:3222");

        // request handler loop
        while (true) {
            listenAndAct(serverSocket);
        }
    }

    void listenAndAct(ServerSocket serverSocket) {
        Socket socket = null;
        try {
            // wait for request
            socket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream out = new BufferedOutputStream(socket.getOutputStream());
            PrintStream pout = new PrintStream(out);

            String request = in.readLine();
            if (request == null) {
                return;
            }

            logRequest(socket, request);

            if (isNotValidGetRequest(request)) {
                printError(pout, socket, "400", "Bad Request",
                        "Your browser sent a request that " +
                                "this server could not understand.");
            } else {
                String req = request.substring(4, request.length() - 9).trim();
                req = URLDecoder.decode(req, "UTF-8");
                if (req.contains("..") || req.endsWith("~")) {
                    printError(pout, socket, "403", "Forbidden",
                            "You don't have permission to access the requested URL.");
                } else {
                    String path = workingDir + req;
                    File f = new File(path);
                    if (f.isDirectory()) {
                        f = new File(path);
                        String html = displayService.printHtml(f);
                        pout.print("HTTP/1.1 200 OK\r\n\r\n" + html);
                    } else {
                        try {
                            downloaderService.downloadFile(f, pout, out); // send raw file
                            logRequest(socket, "200 OK");
                        } catch (FileNotFoundException e) {
                            pout = new PrintStream(out);
                            printError(pout, socket, "404", "Not Found",
                                    "The requested URL was not found on this server.");
                        }
                    }

                }
            }
            out.flush();
        } catch (IOException e) {
            System.err.println(e);
        }
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private boolean isNotValidGetRequest(String request) {
        return !request.startsWith("GET") || request.length() < 14 ||
                !(request.endsWith("HTTP/1.0") || request.endsWith("HTTP/1.1"));
    }


    private static void logRequest(Socket connection, String msg) {
        System.err.println(new Date() + " [" + connection.getInetAddress() + ":" + connection.getPort() + "] " + msg);
    }


    private static void printError(PrintStream pout, Socket connection,
                                   String code, String title, String msg) {
        pout.print("HTTP/1.1 " + code + " " + title + "\r\n\r\n" +
                "<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\r\n" +
                "<TITLE>" + code + " " + title + "</TITLE>\r\n" +
                "</HEAD><BODY>\r\n" +
                "<H1>" + title + "</H1>\r\n" + msg + "<P>\r\n" +
                "<HR><ADDRESS>Simple File Server App 1.1" +
                "</BODY></HTML>\r\n");

        logRequest(connection, code + " " + title);
    }


}
