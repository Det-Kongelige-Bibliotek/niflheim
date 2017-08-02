package dk.kb.niflheim;

import java.io.File;
import java.io.IOException;

import org.jaccept.structure.ExtendedTestCase;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ConfigurationTest extends ExtendedTestCase {

    @Test
    public void testLoadingFromFile() throws IOException {
        /** The configuration file for testing.*/
        File testConfFile = new File("src/test/resources/conf/niflheim.csv");

        Configuration conf = Configuration.createFromCSVFile(testConfFile.getAbsolutePath());
        Assert.assertNotNull(conf);
        Assert.assertNotNull(conf.getConfEntries());

        int nodeIndex = conf.getNodeIndex();
        Assert.assertNotNull(nodeIndex);
        Assert.assertEquals(nodeIndex, 0);
        
        int attrIndex = conf.getAttrIndex();
        Assert.assertNotNull(attrIndex);
        Assert.assertEquals(attrIndex, 1);
    }
}
