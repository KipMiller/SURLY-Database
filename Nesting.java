// CSCI 330 SURLY 2 
// Authors: Maria Adams, Mario Gonzalez, Chris Miller

import java.util.*;
import java.lang.*;
import java.io.*;

// Class to handle nested commands in a command line
public class Nesting{
   
   // Method that counts the number of parenthesis in the nested string to determine
   // if there is a correct number to allow proper nesting.
   public int nestingCheck(String[] str){
      int parens = 0;
      for(int i = 0; i < str.length; i++){
         if(str[i].contains("))")){
            parens += 2;
         }
         else if(str[i].contains("(")){
            parens++;
         }
         else if(str[i].contains("((")){
            parens += 2;
         }
         else if(str[i].contains(")")){
            parens++;
         }
      }
      if(parens % 2 != 0){
         System.out.println("Unbalanced number of parenthesis...");
         return 0;
      } else {
         // Move onto the nesting function
         nest(str);
      }
      return 0;
   }

   // Method to perform the commands in the order they were nested (inside -> out)
   public void nest(String[] str){
      String[][] commands = new String[str.length][str.length+3];
      int commandCount = 0;
      int paren =0;
      ReadFile rf = new ReadFile();
      for(int i = 0; i < str.length; i++){
         
         if(str[i].contains("(")){
            commands[commandCount][paren] = "NESTTEMP;";// if we see the end of one function, tell it to work on the nesting temp
            commandCount++;
            paren = 0;
            commands[commandCount][paren] = "NESTTEMP";
            paren++;
            commands[commandCount][paren] = "=";
            paren++;
            commands[commandCount][paren] = str[i];
            paren++;
         } else {
            commands[commandCount][paren] = str[i];
            paren++;
         }
      }
      
      for(int i = commandCount; i >= 0; i--){//go to the end and work way outwards for commands
         int exSize = 0; 
         for(int k = 0; commands[i][k] != null; k++){
               exSize++;// Get the correct number of arguments in the execute string (crashes if we dont get right size)
         }
         String[] execute = new String[exSize];
         for(int j = 0; commands[i][j] != null; j++){
            execute[j] = commands[i][j];
         }
         rf.checkCommands(execute);
      }
   }
}