package dk.kb.niflheim;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Extract data from XML files and generate a CSV row based on the extracted data.
 */
public class XmlCSV {
    /** The logger.*/
    private static final Logger log = LoggerFactory.getLogger(XmlCSV.class);

    /** The configuration.*/
    private final Configuration conf;
    
    /** List of CVS row entries from the configuration file.*/    
    private final List<String[]> confEntries;
    
    /** The Constructor.
     * @param conf The configuration.
    */
    public XmlCSV(Configuration conf) {
        this.conf = conf;
        this.confEntries = conf.getConfEntries();
    }
    
    /** Extract XML node and attribute values as specified in the configuration file and convert them to CSV rows.
     * @param xmlFile The path to the XML file from which data is extracted.
     * @return The entries in a CSV row.
    */
    public final String[] xmltoCSV(final Path xmlFile) {
        /** The array index of the first element in a row (the name of a node) of the CSV configuration file.*/
        final int nodeIndex = conf.getNodeIndex();

        /** The array index of the second element in a row (the name of a attribute) of the CSV configuration file.*/
        final int attrIndex = conf.getAttrIndex();

        /** The number of entries defined in the configuration file.*/    
        int psize = confEntries.size();

        /** An empty row to hold the entries.*/  
        String[] entries = new String[psize];
        
        File file = xmlFile.toFile();

        DocumentBuilder dBuilder;
        Document doc = null;

        /** Load XML document*/
        try {
            dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = dBuilder.parse(file);

            int pos = 0;
            String nodeName;
            String attributeName;
            NodeList nodeList;
            String elementValue;
            Element element;
            
            /** Extract XML node and attribute values from XML file and use them to create a CSV row*/
            for (String[] prop : confEntries) {
                nodeName = prop[nodeIndex];
                attributeName = prop[attrIndex];
                if (attributeName.isEmpty()) {
                    nodeList = doc.getElementsByTagName(nodeName);
                    if (nodeList.getLength() == 0) {
                        entries[pos] = "";
                    }
                    else {
                        elementValue = nodeList.item(0).getTextContent();
                        entries[pos] = elementValue;
                    }
                }
                else {
                    element = (Element) doc.getElementsByTagName(nodeName).item(0);
                    entries[pos] = element.getAttribute(attributeName);
                }
                pos++;
            }
        } 
        catch (ParserConfigurationException | SAXException | IOException e) {
            log.error("Failure in loading or passing XML document!", e);
        }
        return entries;
    }
}
