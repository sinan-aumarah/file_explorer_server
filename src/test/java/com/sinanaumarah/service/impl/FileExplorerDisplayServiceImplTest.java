package com.sinanaumarah.service.impl;

import com.sinanaumarah.service.api.DisplayService;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.nio.file.NotDirectoryException;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;


public class FileExplorerDisplayServiceImplTest {

    @Test
    public void should_return_htmlTableWithIndexOfHeaderContainingCurrentDirectoryPath() throws Exception {
        final String testDir = "/c/dummyDir/";
        DisplayService service = new FileExplorerDisplayServiceImpl(".");
        File dir = Mockito.mock(File.class);
        when(dir.getAbsolutePath()).thenReturn(testDir);
        when(dir.isDirectory()).thenReturn(true);
        String result = service.printHtml(dir);
        assertTrue(result.contains("Index of " + testDir));
    }


    @Test(expected = NotDirectoryException.class)
    public void should_throwException_when_fileIsNotDirectory() throws Exception {
        DisplayService service = new FileExplorerDisplayServiceImpl(".");
        File dir = Mockito.mock(File.class);
        when(dir.isDirectory()).thenReturn(false);
        service.printHtml(dir);
    }

    @Test(expected = NotDirectoryException.class)
    public void should_throwException_when_fileIsNull() throws Exception {
        DisplayService service = new FileExplorerDisplayServiceImpl(".");
        File dir = Mockito.mock(File.class);
        when(dir.isDirectory()).thenReturn(false);
        service.printHtml(dir);
    }

    @Test
    public void should_return_htmlTableWithDirectoryContentAsHrefs() throws Exception {

        DisplayService service = new FileExplorerDisplayServiceImpl(".");
        String mocDir = "dummy folder";
        String mockPicFile = "pic.png";
        String mockTxtFile = "notepad.txt";
        File[] files = {createMockFileWithNameOf(mockPicFile), createMockFileWithNameOf(mockTxtFile), createMockDirectory(mocDir, null)};
        File dir = createMockDirectory(mocDir, files);
        String result = service.printHtml(dir);

        assertTrue(result.contains("<a href=\"" + mockPicFile + "\">" + mockPicFile + "</a>"));
        assertTrue(result.contains("<a href=\"" + mockTxtFile + "\">" + mockTxtFile + "</a>"));
        assertTrue(result.contains("<a href=\"" + mocDir + "/\">" + mocDir + "/</a>"));
    }

    private File createMockDirectory(String name, File[] files) {
        File dir = Mockito.mock(File.class);
        when(dir.getName()).thenReturn(name);
        when(dir.isDirectory()).thenReturn(true);
        when(dir.getAbsolutePath()).thenReturn("/");
        when(dir.listFiles()).thenReturn(files);
        return dir;
    }

    private File createMockFileWithNameOf(String name) {
        File mockFile1 = Mockito.mock(File.class);
        when(mockFile1.getName()).thenReturn(name);
        return mockFile1;
    }


}