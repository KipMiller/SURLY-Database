// CSCI 330 SURLY 2
// Authors: Maria Adams, Mario Gonzalez, Chris Miller

import java.util.*;

// Removes the specified relation from the SURLY database,
// and removes its schema from the CATALOG.
public class Destroy{

   public Destroy(){
   }

   /* destroy()
      removes the relation and everything that goes along with it
   */
   public void destroy(String[] p){

      Surly sur = new Surly();
      ReadFile r = new ReadFile();

      for(int i = 1; i < p.length; i++){
         String clean = r.cleanName(p[i]);

         if(sur.isRelation(clean) == 0){
            System.out.println("CANNOT Destroy " + clean);
         } 
         else {

            Relations remRel = sur.getRelation(clean);
            System.out.println(remRel.getName() + " has been removed from RELATIONS");  
           
            Catalog.getCatalog().remove(remRel);//Now, remove the relation retrieved
         }
      } 
   } 
}
