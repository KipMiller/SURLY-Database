// CSCI 330 SURLY 2
// Authors: Maria Adams, Mario Gonzalez, Chris Miller
import java.util.*;
import java.lang.*;
import java.io.*;

public class Project{

    private int TARG_ATT = 5;
    public void project(String[] str){
        System.out.println(" "); //for formating purposes
        Surly projectSur = new Surly();
        ReadFile r = new ReadFile();
        String relName = r.cleanName(str[str.length - 1]); //str.length - 1 will always be the relation name
        Relations targetRelation = null;
        Relations newProjectRelation = new Relations(); //add this to temporary relation database
        String tempRelName = r.cleanName(str[0]);

        if(projectSur.isRelation(tempRelName) == 1){
            System.out.println("ERROR: Variable name '" + relName + "' is already a Relation in the DB...");
            return;
        }
        if(projectSur.isTemp(relName) == 1){// if the target is a temp
            targetRelation = projectSur.getTemp(relName);
        }
        else{//if the target is NOT a temp and is a regular relation
            targetRelation = projectSur.getRelation(relName);
        }
        LinkedList<Tuple> schema = targetRelation.getSchema();
        LinkedList<Tuple> tempSchema = new LinkedList<Tuple>(); //temporary relation tuples
        int numSchema = schema.size();

        LinkedList<Tuple> tuples = targetRelation.getTuples();
        LinkedList<Tuple> tempTuples = new LinkedList<Tuple>();
        int numTuples = tuples.size();

        String[] desiredAttributes = new String[str.length - TARG_ATT]; //-5 to account for temporary Name, PROJECT, '=',FROM and RELATION NAME
        String[] storeAttirbutes = new String[desiredAttributes.length]; //store desired attributes to make new tuple

        int[] attributeIndex = new int[desiredAttributes.length]; //store the "index" of each attribute from the schema
        int attIndex = 0; //only used to move along attributeIndex array

        for(int i = 3; i < str.length - 2; i++){//populate the array with the dessired attributes to project
            desiredAttributes[attIndex] = r.cleanName(str[i]);
            attIndex++;
        }

        for(int j = 0; j < desiredAttributes.length;j++){ //get index of each desired index from the schema and create new temporary relation schema
            for(int k = 0; k < numSchema; k++){
                String tempAttributes = schema.get(k).getTitle();
                if(desiredAttributes[j].equals(tempAttributes)){
                    tempSchema.add(schema.get(k)); //add to new schema
                    attributeIndex[j] = k;
                }
            }
        }
        System.out.println(" ");
        for(int i = 0; i < numTuples; i++){// iterate through all the tuples and create new tuples
            Tuple newT = new Tuple();
            LinkedList<Attribute> newTuple = new LinkedList<Attribute>(); //new set of attributes to populate for project
            LinkedList<Attribute> currentTupleAttributes = tuples.get(i).getAttributes(); //get the current tuples attributes

            for(int a = 0; a < attributeIndex.length; a++){//loop through the indicies of the attribute index and add to new tuples
                newTuple.add(currentTupleAttributes.get(attributeIndex[a]));
            }
            newT.addAttributes(newTuple);
            tempTuples.add(newT);
        }

        LinkedList<Tuple> finalTuples = findDuplicates(tempTuples);

        Relations newProjRelation = new Relations(tempRelName,desiredAttributes.length,tempSchema,finalTuples);
        projectSur.addTemp(newProjRelation);
    }//end of project function

    public LinkedList<Tuple> findDuplicates(LinkedList<Tuple> dupTuples){
        LinkedList<Tuple> finTuples = new LinkedList<Tuple>();

        for(int i = 0; i < dupTuples.size();i++){ //for each tuple
            boolean same = false;
                LinkedList<Attribute> currentTupleAtts = dupTuples.get(i).getAttributes();
            if(i == dupTuples.size()-1){ //last line will check itself and consider itself a "duplicate" so
                same = false;
            }
            for(int j = i + 1;j < dupTuples.size();j++){ //for each other tuple
                LinkedList<Attribute> compareAtt = dupTuples.get(j).getAttributes();
                if(compareAtt.get(0).getChar() != null){
                    String comp1 = compareAtt.get(0).getChar();
                    String comp2 =  currentTupleAtts.get(0).getChar();
                    if(comp1.equals(comp2)){ //if the first char attributes match
                        same = checkTupleForDuplicates(currentTupleAtts,compareAtt); //if an char attribute is different
                    }
                }
                else{
                    int compNum1 = compareAtt.get(0).getNum();
                    int compNum2 = currentTupleAtts.get(0).getNum();
                    if(compNum1 == compNum2){ //if the first num attributes match
                        same = checkTupleForDuplicates(currentTupleAtts,compareAtt);//if a num attribute is different
                    }
                }
            }
            if(same != true){
                finTuples.add(dupTuples.get(i));
            }
        }
        return finTuples;
    }


    public boolean checkTupleForDuplicates(LinkedList<Attribute> listOne,LinkedList<Attribute> listTwo){
        boolean identical = true;
        for(int i = 0; i < listOne.size();i++){
            if(listOne.get(i).getChar() != null){
                String comp1 = listOne.get(i).getChar();
                String comp2 =  listTwo.get(i).getChar();
                if(!(comp1.equals(comp2))){ //if the attributes are not equal,
                    identical = false; //if an char attribute is different
                }
            }
            else{
                int compNum1 = listOne.get(i).getNum();
                int compNum2 = listTwo.get(i).getNum();
                if(compNum1 != compNum2){
                    identical = false;//if a num attribute is different
                }
            }
        }
        return identical;
    }

} //end of Project Class
