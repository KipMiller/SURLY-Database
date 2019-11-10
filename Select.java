// CSCI 330 SURLY 2 
// Authors: Maria Adams, Mario Gonzalez, Chris Miller

import java.util.*;
import java.lang.*;
import java.io.*;

public class Select{

   private static int KEY_COND = 5;
   private static int NEXT_COND = 4;
   private static int AND0R_COND = 3;
   private static int TARGET_NAME = 3;

   // method to return all tuples in a relation that meet a condition
   // USAGE: tempRelationName = SELECT relationName WHERE <Conditions>
   // (Where is optional, can be used to retreive a relation alone)
   public void select(String[] str){

      Surly sur = new Surly();
      ReadFile r = new ReadFile();
      String targetName = r.cleanName(str[TARGET_NAME]);
      String title = r.cleanName(str[0]);
      
      Relations target;// either a relation or a temp relation
      if(sur.isRelation(title) == 1){
         System.out.println("ERROR: Variable name '" + targetName + "' is already a Relation in the DB...");
         return;
      }
      if(sur.isTemp(targetName) == 1){// if the target is a temp
         target = sur.getTemp(targetName);
      } else {//if the target is NOT a temp and is a regular relation
         target = sur.getRelation(targetName);
      }
      LinkedList<Tuple> schema = target.getSchema();// get the targets schema 
      LinkedList<Tuple> tuples = target.getTuples();// get ALL of the targets tuples
      int count = target.getColumn();
      for(int i = 0; i < str.length; i++){    
         if(str[i].contains("WHERE")){// if there is a where condition 
            tuples = andOr(str, schema, tuples);// tuples is equal to the new linked list formed by the where clause
         }
      }
      Relations tempRelation = new Relations(title, count, schema, tuples);// make a temporary relation of specified name 
      sur.addTemp(tempRelation);// add the relation to the surly database
   }

   // Method to loop through the conditions and determine the order in which to perform the conditions, 
   // AND has higher precedence than OR, but if AND returns an empty relation, then OR's are performed
   public LinkedList andOr(String[] str, LinkedList<Tuple> schema, LinkedList<Tuple> target){
      LinkedList<Tuple> specified = new LinkedList<Tuple>();// the list of tuples that will be wittled down by condition
      String[][] condition = getConditions(str);// get the conditions to be performed on the target tuples
      int andFlag = 0;
      
      if(condition[0][AND0R_COND] == null){// if there is no AND / OR statements
         specified = where(condition, 0, schema, target);
         return specified;
      }
      for(int index = 0; condition[index][0] != null; index++){// loop through every possible condition, doing ANDS first
         if((condition[index][AND0R_COND].contains("or") || condition[index][0].contains("or")) && andFlag == 1){// If there is an OR and the ANDs failed
            specified = where(condition, index, schema, target);
            if(specified.size() != 0){// if an OR condition does not return empty
               return specified;
            }
         } else if (condition[index][AND0R_COND].contains("and")){
            specified = where(condition, index, schema, target);
            if(specified.size() == 0){//if the and conditions return an empty
               condition[index][AND0R_COND] = "DONE";
               index = -1;// reset the condition looping to the start
               andFlag = 1;// do 'or's this time
            } else {// if the AND relation is NOT empty 
               return specified;
            }
         }  
      }
      return specified;
   }
     
   // Method to further specify which tuples we are pulling from the relation
   // Takes the command string, the schema from the target relation, and all the tuples of the target
   // Returns a LinkedList where all tuples meet the specified criteria from str.
   public LinkedList where(String[][] condition,int index, LinkedList<Tuple> schema, LinkedList<Tuple> target){
      LinkedList<Tuple> copy = target;
      LinkedList<Tuple> specified = new LinkedList<Tuple>();

      for(int innerIndex = 0; condition[index][innerIndex] != null; innerIndex += NEXT_COND){// loop through the specified condition (by INDEX)
         if(condition[index][0].contains("or")){
            innerIndex++;
         }
         String type = condition[index][innerIndex]; 
         String operator = condition[index][innerIndex + 1]; 
         String descriptor = condition[index][innerIndex + 2];         
         for(int i = 0; i < schema.size(); i++){// get the correct attribute from the relations schema
            if(type.equals(schema.get(i).getTitle())){// if the schema title matches the WHERE type
               for(int j = 0; j < copy.size(); j++){// loop through every tuple in this relation
                  LinkedList<Attribute> temp = copy.get(j).getAttributes();
                  // Get the attributes of each tuple matching the correct condition's specifier
                  String title = temp.get(i).getChar();
                  int num = temp.get(i).getNum();
                  
                  if(compare(title, num, operator, descriptor) == 1){// call the compare function to determine validity
                     if(!specified.contains(copy.get(j))){// if we don't already have it
                        specified.add(copy.get(j));//if this tuple is valid, add it to the relation (specified/temp)
                     } 
                  }
               }
            }
         }
         copy = specified;// make the original relation equal to the specified list (for further iterations) 
         specified = new LinkedList<Tuple>();// reset the specified list for the next iteration
         if(copy.size() == 0 ){
            return copy;
         }
      }
      return copy;    
   }

   // Method to determine if there is a match with the given tuple's data and the 
   // operator + descriptor (IE CS101 = CS145 == 0)
   public int compare(String title, int num, String operator, String descriptor){
      try{// Try to find an equal operator to the one provided
         if(operator.equals("=")){
            if(title != null && descriptor.contains(title)){
               return 1;
            } else if(num != 0 && num == Integer.parseInt(descriptor)){
               return 1;
            }
            return 0;
         } else if(operator.equals("!=")){
            if(title != null && !descriptor.contains(title)){
               return 1;
            } else if(num != 0 && num != Integer.parseInt(descriptor)){
               return 1;
            }
            return 0; 
         } else if(operator.equals("<")){
            if(num != 0 && num < Integer.parseInt(descriptor)){
               return 1;
            }
            return 0;
         } else if(operator.equals(">")){
            if(num != 0 && num > Integer.parseInt(descriptor)){
               return 1;
            }
            return 0;
         } else if(operator.equals("<=")){
            if(num != 0 && num <= Integer.parseInt(descriptor)){
               return 1;
            }
            return 0;
         } else if(operator.equals(">=")){
            if(num != 0 && num >= Integer.parseInt(descriptor)){
               return 1;
            }
            return 0;
         } 
      } catch(Exception e){// If there is an error, its most likely converting an int from a string
         System.out.println("ERROR: Could not convert descriptor to an int...");
         System.out.println("ERROR: " + e);
         return 0;
      }
      System.out.println("Didn't recognize operator...");
      return 0;
   }

   // Method that creates a 2d array of conditions that need to be fulfilled in order 
   // for a tuple to be accepted by the where clause
   public String[][] getConditions(String[] str){

      String[][] condition = new String[str.length][str.length];

      int count = 0;
      int condNum = 0;
      int andFlag = 0;
      for(int i = KEY_COND; i < str.length; i++){// starting AFTER the where clause, loop through all conditions
               
         if(str[i].contains("and")){//if we run into an AND, store it in its place (for the andOr function)
            condition[condNum][count] = str[i];
            i++;
            count++;
            andFlag = 1;
         } else if(str[i].contains("or")){//if we run into an OR, make a new command and leave OR in its place
            if(andFlag == 1){
               condNum++;
               count = 0;
               condition[condNum][count] = str[i];
               count++;
               i++;
               andFlag = 0;
            } else {
               condition[condNum][count] = str[i];
               condNum++;
               i++;
               count = 0;
            }
         }
         condition[condNum][count] = str[i].replaceAll("[;\n()]","");
         count++;
      }
      return condition;
   }
}