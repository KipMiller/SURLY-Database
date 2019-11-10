// CSCI 330 SURLY 2
// Authors: Maria Adams, Mario Gonzalez, Chris Miller

import java.util.*;
import java.lang.*;
import java.io.*;

// Deletes all tuples from the specified relation.
public class Delete{

    private static int AFTER_COND = 3;

    // Method that will either delete all tuples within a relation, or if there is a WHERE 
    // clause then it will only delete specified tuples from the relation.
    public void delete(String[] relName){
        int whereFlag = 0;//determine if there is a WHERE in the string
        Surly sur = new Surly();
        ReadFile r = new ReadFile();
        for(int i = 0; i < relName.length; i++){
            if(relName[i].contains("WHERE")){
                Relations target;
                whereFlag = 1;
                if(sur.isTemp(relName[1]) == 1){// if the target is a temp
                target = sur.getTemp(relName[1]);
                } else {//if the target is NOT a temp and is a regular relation
                target = sur.getRelation(relName[1]);
                }
                LinkedList<Tuple> schema = target.getSchema();// get the targets schema 
                LinkedList<Tuple> tuples = target.getTuples();// get ALL of the targets tuples
                LinkedList<Tuple> deleting = andOr(relName, schema, tuples);
                remove(target, deleting);//Delete specified tuples from the target tuple
            } 
        }
        //else delete every tuple from the relation (but not the relation)
        for(int i = 1; i < relName.length && whereFlag == 0; i++){
            String cleanRel = r.cleanName(relName[i]);
            Relations rel = sur.getRelation(cleanRel);
            rel.remTup();
        }
    }

   // Method that loops through the target Relation and the linked list of tuples to be deleted, 
   // every time there is a match from the Relation to the list of delete targets, that tuple is 
   // deleted from the Relation. 
   public void remove(Relations target, LinkedList<Tuple> deleting){
      LinkedList<Tuple> temp = target.getTuples();
      for(int i = 0; i < target.getTuples().size(); i++){// for every tuple in the target 
         LinkedList<Attribute> attributes = temp.get(i).getAttributes();
         String title = attributes.get(0).getChar();
         
         for(int j = 0; j < deleting.size(); j++){//for every tuple in the specified deleting list
            LinkedList<Attribute> deletAtt = deleting.get(j).getAttributes();    
            String Ttitle = deletAtt.get(0).getChar(); 
            if(title.equals(Ttitle)){//if the title of the to be removed tuple matches the relation's current tuple
               temp.remove(i);
               i--;//with one link of the list gone, we need to decrement the index to accomodate
            }        
         }   
      }
   }

   // Method to loop through the conditions and determine the order in which to perform the conditions, 
   // AND has higher precedence than OR, but if AND returns an empty relation, then OR's are performed
   public LinkedList andOr(String[] str, LinkedList<Tuple> schema, LinkedList<Tuple> target){
      LinkedList<Tuple> specified = new LinkedList<Tuple>();// the list of tuples that will be wittled down by condition
      String[][] condition = getConditions(str);// get the conditions to be performed on the target tuples
      int andFlag = 0;
      
      if(condition[0][3] == null){//for if there are no ANDS / ORS
         specified = where(condition, 0, schema, target);
         return specified;
      }    
      for(int index = 0; condition[index][0] != null; index++){// loop through every possible condition, doing ANDS first
         if((condition[index][3].contains("or") || condition[index][0].contains("or")) && andFlag == 1){// If there is an OR and the ANDs failed
            specified = where(condition, index, schema, target);
            if(specified.size() != 0){// if an OR condition does not return empty
               return specified;
            }
            
         } else if (condition[index][3].contains("and")){
            specified = where(condition, index, schema, target);
            if(specified.size() == 0){//if the and conditions return an empty
               condition[index][3] = "DONE";
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
      for(int innerIndex = 0; condition[index][innerIndex] != null; innerIndex += 4){// loop through the specified condition (by INDEX)
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
      for(int i = AFTER_COND; i < str.length; i++){// starting AFTER the where clause, loop through all conditions
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
         condition[condNum][count] = str[i].replaceAll(";\n","");
         count++;
      }
      return condition;
   }
}
