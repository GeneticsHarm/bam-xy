/*
 * Copyright (c) 2017 Harm Brugge [harmbrugge@gmail.com].
 * All rights reserved.
 */
package com.harmbrugge.bamtools;


import htsjdk.samtools.SAMRecord;

import java.util.HashMap;

/**
 * @author Harm Brugge
 * @version 0.0.1
 */
public class SingleCell {

    private String barcode;

    /**
     * Key is the ensemble id;
     */
    private HashMap<String, Gene> genes;

    private int xCount;
    private int yCount;
    private int totalCount;

    public SingleCell(String barcode) {
        this.barcode = barcode;
        this.genes = new HashMap<>();
    }

    public String getBarcode() {
        return barcode;
    }

    public void addRead(SAMRecord samRecord) {

        String mappedGene = getMappedGene(samRecord);

        if (mappedGene != null) {
            Read read = new Read(samRecord);
            if (genes.containsKey(mappedGene)) {
                Gene gene = genes.get(mappedGene);
                if (gene.addRead(read)) {
                    totalCount++;
                    if (!gene.isAutosomal()) setYorX(gene);
                }
            } else {
                Gene gene = new Gene(mappedGene, read);
                genes.put(mappedGene, gene);
                totalCount++;
                if (!gene.isAutosomal()) setYorX(gene);
            }
        }
    }

    private void setYorX(Gene gene) {
        if (gene.isY()) yCount++;
        else if (gene.isX()) xCount++;
    }

    private String getMappedGene(SAMRecord samRecord) {
        Object gxTag = samRecord.getAttribute("GX");
        if (gxTag != null) {
            String[] mappedGenes = String.valueOf(gxTag).split(";");
            if (mappedGenes.length == 1) return mappedGenes[0];
        }
        return null;
    }

    public int getXCount() {
        return xCount;
    }

    public int getYCount() {
        return yCount;
    }

    public int getTotalCount() {
        return totalCount;
    }
}
