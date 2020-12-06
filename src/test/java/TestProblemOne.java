import com.kata.problem.one.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

public class TestProblemOne {

    @Test
    public void testGenerate() throws Exception {
        Parser parser = new Parser(getClasspathFile("spec.json"));
        File tempFile = File.createTempFile("gen", ".data");
        tempFile.deleteOnExit();

        parser.generateFixedWidthFile(tempFile, 100);

        String string = Files.readString(tempFile.toPath(), Charset.forName("windows-1252"));
        byte[] bytes = Files.readAllBytes(tempFile.toPath());

        Assert.assertEquals(bytes.length, 100 * (5 + 12 + 3 + 1)); // 100 times of 3 columns plus \n
        Assert.assertEquals(string.length(), bytes.length);
        Assert.assertTrue(string.getBytes().length > bytes.length);  // UTF8 length must not be equal to cp1252 length
    }

    @Test
    public void testConvertToCsv() throws Exception {
        Parser parser = new Parser(getClasspathFile("spec.json"));
        File tempFile = File.createTempFile("gen", ".csv");
        tempFile.deleteOnExit();
        File testSet = getClasspathFile("testset_cp1252.txt");
        parser.convertFixedWidthToCsv(testSet, tempFile);

        List<String> csvOutput = Files.readAllLines(tempFile.toPath());
        Assert.assertEquals(4, csvOutput.size());
        Assert.assertEquals("f1,f2,f3", csvOutput.get(0));
        Assert.assertEquals("\"12345\",\"1234567890AB\",\"123\"", csvOutput.get(1));
        Assert.assertEquals("\"âþÇÐE\",\"9876543210AB\",\"***\"", csvOutput.get(2));
        Assert.assertEquals("\"1\"\"234\",\"\"\"Hello CSV!\"\"\",\"\"\"\"\"\"\"\"", csvOutput.get(3));
    }

    @Test
    public void testConvertToCsvNoHeader() throws Exception {
        Parser parser = new Parser(getClasspathFile("spec_noheader.json"));
        File tempFile = File.createTempFile("gen", ".csv");
        tempFile.deleteOnExit();
        File testSet = getClasspathFile("testset_cp1252.txt");
        parser.convertFixedWidthToCsv(testSet, tempFile);

        List<String> csvOutput = Files.readAllLines(tempFile.toPath());
        Assert.assertEquals(3, csvOutput.size());
        Assert.assertEquals("\"12345\",\"1234567890AB\",\"123\"", csvOutput.get(0));
        Assert.assertEquals("\"âþÇÐE\",\"9876543210AB\",\"***\"", csvOutput.get(1));
        Assert.assertEquals("\"1\"\"234\",\"\"\"Hello CSV!\"\"\",\"\"\"\"\"\"\"\"", csvOutput.get(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTooShort() throws Exception {
        Parser parser = new Parser(getClasspathFile("spec.json"));
        File tempFile = File.createTempFile("gen", ".csv");
        tempFile.deleteOnExit();
        File testSet = getClasspathFile("testset_too_short_cp1252.txt");
        parser.convertFixedWidthToCsv(testSet, tempFile);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTooLong() throws Exception {
        Parser parser = new Parser(getClasspathFile("spec.json"));
        File tempFile = File.createTempFile("gen", ".csv");
        tempFile.deleteOnExit();
        File testSet = getClasspathFile("testset_too_long_cp1252.txt");
        parser.convertFixedWidthToCsv(testSet, tempFile);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadSpec() throws Exception {
        new Parser(getClasspathFile("spec_bad.json"));
    }

    private File getClasspathFile(String fileName) throws IOException
    {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);

        if (resource == null) {
            throw new IllegalArgumentException("file not found!");
        } else {
            return new File(resource.getFile());
        }
    }
}
