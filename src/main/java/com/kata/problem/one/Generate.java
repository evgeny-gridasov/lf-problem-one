package com.kata.problem.one;

import java.io.File;

public class Generate {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Please provide spec.json, output file name and number of records to generate");
            System.exit(1);
        }
        try {
            File spec = new File(args[0]);
            File output = new File(args[1]);
            long records = Long.parseLong(args[2]);
            Parser parser = new Parser(spec);
            parser.generateFixedWidthFile(output, records);
        } catch (Exception e) {
            System.err.println("Error generating fixed width file:");
            e.printStackTrace();
            System.exit(2);
        }
        System.exit(0);
    }
}
