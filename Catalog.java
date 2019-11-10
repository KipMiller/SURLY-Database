// CSCI 330 SURLY 2
// Authors: Maria Adams, Mario Gonzalez, Chris Miller
import java.util.*; 

// A special relation that is automatically created when the program 
// runs and stores the schema of every relation in the database.
// It contains the names of each Relation and the names of each 
// Attribute of each Relation. (See example of Catalog in our notes)
public class Catalog{
    
    static LinkedList<Relations> relations = new LinkedList<Relations>();// stores all relations in the DB for the purpose of displaying their tuple formats (schema)
    
    public Catalog(){// empty constructor
    }
    
    public static LinkedList<Relations> getCatalog(){
        return relations;
    }
    
    // adds a relation into the catalog storage list
    public static void addToCatalog(Relations THING){
        System.out.println("Adding relation: " + THING.getName() + " to catalog.");// +" with " + THING.columnNum + " columns to catalog.");
        relations.add(THING);
    
    }
    
    // Prints the contents of the catalog (based on lecture 1 slide number 7 (data dictionary)),
    // using string format to neatly space each element of the tables.
    public static void printCatalog(){
        System.out.println("\n          RELATIONS");
        System.out.println("_________________________________");
        System.out.println("| Relation_name | No_of_columns |");
        System.out.println("|---------------|---------------|");
        for(int i = 0; i < relations.size(); i++){// prints the name of each relation in the catalog and how many attributes (columns) they have
            System.out.println(String.format("| %-14s|     %-9s |", relations.get(i).getName(),  relations.get(i).getColumn() ));
        }
        
        System.out.println("\n                  \tCOLUMNS");
        System.out.println("_______________________________________________________");
        System.out.println("|  Column_name  |  Data_type  |  Belongs_to_relation  |");
        System.out.println("|---------------|-------------|-----------------------|");
        for(int i = 0; i < relations.size(); i++){
            LinkedList<Tuple> test = relations.get(i).getSchema();// get the format tuple from each relation in the catalog
            for(int j = 0; j < test.size(); j++){                  
                System.out.println(String.format("| %-13s |  %-10s |  \t%-17s |", test.get(j).getTitle(),
                                         (test.get(j).getType() +"("+  test.get(j).getLength() + ")"),
                                          test.get(j).getRelName() ));
            }    
        }
    }// end of print catalog method

}// end of catalog class