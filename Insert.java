// CSCI 330 SURLY 2
// Authors: Maria Adams, Mario Gonzalez, Chris Miller

import java.lang.*; 
import java.util.*; 


public class Insert{

   /* insert()
    *
    *  gathers all the data values of each attributes
    *  creates and adds a tuple and inserts accordingly
    */
   public void insert(String[] str){

      Surly sur = new Surly();
      try{
         if(sur.isRelation(str[1]) == 0){
            System.out.println("ERROR: Cannot insert tuple into " + str[1] + ", relation does not exist.");
            return;
         }

         Tuple tuple = new Tuple(str, str[1]);
         addTuple(tuple, str[1], sur);

      } catch(Exception e){
         System.out.println("ERROR: " + e + " this is called from Insert Class");
      }
   }
  
   /* addTuple()
    *
    * goes through the list of relations and adds a tuple
    * to the corresponding relation
    */
   public void addTuple(Tuple tuple, String relName, Surly sur){

      for(int i = 0; i < sur.surly.size(); i++){// iterate through the surly database and find the corresponding relation
         String title = sur.surly.get(i).getName();// get the name of the relation
         if(title.equals(relName)){// if the tuple matches the relation

            if(tuple.getAttributes().size() != sur.surly.get(i).getColumn()){
               System.out.println("ERROR: Not enough attributes to match " + relName + "'s schema...\n");
               return;
            }
            Surly.surly.get(i).getTuples().add(tuple);
         }
      }
   }

}// end of Insert class
