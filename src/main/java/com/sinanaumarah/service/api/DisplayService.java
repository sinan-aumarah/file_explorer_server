package com.sinanaumarah.service.api;


import java.io.File;
import java.nio.file.FileSystemException;

public interface DisplayService {
    String printHtml(File currentDir) throws FileSystemException;
}
