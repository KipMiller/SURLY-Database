// CSCI 330 SURLY 2
// Authors: Maria Adams, Mario Gonzalez, Chris Miller

//last update: Mon, 2/19 by Maria

import java.util.*;
import java.lang.*;
import java.io.*;

public class ReadFile{

  /* openFile()
     Opens the file based based on its title and
     reads each line into command array
   */
   public void openFile(String title){

    File commandFile = new File(title);

    // create the database object
    Surly DB = new Surly();

    // make the catalog
    Catalog catalog = new Catalog();

    try(BufferedReader read = new BufferedReader(new FileReader(commandFile))){
      String line; String command ="";

      while((line = read.readLine()) != null){
      	line += "\n"; command += line;

      	if(line.contains(";")){

          //starting point for parsing command strings
          parseCommands(command);
          command = ""; line = "";
         }
       }
    }
    catch(Exception e){
      System.out.println(e);
    }
  }

  /* parseCommands()  - (helper)
   *
   *  Splits up the line of text stored in command based on the syntax logic of the writeup
   *  if the command line (before being parsed) fails an error check, then move to next line
   */
  public static void parseCommands (String command){
    String[] spaceSplit = command.split(" ");
    String parsed = "";

    if(!Error.preCheck(spaceSplit)){
      System.out.println("preCheck failure");
      return;
    }
    int flag = 0;
    int stringCheck = 0;

    for(String split: spaceSplit){
      if(split.contains("'")){// if we find a string in the current command line
        stringCheck = 1;
      }
      if(split.contains("*/")){//end of comment
        flag = 0;
      }
      if(split.contains("/*")){//start comment
        flag = 1;
      }
      if(flag == 0){//only add to parsed if not a comment
        parsed += split;
        parsed += " ";
      }

    }
    filterChar(parsed, stringCheck);

  }


  /* helper method that strips unwanted characters attached to strings */
  public static void filterChar(String parsed, int stringCheck){

    // split on end comments OR space OR start comment
    String[] commentSplit = parsed.replaceFirst("\\*/", "").split("\\*/| |/\\*");

    if(stringCheck == 1){// if we find 'strings' in the line, then react accordingly
      String[] stringParsed = checkForStrings(commentSplit);
      if(Error.parsedCheck(stringParsed)){
         checkCommands(stringParsed);// now execute any commands based on the final parsed string
      }
    }
    else{ // else don't adjust for 'strings' and just execute according commands
      if(Error.parsedCheck(commentSplit)){
         checkCommands(commentSplit);
      }
    }
  }


  /* checkForString()
     combine multiple string enclosed by ' ' into one string attribute
     resolves the case for attributes like 'DATA STRUCTURE'
   */
  public static String[] checkForStrings(String[] checking){
    int stringFlag = 0;
    int[] strIndx = new int[2];

    //get full string and index of start and end of the full string
    String line = getString(checking,strIndx);

    // make new string array of appropriate size
    String[] checked = new String[checking.length - (strIndx[1] - strIndx[0])];
    int stringStart = strIndx[0];
    int stringEnd = strIndx[1];

    checked = newString(line,stringStart,stringEnd,checking,checked);

    return checked;
  }

  /* helper method for checking strings */
  public static String[] newString(String line,int start,int end,String[] chkng, String[] chkd){
    int j = 0;
    for(int i = 0; i < chkng.length; i++){
      if(i < start){// up until the start of the string
        chkd[j] = chkng[i];
        j++;
      }
      if(i == start){// put the newly made word into its appropriate spot
        chkd[j] = line;
        j++;
      }
      if(i > end){// continue adding the other words in their spots
        chkd[j] = chkng[i];
        j++;
      }
    }
    return chkd;
  }


  /* Gets the start and end index of a string */
  public static String getString(String[] word, int[] index){
    int stringFlag = 0;
    String stringLine = "";

    for(int i = 0; i < word.length; i++){
      if(word[i].contains("'") && stringFlag == 0){// start of a string
        stringFlag = 1;
        index[0] = i;// get the index of the start of the string
        stringLine += (word[i] + " ");
        i++;
      }
      if(stringFlag == 1){// keep adding while in string
        stringLine += (word[i] + " ");
      }
      if(word[i].contains("'") && stringFlag == 1){// end of a string
        stringFlag = 0;
        index[1] = i;// get the ending index of the string
      }
    }
    return stringLine;
  }


  /* helper that removes unnecessary characters from a string */
  public String cleanName(String dirty){
        String clean = dirty.replaceAll("[^a-zA-Z0-9]","");
        return clean;
    }


  /* checkCommands()
     Iterates through each parsed line of the command array, looking for commands to execute
     If a command is recognized, enter the appropriate class and execute its primary function
   */
  public static void checkCommands(String[] parsed){
       Surly sur = new Surly();
       Relations r = new Relations();
       Insert ins = new Insert();
       Print pr = new Print();
       Destroy des = new Destroy();
       Delete del = new Delete();
       Select sel = new Select();
       Project proj = new Project();
       Join join = new Join();
      
      // loop through to look for a SELECT either in the start or after an assignment of temp variable
      for(int i = 0; i < parsed.length; i++){
         if(parsed[i].contains("SELECT")){
            sel.select(parsed);
         }
         if(parsed[i].contains("PROJECT")){
            proj.project(parsed);
         }
         if(parsed[i].contains("JOIN")){
             join.join_tables(parsed, sur);
         }
      }
      
      if(parsed[0].contains("RELATION")){
         r.relate(parsed);
      }
      
      else if(parsed[0].contains("INSERT")){
         ins.insert(parsed);
      }
      
      else if(parsed[0].contains("PRINT")){
        pr.printFunc(parsed);
      }
      
      else if(parsed[0].contains("DESTROY")){
      	des.destroy(parsed);
      }
      
      else if(parsed[0].contains("DELETE")){
        del.delete(parsed);
      }
   }
}// end of ReadFile class
