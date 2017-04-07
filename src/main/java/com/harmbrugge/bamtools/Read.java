/*
 * Copyright (c) 2017 Harm Brugge [harmbrugge@gmail.com].
 * All rights reserved.
 */
package com.harmbrugge.bamtools;

import htsjdk.samtools.SAMRecord;

/**
 * @author Harm Brugge
 * @version 0.0.1
 */
public class Read {

    private String umi;
    private int duplicates;

    public Read(String umi) {
        this.umi = umi;
    }

    public Read(SAMRecord samRecord) {
        this.umi = samRecord.getStringAttribute("UR");
    }

    public String getUmi() {
        return umi;
    }

    public void addDuplicate() {
        duplicates++;
    }

    public int getDuplicates() {
        return duplicates;
    }
}
