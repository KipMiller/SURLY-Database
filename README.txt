SURLY II

Installation and Demo Instructions: 
------------------------------------

Files included in the zip: 

Main.java
Catalog.java
Surly.java
ReadFile.java
Relations.java
Tuple.java
Attribute.java
Insert.java
Print.java
Destroy.java
Delete.java
Error.java
Nesting.java
Select.java
Join.java
Project.java

testfiles:
SURLY-report file
inputTest_1.txt
outputTest_1.txt
Test.txt
TestOut.txt
inputNoRelation.txt
outputNoRelation.txt

others:
README file


To compile:	javac Main.java
To run: 	java Main Test.txt

Instructions: 

1. Make sure all .java files and .txt files (testfiles) are in the same folder
2. After it has been loaded and extracted to a location,
   you must compile the Main file. 

3. Once Main has been compiled, create an input file with 
commands such as the following:

---------------------------------------------------------------                                                             
/* Comments can be placed anywhere in the file */   

RELATION COURSE (CNUM CHAR 8, TITLE CHAR 30, CREDITS NUM 4); 
                                                             
/* Place this tuple into the COURSE relation */ 	

INSERT COURSE CSCI141 'Computer Programming I' 4;	         
							                                 
PRINT COURSE; 						                         
							                                 
DESTROY COURSE; 					     
							     
PRINT CATALOG;						     
							     
--------------------------------------------------------------
(A complete list of commands includes PRINT, RELATION, 
DESTROY, DELETE, INSERT, SELECT, PROJECT, and JOIN)

4. If the input file exists, then each 
command should run as long as they are correctly 
entered within the text file. 

5. Enjoy!

