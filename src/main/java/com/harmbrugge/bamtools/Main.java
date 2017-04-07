/*
 * Copyright (c) 2016 Harm Brugge [harmbrugge@gmail.com].
 * All rights reserved.
 */
package com.harmbrugge.bamtools;

import org.apache.commons.cli.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Main class for parsing the commandline arguments and initializing BamProcessor
 *
 * @author Harm Brugge
 * @version 0.0.1
 */
public class Main {

    private void start(String[] args) {

        String pathToBarcode;
        String pathToBam;
        String outputPath;

        CommandLineParser parser = new DefaultParser();

        Options options = new Options();
        options.addOption("i", true, "Input file BAM file or dir");
        options.addOption("o", true, "Output folder");
        options.addOption("b", true, "Input file barcode file");

        try {
            CommandLine line = parser.parse(options, args);
            if(!line.hasOption("i")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("Bam tools", options);
                System.exit(1);
            }

            pathToBam = line.getOptionValue("i");
            pathToBarcode = line.getOptionValue("b");
            outputPath = line.getOptionValue("o");

            BamProcessor bamProcessor = new BamProcessor(Paths.get(pathToBam), Paths.get(pathToBarcode));
            bamProcessor.read();

            bamProcessor.writeOutput(outputPath);
        }
        catch (ParseException exp) {
            System.out.println("Parse exception:" + exp.getMessage());
        } catch (IOException e) {
            System.out.println("IO exception");
        }

    }

    public static void main(String [] args) {
        Main main = new Main();
        main.start(args);
    }


}
