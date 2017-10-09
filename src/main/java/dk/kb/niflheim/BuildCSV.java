package dk.kb.niflheim;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVWriter;

/**
 * Given a directory, that and it's sub-directories are traversed and
 * the files within are processed, if they have a given suffix.
 */
public class BuildCSV {
    /** The logger.*/
    private static final Logger log = LoggerFactory.getLogger(BuildCSV.class);

    /** CSV writer.*/
    private final CSVWriter writer;
    
    /** XML to CVS transformer.*/    
    private final XmlCSV xmlCSV;
    
    /**
     * Constructor. 
     * @param startDirPath The directory to be traversed.
     * @param outputCsvFilePath The path to the output CSV file.
     * @param suffix Suffix for the files to be processed.
     * @param conf The configuration.
     * @throws IOException If it is not possible to read or write to a file. 
     */
    public BuildCSV(String startDirPath, String outputCsvFilePath, String suffix, Configuration conf)
            throws IOException {
        FileOutputStream fos = new FileOutputStream(outputCsvFilePath);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
        this.writer = new CSVWriter(osw);

        this.xmlCSV = new XmlCSV(conf);

        this.buildCsvHeader(conf);
        this.traverseFileTree(startDirPath, suffix);

        writer.close();
        osw.close();
        fos.close();
    }

    /**
     * Add a header row to the CVS output file.
     * @param conf The configuration.
     */
    private void buildCsvHeader(Configuration conf) {
        /** The number of entries defined in the configuration file.*/    
        int cpsize = conf.getConfEntries().size();
        
        /** The header row of the output file.*/  
        String[] header = new String[cpsize];

        /** The array index of the first element in a row (the name of a node) of the CSV configuration file.*/
        final int nodeIndex = conf.getNodeIndex();

        /** The array index of the second element in a row (the name of a attribute) of the CSV configuration file.*/
        final int attrIndex = conf.getAttrIndex();

        int pos = 0;
        for (String[] prop : conf.getConfEntries()) {
            if (prop[attrIndex].isEmpty()) {
                String nodeName = prop[nodeIndex];
                header[pos++] = nodeName;
            }
            else {
                String attributeName = prop[attrIndex];
                header[pos++] = attributeName;
            }
        }
        writer.writeNext(header);
        log.debug("Added CSV header row: " + Arrays.toString(header));
    }

    /** Traverse the given directory processing files with the given suffix.
     * @param startDirPath The directory to be traversed.
     * @param suffix Suffix for the files to be processed.
     * @throws IOException If it is not possible to read or write to a file. 
     */
    protected void traverseFileTree(String startDirPath, String suffix)
            throws IOException {
        /** Traverse the given directory processing files with the given suffix.*/
        Path startPath = FileSystems.getDefault().getPath(startDirPath);
        log.debug("Starting directory: " + startPath);
        
        Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(final Path file,
                    final BasicFileAttributes attrs) throws IOException {
                /** CSV row entries of output file.*/
                final String[] entries;
                
                if (file.toString().endsWith(suffix)) {
                    log.debug("Processing file: " + file);
                    entries = xmlCSV.xmltoCSV(file);
                    writer.writeNext(entries);
                    log.debug("New CVS row with the following entries addded: " 
                            + Arrays.toString(entries));
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
