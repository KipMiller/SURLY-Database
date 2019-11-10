// CSCI 330 SURLY 2
// Authors: Maria Adams, Mario Gonzalez, Chris Miller

import java.lang.*; 

public class Error{


   // Boolean function to check the command string before we parse through it and make changes.
   // Looking for errors like a missing end comment */ or a missing ' for a string
   public static boolean preCheck(String[] unparsed){
      int commentFlag =0;
      int stringFlag =0; 
      
      for(int i = 0; i < unparsed.length; i++){
         if(unparsed[i].contains("/*")){
            commentFlag++;
         }
         if(unparsed[i].contains("*/")){
            commentFlag++;
         }
         if(unparsed[i].contains("'")){
            stringFlag++;
         }
      }
      if((commentFlag % 2 != 0) || (stringFlag %2 != 0)){
         System.out.println("ERROR: Moving onto next line of input...");
         return false;
      }
      else 
         return true;
   }
   
   // Boolean function that will return true if there are no errors found within the parsed command string, 
   // and false if it discovers some form of syntax error within the string array.
   public static boolean parsedCheck(String[] parsed){
      int commandFlag =0;
      Nesting nesting = new Nesting();
      
      for(int i = 0; i < parsed.length; i++){
         if(parsed[i].contains("RELATION")){
            commandFlag++;
         }
         else if(parsed[i].contains("INSERT")){
            commandFlag++;
         }
         else if(parsed[i].contains("PRINT")){
            commandFlag++;
         }      
         else if(parsed[i].contains("DESTROY")){
            commandFlag++;
         }
         else if(parsed[i].contains("DELETE")){
            commandFlag++;
         }  
         else if(parsed[i].contains("JOIN")){
            commandFlag++;
         }
         else if(parsed[i].contains("SELECT")){
            commandFlag++;
         }
         else if(parsed[i].contains("PROJECT")){
            commandFlag++;
         }
      }
      if(commandFlag > 1){// go to the nesting class
         nesting.nestingCheck(parsed);
         return false; 
      }
      return true;
   }
   
   // Method to check whether or not there is a balanced amount of parenthesis in the 
   // parsed string. If there is a missing starting or ending parenthesis, return false, 
   // else return true, signifying the parsed string is good to execute commands.
   public static boolean parenCheck(String[] parsed){
      int parenthesis = 0;
      for(int i = 0; i < parsed.length; i++){
         if(parsed[i].contains("(")){
            parenthesis++;
         }
         if(parsed[i].contains(")")){
            parenthesis--;
         }
      }
      
      if(parenthesis != 0){
         System.out.println("ERROR: Unbalanced parenthesis - Skipping Line...");
         return false;
      }
      else 
         return true;
   }
   
   // Method to check if a new relation matches the correct format
   // I.E. TITLE TYPE SIZE, if not, return false
   public static boolean relationFormat(String[] att){
      int format = 0;
      for(int i = 2; i < att.length; i++){
         if(!att[i].equals("") && !att[i].equals("\n")){// dont count the empty spaces
            format++;
         }
         if(att[i].contains(",") || att[i].contains("\n") || att[i].contains(";")){
            if(format >= 3){// only reset the count if it is at 3 or more (ending with a ; might make the count 4)
               format = 0;
            }
         }
         if(format > 3){// if there are more than 3 entities as an attribute
            System.out.println("ERROR: Incorrect Relation format: " + att[1]);
            return false;// reject this relation
         }
      }   
      //System.out.println("RELATION format is correct: creating relation " + att[1] );
      return true;
   }

}// end of the Error class