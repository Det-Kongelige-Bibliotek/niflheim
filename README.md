
# Niflheim
Traverses a given folder and its subfolders searching for XML files with a given suffix. 
Based on a configuration giving specifying XML attributes and values Niflheim will extract these 
and insert them as values in rows into a CSV formatted spreadsheet. 

The configuration is given in a CSV file where the first column refers to the XML element and 
the second a possible related attribute.
If the second column has a value this value is used as the column name in the spreadsheet and 
if it does not, the value of the first column is used as column name in the resulting spreadsheet.

The main purpose of this program is to extract data from FITS XML files and 
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


Running niflheim
----------------------------------------------------------
You need four arguments: the path to the configuration file, the path to the starting directory, 
the path to the output file, and the suffix for the files to be processed.

Note: the logs are written to logs/niflheim.log 


Running niflheim on Linux
----------------------------------------------------------
Open a terminal and run the command
bash bin/niflheim.sh 'conf_path' 'start_dir_path' 'output_csv_file_path' 'suffix'


Running niflheim on Windows
----------------------------------------------------------
Open the command terminal cmd and run the command
"bin/niflheim.cmd" "conf_path" "start_dir_path" "output_csv_file_path" "suffix"
