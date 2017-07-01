package com.sinanaumarah.service.impl;

import com.sinanaumarah.service.api.DisplayService;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.NotDirectoryException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class FileExplorerDisplayServiceImpl implements DisplayService {

    private final String workingDir;

    public FileExplorerDisplayServiceImpl(String workingDir) {
        this.workingDir = workingDir;
    }

    @Override
    public String printHtml(File dir) throws NotDirectoryException {
        return getFileExplorerAsHtmlTable(dir);
    }

    private String getFileExplorerAsHtmlTable(File dir) throws NotDirectoryException {

        if (dir == null || !dir.isDirectory()) {
            throw new NotDirectoryException("Requested file is not a directory!");
        }

        File[] entries = dir.listFiles();

        StringWriter out = new StringWriter();
        PrintWriter tableWriter = new PrintWriter(out);

        String relativePath = dir.getAbsolutePath().replace(workingDir, "");
        tableWriter.println("<H1> Index of " + relativePath + "</H1>");

        String parent = relativePath.substring(0, relativePath.lastIndexOf(File.separator) + 1);

        if (entries != null && entries.length > 0) {
            tableWriter.print("<TABLE style='table-layout:fixed;'>");
            tableWriter.print("<TH>Name</TH><TH>Size</TH><TH>Last modified</TH>");
            if (!parent.isEmpty()) {
                tableWriter.println("<TR><TD><A HREF=\"" + parent + "\">Up to parent directory</A></TD></TR>");
            }
            for (File f : entries) {
                String name = f.isDirectory() ? f.getName() + "/" : f.getName();
                tableWriter.println("<TR><TD><a href=\"" + name + "\">" + name + "</a></TD>");
                tableWriter.println("<TD>" + f.length() + "</TD>");
                tableWriter.println("<TD>" + dateFormat(f.lastModified()) + "</TD></TR>");
            }
            tableWriter.println("</TABLE>");
        } else {
            tableWriter.print("<pre>Empty folder</pre>");
        }

        tableWriter.close();
        return out.toString();
    }

    private static String dateFormat(Long longValue) {
        LocalDateTime date =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(longValue), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm");
        return date.format(formatter);
    }
}
