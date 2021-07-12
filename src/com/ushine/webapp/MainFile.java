package com.ushine.webapp;

import java.io.File;

public class MainFile {
    public static void main(String[] args) {
        File pack = new File(".\\src\\com\\ushine\\webapp");
        checkFile(pack, "\t");

    }

    private static void checkFile(File file, String s) {
        System.out.println(s + file.getName());
        File[] files = file.listFiles();
        if (file.isDirectory() && files!=null) {
            for (File f : files) {
                checkFile(f, s + "\t");
            }
        }
    }
}



