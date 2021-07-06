package com.ushine.webapp;

import java.io.File;
import java.util.Objects;

public class MainFile {
    public static void main(String[] args) {
        File pack = new File(".\\src\\com\\ushine\\webapp");
        checkFile(pack, "\t");

    }

    private static void checkFile(File file, String s) {
        System.out.println(s + file.getName());
        if (file.isDirectory()) {
            for (File f : Objects.requireNonNull(file.listFiles())) {
                checkFile(f, s + "\t");
            }
        }
    }
}



