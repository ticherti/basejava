package com.ushine.webapp;

import java.io.File;
import java.util.Objects;

public class MainFile {
    public static void main(String[] args) {
        File pack = new File(".\\src\\com\\ushine\\webapp");
        checkFile(pack);

    }

    private static void checkFile(File file) {
        for (File f : Objects.requireNonNull(file.listFiles())
        ) {
            if (f.isDirectory()) {
                checkFile(f);
            } else {
                System.out.println(f.getName());
            }
        }
    }
}


