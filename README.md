
# Niflheim
Traverse a given folder and its subfolders searching for XML files with a given suffix. 
Based on a configuration giving specifying XML attributes and values Niflheim will extract these 
and insert them as values in rows into a CSV formatted spreadsheet. 
The configuration is given in a CSV file where the first colon refers to the XML element and 
the second a possible related attribute.
If the second colon has a value this value is used as the colon name in the spreadsheet and 
if it does not, the value of the first colon is used as colon name in the resulting spreadsheet.
The main purpose of this program was to extract data from FITS XML files and 
generate a CSV file based on that data. This process is also, what is being tested in the unit tests.
If the processing of a XML file fails an empty row is inserted in the CSV spreadsheet.

Making the niflheim package without running the unit tests
----------------------------------------------------------

mvn clean -Dmaven.test.skip=true package


Producing a new niflheim package for deployment
----------------------------------------------------------

Update the version variable in pom.xml

Produce the new package using Maven


Deploying the Niflheim program
-------------------------------
$ unzip niflheim-$VERSION.zip 

$ cd niflheim-$VERSION


Running niflheim on Linux
----------------------------------------------------------
You need four arguments: the path to the configuration file, the path to the starting directory, 
the path to the output file, and the suffix for the files to be processed.

bash bin/niflheim.sh 'conf_path' 'start_dir_path' 'output_csv_file_path' 'suffix'

Note: the logs are written to logs/niflheim.log 

