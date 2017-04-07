/*
 * Copyright (c) 2017 Harm Brugge [harmbrugge@gmail.com].
 * All rights reserved.
 */
package com.harmbrugge.bamtools;

import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.SamReader;
import htsjdk.samtools.SamReaderFactory;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author Harm Brugge
 * @version 0.0.1
 */
public class BamProcessor {

    private SamReader samReader;
    private HashMap<String, SingleCell> cells;

    public BamProcessor(Path pathToBamFile, Path pathToBarcodeFile) throws IOException {
        this.samReader = SamReaderFactory.makeDefault().open(pathToBamFile.toFile());
        cells = new HashMap<>();
        populateKeys(pathToBarcodeFile);
    }

    public void read() throws IOException {
        int i = 0;
        for (SAMRecord samRecord : samReader) {
            String barcode = samRecord.getStringAttribute("CB");

            if (cells.containsKey(barcode)) {
                SingleCell cell = cells.get(barcode);
                cell.addRead(samRecord);
            }
            i++;
            if (i % 1e7 == 0) {
                System.out.println(i);
            }
        }

    }

    public void writeOutput(String outputPath) throws IOException {
        File outputFile = new File(outputPath);
        if (!outputFile.exists()) outputFile.createNewFile();

        FileWriter fileWriter = new FileWriter(outputFile.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fileWriter);

        bw.write("cell\tx\ty\ttotal\n");

        for (Map.Entry<String, SingleCell> entry : cells.entrySet()) {
            bw.write(entry.getKey() + "\t");
            SingleCell cell = entry.getValue();
            bw.write(cell.getXCount() + "\t" + cell.getYCount() + "\t" + cell.getTotalCount() + "\n");
        }

        bw.close();
    }

    private void populateKeys(Path pathToBarcodeFile) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(pathToBarcodeFile.toFile()))) {
            String barcode = br.readLine();
            while (barcode != null) {
                cells.put(barcode, new SingleCell(barcode));
                barcode = br.readLine();
            }
        }
    }

    public String withinHammingDistance(String barcode) {

        Set<String> equals = new HashSet<>(cells.keySet());
        Set<String> oneHammings = new HashSet<>();

        for (int i = 0; i < barcode.length(); i++) {
            if (equals.isEmpty() && oneHammings.isEmpty()) return null;

            // First check reads within one hamming distance are still equal, else pop
            for (Iterator<String> oneHamming = oneHammings.iterator(); oneHamming.hasNext();) {
                String element = oneHamming.next();
                if (element.charAt(i) != barcode.charAt(i)) oneHamming.remove();
            }
            // Check if equals are still equal, else move to oneHamming set
            for (Iterator<String> equal = equals.iterator(); equal.hasNext();) {
                String element = equal.next();
                if (element.charAt(i) != barcode.charAt(i)) {
                    equal.remove();
                    oneHammings.add(element);
                }
            }
            String foo = "bar";
        }

        if (oneHammings.size() > 0) return oneHammings.iterator().next();
        return null;
    }

}
