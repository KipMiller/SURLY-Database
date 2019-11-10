// CSCI 330 SURLY 1
// Authors: Maria Adams, Mario Gonzalez, Chris Miller

import java.util.*;
import java.lang.*;
import java.io.*;

// Creates a new relation in the SURLY database, and adds its schema to the CATALOG.
public class Relations{

   private int START_ATTR = 2;
   private String name;// The title of the relation
   private int columnNum;// how many attributes for said relation
   private LinkedList<Tuple> tuples = new LinkedList<Tuple>();//
   private LinkedList<Tuple> schemaFormat = new LinkedList<Tuple>();// each relation stores its format for the tuples

   public Relations(){}

   //constructor with 4 params
   public Relations(String name, int columnNum, LinkedList<Tuple> schemaFormat, LinkedList<Tuple> tuples){//, LinkedList schema){
      this.name = name;// the name of the relation
      this.columnNum = columnNum;// how many columns it has in its tuple
      this.schemaFormat = schemaFormat;// the format that the tuple will take (I.E. (Char, int, Char))
      this.tuples = tuples;// the tuple of attributes
   }

   //getters
   public String getName(){
      return name;
   }
   public int getColumn(){
      return columnNum;
   }
   public LinkedList getSchema(){
      return schemaFormat;
   }
   public LinkedList getTuples(){
      return tuples;
   }

   //setter
    public void setColumnNum(int columnNum){
       this.columnNum=columnNum;
    }


    /* remTup()
     *removes tuple in a relation */
    public void remTup(){

        for(int i=0; i<tuples.size(); i++){
            this.tuples.remove(i);
            i--;
        }

    }

   /* relate()
      obtains schema for each relation
    */
    public void relate(String[] att) {

        Surly sur = new Surly();

        if(sur.isRelation(att[1]) == 1){
            System.out.println("ERROR: Cannot create new relation " + att[1] + ", Relation already in the database...\n");
            return;
        }
        int attCount = 1;
        int commaFlag = 0;// if there is a comma on a line, don't also count newlines.

        if(!Error.parenCheck(att) || !Error.relationFormat(att)){
            return;
        }
        //find # of commas (or newlines), that will determine how many attributes we have
        for(int i = 1; i < att.length -1;i++){
            if(att[i].contains(",")){
            commaFlag = 1;
            attCount++;
            }
            else if(att[i].contains("\n") && (commaFlag != 1) ){//if, instead of commas, the line is seperated by newlines
                attCount++;
                }
            }
        getFormat(att, attCount);// obtain the schema format and store it in the relation

    } //last brace of main function


   /* getFormat()
        creates relation object that will store the schema
        sets up attributes to makeSchema
      param: (string)attributes, (int) number of attributes
    */
   public void getFormat(String[] att, int count){
        Surly sur = new Surly();
        String relName = att[1];
        String attribute = "";
        for(int i = START_ATTR; i < att.length; i++){

            attribute += att[i];
            attribute += " ";
            if(att[i].contains(",") || att[i].contains("\n")){
               String schem = attribute.replace(",", "");
               attribute = schem;
            }
        }
        LinkedList<Tuple> schema = makeSchema(attribute, relName, count);
        LinkedList<Tuple> tuples = new LinkedList<Tuple>();

        Relations catalRelation = new Relations(relName, count, schema, tuples);// make a new relation containing the format schema for future tuples

        sur.addRelation(catalRelation);// add the relation to the surly database
        Catalog.addToCatalog(catalRelation);// add this relation (with its schema format and number of attributes) to the catalog
   }

   /* makeSchema()
         creates a linked-list tuple that stores proper format
         that corresponding tuples will take for this relation
      param: (String) schema, (String)name of relation, (int)number of attributes found
      return: formatted schema
    */
   public LinkedList makeSchema(String schema, String relName, int count){
      LinkedList<Tuple> finishedFormat = new LinkedList<Tuple>();
      String replaced = schema.replace("\n", "");// get rid of newlines
      String[] seperated = replaced.split(" |\t");// split on spaces or tabs

      int loopCount = 0;
      int attCount = 0;
      String title = "";
      String type = "";
      String length = "";
      for( String element : seperated){
         if(attCount < count && !element.equals("")){// if element is NOT empty, and only loop attribute # amount of times
            if(loopCount == 0){
               title = element.replace("(", "");// the title of the column (name of element in tuple)
               loopCount++;
            }
            else if(loopCount == 1){
               type = element;// the data type of element in tuple
               loopCount++;
            }
            else if(loopCount == 2){
               length = element.replace(");", "");// the expected size of the element in the tuple
               Tuple tupleElement = new Tuple(relName, type, title, length);
               finishedFormat.add(tupleElement);
               title = "";
               type = "";
               length = "";
               loopCount = 0;
               attCount++;
            }
         }
      }
      return finishedFormat;
   }

} //last brace of the class
