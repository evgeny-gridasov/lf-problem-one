package com.kata.problem.one;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Parser {

    private static Logger logger = Logger.getLogger(Parser.class.getName());

    private static char[] RFC_4180_CSV_NEWLINE = new char[]{'\r', '\n'};

    private ObjectMapper objectMapper;
    private Specification specification;
    private int fixedWidth;

    public Parser(File spec) throws IOException {
        if (spec.exists() && spec.canRead()) {
            objectMapper = new ObjectMapper();
            specification = objectMapper.readValue(spec, Specification.class);
            if (specification.getOffsets().size() != specification.getColumnNames().size()) {
                throw new IllegalArgumentException("The number of column names must be same as number of offsets.");
            }
            fixedWidth = specification.getOffsets().stream().collect(Collectors.summingInt(it -> it));
            logger.info(specification.toString());
        } else {
            throw new IllegalArgumentException("Spec file " + spec.getAbsolutePath() + " does not exist or cannot be read");
        }
    }

    /**
     * Convert fixed width file to csv
     *
     * @param input  input file
     * @param output output file, must exist
     */
    public void convertFixedWidthToCsv(File input, File output) throws Exception {
        if (input.exists() && input.canRead()) {
            truncateOrCreateFile(output);
            try (InputStream inputStream = Files.newInputStream(input.toPath());
                 OutputStream outputStream = Files.newOutputStream(output.toPath())) {
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(outputStream, Charset.forName(specification.getDelimitedEncoding())));
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream, Charset.forName(specification.getFixedWidthEncoding())));
                writeCsvHeader(writer);
                writeCsvContent(reader, writer);
                reader.close();
                writer.close();
            }
            logger.info("Converted fixed width file " + input.getAbsolutePath() + " to CSV file " + output.getAbsolutePath());
        } else {
            logger.severe("Input file " + input.getAbsolutePath() + " does not exist or cannot be read");
        }
    }

    private void writeCsvHeader(Writer writer) throws IOException {
        if (specification.isIncludeHeader()) {
            String header = specification.getColumnNames().stream().collect(Collectors.joining(","));
            writer.write(header);
            writer.write(RFC_4180_CSV_NEWLINE);
        }
    }

    private void writeCsvContent(BufferedReader reader, Writer writer) throws IOException {
        int numberOfFields = specification.getOffsets().size();
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            checkLineLength(line);
            for (int i = 0, pos = 0; i < numberOfFields; i++) {
                if (i > 0) {
                    writer.write(',');
                }

                writer.write('"');

                // write field value, escape " with ""
                Integer fieldLength = specification.getOffsets().get(i);
                int endIndex = pos + fieldLength;
                for (int k = pos; k < endIndex; k++) {
                    char c = line.charAt(k);
                    if (c == '"') {
                        writer.write('"');
                        writer.write('"');
                    } else {
                        writer.write(c);
                    }
                }
                pos = endIndex;

                writer.write('"');
            }
            writer.write(RFC_4180_CSV_NEWLINE);
        }
    }

    private void checkLineLength(String line) {
        int length = line.length();
        if (length > fixedWidth) {
            throw new IllegalArgumentException("Line is too long: " + line);
        } else if (length < fixedWidth) {
            throw new IllegalArgumentException("Line is too short: " + line);
        }
    }

    /**
     * Generates fixed width file
     *
     * @param output  output file
     * @param records how many records to create
     */
    public void generateFixedWidthFile(File output, long records) throws Exception {
        truncateOrCreateFile(output);
        try (FileOutputStream fileOutputStream = new FileOutputStream(output)) {
            List<Character> printable = getPrintable(specification.getFixedWidthEncoding());
            char buf[] = new char[fixedWidth + 1];
            buf[fixedWidth] = '\n';
            Random random = new Random(); // Pseudo random should be fine

            for (int i = 0; i < records; i++) {
                for (int j = 0, pos = 0; j < specification.getOffsets().size(); j++) {
                    for (int k = 0; k < specification.getOffsets().get(j); k++, pos++) {
                        buf[pos] = printable.get(random.nextInt(printable.size()));
                    }
                }
                fileOutputStream.write(new String(buf).getBytes(specification.getFixedWidthEncoding()));
            }
            logger.info("Generated " + records + " records to " + output.getAbsolutePath());
        }
    }

    private void truncateOrCreateFile(File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        } else {
            Files.write(file.toPath(), new byte[]{}, StandardOpenOption.TRUNCATE_EXISTING);
        }
    }

    /**
     * Get all printable characters of a charset
     */
    private List<Character> getPrintable(String charset) {
        List<Character> chars = new ArrayList<>();
        CharsetEncoder charsetEncoder = Charset.forName(charset).newEncoder();
        for (char c = '0'; c < '\uffff'; c++) {
            if (charsetEncoder.canEncode(c) && (Character.isAlphabetic(c) || Character.isDigit(c))) {
                chars.add(Character.valueOf(c));
            }
        }
        return chars;
    }
}
