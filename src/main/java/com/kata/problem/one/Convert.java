package com.kata.problem.one;

import java.io.File;

public class Convert {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Please provide spec.json, input file name and output file name");
            System.exit(1);
        }
        try {
            File spec = new File(args[0]);
            File input = new File(args[1]);
            File output = new File(args[2]);
            Parser parser = new Parser(spec);
            parser.convertFixedWidthToCsv(input, output);
        } catch (Exception e) {
            System.err.println("Error converting to CSV:");
            e.printStackTrace();
            System.exit(2);
        }
        System.exit(0);
    }
}
