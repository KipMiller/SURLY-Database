// CSCI 330 SURLY 1
// Authors: Maria Adams, Mario Gonzalez, Chris Miller

import java.lang.*;
import java.util.*;

// Each tuple is a linked-list of attributes, attributes format being defined in RELATION
// and attribute data being obtained in INSERT.
public class Tuple{

   public static final int BEG_ATTRIBUTES = 2;
   private String relationName;
   private String dataType;
   private String title;
   private int length;
   private LinkedList<Attribute> attributes = new LinkedList<Attribute>();

  /* constructor with 4 params */
   public Tuple(String relationName, String dataType, String title, String Length){
      this.relationName = relationName;
      this.dataType = dataType;
      this.title = title;
      int temp = Integer.parseInt(Length);
      length = temp;
   }
   public Tuple(LinkedList<Attribute> attributes){
      this.attributes = attributes;
    }

   /* Constructor with 2 params */
   public Tuple(String[] att, String name){// actual tuple that stores info (?)
      formTuple(att, name);
   }

   public Tuple(){} //empty object for use in PROJECT

   /* formTuple()
    *
    * creates tuple that adheres to its dataTypes
    */
   public void formTuple(String[] att, String name){
      LinkedList<Tuple> schema = getSchema(name);
      for(int i = 0; i < att.length-BEG_ATTRIBUTES; i++){// start at 2 to avoid the the command and the title
         Attribute attribute = new Attribute();// make a new attribute to be inserted in the LL
         int attLength = schema.get(i).getLength();// get the projected length of the attribute

         if(schema.get(i).getType().equals("CHAR")){
            attribute.setChar(att[i+BEG_ATTRIBUTES], attLength);// TODO: Work on this area, enforcing the length of an attribute
         }
         else if(schema.get(i).getType().equals("NUM")){
            attribute.setNum(att[i+BEG_ATTRIBUTES], attLength);// TODO: Work on this area, enforcing the length of an attribute
         }
         attributes.add(attribute);
      }
   }

   /* printAttributes()
    *
    * goes through each attribute and prints it
    */
   public void printAttributes(){
      LinkedList temp = getAttributes();
      for(int i = 0; i < temp.size(); i++){
         System.out.println(temp.get(i));
      }
   }

   /* getSchema()
    *
    * param: relation name
    * return: linkedlist of Tuple
    */
   public LinkedList<Tuple> getSchema(String relName){
      LinkedList<Tuple> temp = new LinkedList<Tuple>();
      for(int i = 0; i < Surly.surly.size(); i++){// iterate through the surly database and find the corresponding relation
         String title = Surly.surly.get(i).getName();// get the name of the relation
         if(title.equals(relName)){// if the title of this tuple matches a relation in the DB
            temp = Surly.surly.get(i).getSchema();// set local schema to the relation's schema
         }
      }
      return temp;
   }

   /* Getters */
   public String getRelName(){
      return relationName;
   }
   public String getType(){
      return dataType;
   }
   public String getTitle(){
      return title;
   }
   public int getLength(){
      return length;
   }
   public void addAttributes(LinkedList<Attribute> a){
     attributes = a;
   }
   public LinkedList<Attribute> getAttributes(){ return this.attributes; }

}// end of Tuple class
