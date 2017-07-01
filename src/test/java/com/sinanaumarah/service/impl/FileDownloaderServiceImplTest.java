package com.sinanaumarah.service.impl;

import com.sinanaumarah.service.api.FileDownloaderService;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


public class FileDownloaderServiceImplTest {
    private final ByteArrayOutputStream byteArrayOutputStream = mock(ByteArrayOutputStream.class);
    private final PrintStream printStream = mock(PrintStream.class);

    @Test
    public void should_writeFileContentToOutputStream() throws Exception {
        FileDownloaderService service = new FileDownloaderServiceImpl();
        service.downloadFile(new File("src/test/resources/dummyDir/testFile.txt"), printStream, byteArrayOutputStream);
        verify(byteArrayOutputStream, atLeast(1)).write(any(byte[].class), any(int.class), any(int.class));
    }


    @Test(expected = FileNotFoundException.class)
    public void should_throwException_when_fileNotFound() throws Exception {
        FileDownloaderService service = new FileDownloaderServiceImpl();
        service.downloadFile(new File("src/test/resources/dummyDir/not there!"), printStream, byteArrayOutputStream);
    }

}