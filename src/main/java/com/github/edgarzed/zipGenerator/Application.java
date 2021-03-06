package com.github.edgarzed.zipGenerator;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Application {

    public static void main(String[] args) throws Exception {
        Scanner consoleIn = new Scanner(System.in);
        String pathParam;
        long sizeParam;
        int amountParam;
        boolean ackParam;

        if (args.length < 1) {
            System.out.println("Enter directory to save in:");
            pathParam = consoleIn.nextLine();
        } else {
            pathParam = args[0];
        }
        if (args.length < 2) {
            System.out.println("Enter content size (Kbyte):");
            sizeParam = consoleIn.nextLong();
        } else {
            sizeParam = Long.parseLong(args[1]);
        }
        if (args.length < 3) {
            System.out.println("Enter number of files:");
            amountParam = consoleIn.nextInt();
        } else {
            amountParam = Integer.parseInt(args[2]);
        }
        if (args.length < 4) {
            System.out.println("Need .ack ? (y/n)");
            String ackLine = consoleIn.next();
            ackParam = "y".equals(ackLine);
        } else {
            ackParam = Boolean.parseBoolean(args[3]);
        }

        byte[] oneKB = new byte[1024];
        new Random().nextBytes(oneKB);

        DecimalFormat decimalFormat = new DecimalFormat("#.####", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        String sizeMb = decimalFormat.format(sizeParam / 1024.0);

        LocalDateTime dateTime = LocalDateTime.now();
        String dateTimeFormatted = dateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss_"));

        if (Files.notExists(Paths.get(pathParam))) {
            Files.createDirectories(Paths.get(pathParam));
            System.out.println("Directory created");
        }

        System.out.println("Creating " + amountParam + " zip files in " + pathParam + " with content size (Mbyte) = " + sizeMb + (ackParam ? " with .ack" : "") + "\n");
        long start = System.currentTimeMillis();
        for (int i = 0; i < amountParam; i++) {
            String fileName = dateTimeFormatted + sizeMb + "_" + i + ".zip";

            Path tempFile = Files.createFile(Paths.get(pathParam, fileName));
            try (ZipOutputStream zOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(tempFile.toFile())))) {
                zOut.setLevel(0);

                long sizeRest = sizeParam;

                for (int j = 0; sizeRest > 0; sizeRest -= 1024, j++) {
                    ZipEntry zipEntry = new ZipEntry(j + ".txt");
                    zOut.putNextEntry(zipEntry);

                    long entrySize = sizeRest >= 1024 ? 1024 : sizeRest;
                    for (int k = 0; k < entrySize; k++) {
                        zOut.write(oneKB);
                    }

                    zOut.closeEntry();
                }
            }

            System.out.println(fileName + " created. " + (i + 1) + "/" + amountParam);

            if (ackParam) {
                Files.createFile(Paths.get(pathParam, fileName + ".ack"));
                System.out.println("~.ack created.");
            }
        }

        long time = (System.currentTimeMillis() - start) / 1000;

        System.out.println("\nDone in " + time + "sec :)");
    }
}
