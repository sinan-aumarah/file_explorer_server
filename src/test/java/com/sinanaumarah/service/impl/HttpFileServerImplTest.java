package com.sinanaumarah.service.impl;

import com.sinanaumarah.service.api.DisplayService;
import com.sinanaumarah.service.api.FileDownloaderService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static org.mockito.Mockito.*;

public class HttpFileServerImplTest {
    private final ServerSocket serverSocket = mock(ServerSocket.class);
    private final Socket socket = mock(Socket.class);
    private final DisplayService displayService = mock(DisplayService.class);
    private final FileDownloaderService fileDownloaderService = mock(FileDownloaderService.class);
    private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private final InputStream inputStream = new ByteArrayInputStream("GET / HTTP/1.1".getBytes());
    private final String workingDir = System.getProperty("user.dir") + "/src/test/resources/dummyDir";
    private final String dummyFile = workingDir + "/testFile.txt";

    @Before
    public void setUp() throws Exception {
        when(serverSocket.accept()).thenReturn(socket);
        when(socket.getOutputStream()).thenReturn(byteArrayOutputStream);
        when(socket.getInputStream()).thenReturn(inputStream);
    }

    @Test
    public void should_startHttpServer_and_listensToRequest() throws IOException {
        HttpFileServerImpl server = new HttpFileServerImpl(1, "/", displayService, fileDownloaderService);
        server.listenAndAct(serverSocket);
        verify(serverSocket, atLeastOnce()).accept();
    }

    @Test
    public void should_callDisplayService_when_validHttpGetIsRequestingFolder() throws IOException {
        HttpFileServerImpl server = new HttpFileServerImpl(1, workingDir, displayService, fileDownloaderService);
        server.listenAndAct(serverSocket);
        verify(displayService).printHtml(any(File.class));
    }

    @Test
    public void should_sendFileInOutputStream_when_validHttpGetIsRequestingFile() throws IOException {
        HttpFileServerImpl server = new HttpFileServerImpl(1, dummyFile, displayService, fileDownloaderService);
        server.listenAndAct(serverSocket);
        verify(fileDownloaderService).downloadFile(any(File.class), any(PrintStream.class), any(BufferedOutputStream.class));
    }

    @After
    public void validate() {
        validateMockitoUsage();
    }

}