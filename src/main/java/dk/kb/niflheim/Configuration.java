package dk.kb.niflheim;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVReader;

/**
 * Configuration for Niflheim.
 */
public class Configuration {
    /** The logger.*/
    private static final Logger log = LoggerFactory.getLogger(Configuration.class);

    /** The array index of the first element in a row (the name of a node) of the CSV configuration file.*/
    private final int nodeIndex = 0;
    
    /**  The array index of the second element in a row (the name of a attribute) of the CSV configuration file.*/
    private final int attrIndex = 1;

    /**  Entries to be extracted as defined in the CSV configuration file.
     */
    private final List<String[]> confEntries;
    
    /**
     * Constructor.
     * @param confEntries The CSV entries for the configuration.
     * @throws IOException If the output directory does not exist and cannot be created.
     */
    public Configuration(List<String[]> confEntries) throws IOException {
        this.confEntries = confEntries;
    }
    
    /** The get method for nodeIndex.
     * @return nodeIndex.
     */
    public int getNodeIndex() {
        return nodeIndex;
    }
    
    /** The get method for attrIndex.
    * @return attrIndex.
    */
    public int getAttrIndex() {
        return attrIndex;
    }

    /** The get method for confEntries.
    * @return confEntries.
    */
    public List<String[]>  getConfEntries() {
        return confEntries;
    }

    /**
     * Creates a Configuration based on the configuration file.
     * @param confPath The path of the CSV file with the configuration.
     * @return The configuration.
     * @throws IOException If it fails to load, or the configured elements cannot be instantiated.
     */
    public static Configuration createFromCSVFile(final String confPath) 
            throws IOException {
        log.debug("Loading configuration from file " + confPath);

        FileInputStream fis = new FileInputStream(confPath); 
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        CSVReader reader = new CSVReader(isr);
        List<String[]> confEntries = reader.readAll();
        reader.close();
        isr.close();
        fis.close();
        
        for (String[] row : confEntries) {
            log.debug(Arrays.toString(row));
        }
        
        return new Configuration(confEntries);
    }
}
