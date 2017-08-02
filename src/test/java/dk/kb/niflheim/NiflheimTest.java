package dk.kb.niflheim;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Permission;

import org.jaccept.structure.ExtendedTestCase;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import dk.kb.niflheim.testutils.TestFileUtils;

public class NiflheimTest extends ExtendedTestCase {
    /** The configuration file for testing.*/
    private File testConfFile = new File("src/test/resources/conf/niflheim.csv");

    /** The path to the starting directory.*/
    private Path startDirPath = Paths.get("src/test/resources/xml/");
    
    /** The starting directory as sting (for argument use).*/
    private String startDir = startDirPath.toString();

    /** The expected output CSV file.*/
    private File expectedOutputCsvFile = 
            new File("src/test/resources/csv/output_expected.csv");

    /** The output CSV file.*/
    private File outputCsvFile = new File("src/test/resources/csv/output.csv");
    
    /** The output CSV file as string (for argument use).*/
    private String outputCsv = outputCsvFile.toString();
    
    /** The suffix of the files to be processed.*/
    private String suffix = ".fits.xml";

    @BeforeMethod
    public void setup() throws IOException {
        Assert.assertTrue(testConfFile.isFile());
    }
    
    @Test
    public void testInstantiation() {
        Niflheim v = new Niflheim();
        Assert.assertNotNull(v);
    }

    @Test
    public void testConfigRun() throws IOException {
        try {
            Niflheim.main(testConfFile.getAbsolutePath(), startDir, outputCsv, suffix);
        } finally {
            enableSystemExitCall();
        }
    }

    @Test
    public void testRun() throws IOException {
        try {
            Niflheim.main(testConfFile.getAbsolutePath(), startDir, outputCsv, suffix);
            String expOutCsv = TestFileUtils.readFile(expectedOutputCsvFile);
            String outCsv = TestFileUtils.readFile(outputCsvFile);
            Assert.assertEquals(expOutCsv, outCsv);
        } finally {
            enableSystemExitCall();
        }
    }

    @Test(expectedExceptions = ExitTrappedException.class)
    public void testFailureWhenNoArgs() throws IOException {
        forbidSystemExitCall();
        try {
            Niflheim.main(new String[0]);
        } finally {
            enableSystemExitCall();
        }
    }
    @Test(expectedExceptions = ExitTrappedException.class)
    public void testFailureWithBadConfiguration() throws IOException {
        forbidSystemExitCall();
        try {
            Niflheim.main(testConfFile.getAbsolutePath() + "1", startDir, outputCsv, suffix);
        } finally {
            enableSystemExitCall();
        }
    }
    
    @Test(expectedExceptions = ExitTrappedException.class)
    public void testFailureWithBadStartingDirctory() throws IOException {
        forbidSystemExitCall();
        try {
            Niflheim.main(testConfFile.getAbsolutePath(), startDir + "1", outputCsv, suffix);
        } finally {
            enableSystemExitCall();
        }
    }
    
    @Test(expectedExceptions = ExitTrappedException.class)
    public void testFailureWithBadOutputCsv() throws IOException {
        forbidSystemExitCall();
        try {
            Niflheim.main(testConfFile.getAbsolutePath(), startDir, "1" + outputCsv, suffix);
        } finally {
            enableSystemExitCall();
        }
    }
    
    @Test(expectedExceptions = ExitTrappedException.class)
    public void testFailureWithNoSuffix() throws IOException {
        forbidSystemExitCall();
        try {
            Niflheim.main(testConfFile.getAbsolutePath(), startDir, outputCsv, "");
        } finally {
            enableSystemExitCall();
        }
    }
    
    private static class ExitTrappedException extends SecurityException { }
    private static void forbidSystemExitCall() {
        final SecurityManager securityManager = new SecurityManager() {
            public void checkPermission(Permission permission) {
                if (permission.getName().startsWith("exitVM")) {
                    throw new ExitTrappedException();
                }
            }
        };
        System.setSecurityManager(securityManager);
        System.out.println("Exit disabled");
    }

    private static void enableSystemExitCall() {
        System.setSecurityManager(null);
    }
}
