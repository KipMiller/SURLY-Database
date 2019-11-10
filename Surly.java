// CSCI 330 SURLY 2
// Authors: Maria Adams, Mario Gonzalez, Chris Miller

import java.lang.*;
import java.util.*;

// The Surly Database:
// Data is organized using a Linked-List storage structure,
// where the SURLY database is a linked-list of relations,
// each relation is a linked-list of tuples, and each tuple is a linked-list of attributes.
public class Surly{

   static LinkedList<Relations> surly = new LinkedList<Relations>();
   static LinkedList<Relations> tempRelations = new LinkedList<Relations>();// surly can hold a list of temporary relations that don't go into the the catalog

   public Surly(){}
   public LinkedList<Relations> getDB(){
     return surly;
   }

   // adds a relation to the surly database
   public void addRelation(Relations relations){
      surly.add(relations);
   }

   public void addTemp(Relations temp){
        tempRelations.add(temp);
    }


   /* isTemp()
    *
    *  method to return whether or not a temp relation exists
    */
   public int isTemp(String name){
     Relations temp = new Relations();
     for(int i = 0; i < tempRelations.size(); i++){
       String tempN = tempRelations.get(i).getName();
       if(tempN.equals(name)){
         return 1;// IF it is a temp relation, return 1
       }
     }
     return 0;// if it is NOT a temp relation, return 0
   }

   /* getTemp()
    *
    *  return: Relations specified by the name
    */
   public Relations getTemp(String name){
     Relations temp = new Relations();
     for(int i = 0; i < tempRelations.size(); i++){
       String tempN = tempRelations.get(i).getName();
       if(tempN.equals(name)){
         temp = tempRelations.get(i);
       }
     }
     return temp;
   }

   /* getRelation()
    *
    * method to return a relation specified by its name in the database
    */
   public Relations getRelation(String name){
     Relations temp = new Relations();
     for(int i = 0; i < surly.size(); i++){
       String tempN = surly.get(i).getName();
       if(tempN.equals(name)){
         temp = surly.get(i);
       }
     }
     return temp;
   }

   /* isRelation()
    *
    * checks if the relation exists
    */
   public int isRelation(String name){
     Relations temp = new Relations();
     for(int i = 0; i < surly.size(); i++){
       String tempN = surly.get(i).getName();
       if(tempN.equals(name)){
         return 1;
       }
     }
     return 0;
   }
}
