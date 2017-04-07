/*
 * Copyright (c) 2017 Harm Brugge [harmbrugge@gmail.com].
 * All rights reserved.
 */
package com.harmbrugge.bamtools;

import java.util.HashMap;

/**
 * @author Harm Brugge
 * @version 0.0.1
 */
public class Gene {

    private String id;
    private boolean isX;
    private boolean isY;

    /**
     * Key: UMI
     */
    private HashMap<String, Read> reads;

    public Gene(String id) {
        this.id = id;
        this.reads = new HashMap<>();
        if (ChromoUtil.isY(id)) {
            isY = true;
        }
        else if (ChromoUtil.isX(id)) isX = true;
    }

    public Gene(String id, Read read) {
        this(id);
        reads.put(read.getUmi(), read);
    }

    /**
     *
     * @param read
     * @return true if read is not PCR duplicate
     */
    public boolean addRead(Read read) {
        if (!reads.containsKey(read.getUmi())) {
            reads.put(read.getUmi(), read);
            return true;
        } else {
            Read existingRead = reads.get(read.getUmi());
            existingRead.addDuplicate();
            return false;
        }
    }


    public String getId() {
        return id;
    }

    public HashMap<String, Read> getReads() {
        return reads;
    }

    public boolean isX() {
        return isX;
    }

    public boolean isY() {
        return isY;
    }

    public boolean isAutosomal() {
        return !isX() && !isY();
    }
}
