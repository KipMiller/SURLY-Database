// CSCI 330 SURLY 2
// Authors: Maria Adams, Mario Gonzalez, Chris Miller

import java.util.*; 

// Formats and prints each of the named relations in tabular form.
public class Print{

   /* printFunc()
    *
    *  prints the specified relation and all of its attributes
    */
   public void printFunc(String[] commands){

      Surly sur = new Surly();
      ReadFile r = new ReadFile();
      for(int i = 1; i < commands.length; i++){// loop for every RELATION in the string
         
         Relations temp = new Relations(); 
         String cleanTitle = r.cleanName(commands[i]);
         int spacing = 0;
         if(commands[i].contains("CATALOG")){// if they want to print the catalog
            Catalog.printCatalog();
         } 
         
         //TEMP STUFF **********************************************************************
         else if(sur.isTemp(cleanTitle) == 1 && sur.isRelation(cleanTitle) == 0 ){
            temp = sur.getTemp(cleanTitle);
            
            LinkedList<Tuple> tempSchema = temp.getSchema();// get the schema from the temp
            System.out.println(String.format("\n %25s %25s \n", cleanTitle, ""));
            for(int j = 0; j < temp.getColumn(); j++){// how many attributes there are in this relation
               spacing = (tempSchema.get(j).getLength() + 5);// divide spacing by how many columns are in a relation
               System.out.print(String.format(" %-" + spacing+ "s", tempSchema.get(j).getTitle()));
            }
            System.out.println("\n");
            printAttributes(temp);
          
         }
         // *******************************************************************************
         
          else if(sur.isRelation(cleanTitle) == 0){// if the relation doesn't exist
            System.out.println("ERROR: Cannot print relation '" + cleanTitle +"', relation does not exist.");
         } else {// if it does exist and isn't the catalog
            temp = sur.getRelation(cleanTitle);
            
            LinkedList<Tuple> tempSchema = temp.getSchema();// get the schema from the temp
            System.out.println(String.format("\n %25s %25s \n", cleanTitle, ""));
            for(int j = 0; j < temp.getColumn(); j++){// how many attributes there are in this relation
               spacing = (tempSchema.get(j).getLength() + 5);// divide spacing by how many columns are in a relation
               System.out.print(String.format(" %-" + spacing+ "s", tempSchema.get(j).getTitle()));
            }
            System.out.println("\n");
            printAttributes(temp);
         }
      }
      
   }


   /* printAttributes()
    *
    * goes through each tuple in a relation and prints out
    * every attribute within each tuple.
    */
   public void printAttributes(Relations temp){
      LinkedList<Tuple> tempTuple = temp.getTuples();
      LinkedList<Tuple> tempSchema = temp.getSchema();
      
      for(int t = 0; t < tempTuple.size(); t++){
         LinkedList<Attribute> tempAttr = tempTuple.get(t).getAttributes();
         for(int n = 0; n < tempAttr.size(); n++){
            
            int spacing = (tempSchema.get(n).getLength() + 5);
            if(tempAttr.get(n).getType().equals("CHAR")){
               System.out.print(String.format(" %-" + spacing + "s",tempAttr.get(n).getChar()));
            } 
            else {
               System.out.print(String.format(" %-" + spacing + "s",tempAttr.get(n).getNum())); 
            }
         }
         System.out.println("\n");
      }
   }
    
}// end of Print class