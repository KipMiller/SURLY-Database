// CSCI 330 SURLY 2 
// Authors: Maria Adams, Mario Gonzalez, Chris Miller

public class Attribute{

   private int length;// expected length of the data
   private String CHAR;
   private int NUM;
   private String type;
   
   // Empty constructor
   public Attribute(){
   
   }

   //getter and setters   									
   public int getLength(){
      return length; 
   }

   public String getType(){
      return type;
   }

   public void setLength(int Length){
      this.length = Length; 
   }

   public String getChar(){
      return CHAR; 
   }

   public void setChar(String Char, int length){  
      type = "CHAR";
      Char = Char.replace(";", "");
      Char = Char.replace("\n", "");
      
      if(Char.length() > length){
         Char = truncate(Char, length);
      }
      this.CHAR = Char; 
   }

   public int getNum(){
      return NUM; 
   }

   public void setNum(String Num, int length){
      type = "NUM";
      Num = Num.replace(";", "");
      Num = Num.replace("\n", "");

      if(Num.length() > length){
         Num = truncate(Num, length);
      }
      int temp = Integer.parseInt(Num); 
      NUM = temp; 
   }

   // method to truncate a string if it is bigger than the projected length depicted
   // within the schema
   public String truncate(String target, int length){
      String shorter = "";
      for(int i = 0; i < length; i++){
         shorter += target.charAt(i);
      }
      return shorter;
   }

}// end of Attribute class
