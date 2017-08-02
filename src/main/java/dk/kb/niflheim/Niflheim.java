package dk.kb.niflheim;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The main class of Niflheim.
 * Given a directory, that and it's sub-directories are traversed and
 * the files within are processed, if they have a given suffix.

 */
public class Niflheim {
    /** The logger.*/
    private static final Logger log = LoggerFactory.getLogger(Niflheim.class);
    
    /**
     * Main method, check the arguments and initialize the configurations.
     * Requires at least four arguments; the configuration file, the path to the starting directory,
     * the path to the output file, and a suffix for the files to be processed.
     * @param args The main method arguments.
     * @throws IOException If the directory or files in the arguments does not exist.
     */
    public static void main(final String ... args) throws IOException {
        if (args.length < 4) {
            System.err.println("Needs at least four arguments: the configuration file, "
                    + "the path to the starting directory, the path to the output file, "
                    + "and a suffix for the files to be processed.");
            System.exit(-1);
        }
        
        String confPath = args[0];
        File confFile = new File(confPath); 
        if (!confFile.isFile()) {
            System.err.println("The configuration file " + confFile.getAbsolutePath() 
                    + " is not a valid file.");
            System.exit(-1);
        }
        log.debug("[ARG1] Using configuration file: " + confPath);
        
        String startDirPath = args[1];
        File startDir = new File(startDirPath); 
        if (!startDir.isDirectory()) {
            System.err.println("The starting directory " + startDir.getAbsolutePath() 
                    + " is not a valid directory.");
            System.exit(-1);
        }
        log.debug("[ARG2] Using " + startDirPath + " as starting directory");
        
        String outputCsvFilePath = args[2];
        File outputCsvFile = new File(outputCsvFilePath); 
        if (outputCsvFile.isDirectory()) {
            System.err.println("The output file "  + outputCsvFile.getAbsolutePath() + " is a directory.");
            System.exit(-1);
        }
        log.debug("[ARG3] Using output file: " + outputCsvFilePath);
        
        String suffix = args[3];
        if (suffix.isEmpty()) {
            System.err.println("A suffix for the files to be " + "processed has to be given.");
            System.exit(-1);
        }
        log.debug("[ARG4] Using suffix: " + suffix);
        
        try {
            Configuration conf = Configuration.createFromCSVFile(confPath);
            new BuildCSV(startDirPath, outputCsvFilePath, suffix, conf);
        } catch (IOException e) {
            log.error("Failure in loading configuration or in building CSV!", e);
            System.exit(-1);
        }
    }
}
