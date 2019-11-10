
import javax.management.relation.Relation;
import java.util.*;
import java.lang.*;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class Join{

    private static final int TAB1_INDEX = 3;
    private static final int TAB2_INDEX = 4;
    private static final int NAME = 0;
    private static final int COND_TABLE1 = 6;
    private static final int COND_TABLE2 = 8;
    private static final int FIRST_TABLE = 0;
    private static final int SECOND_TABLE = 1;
    private static final int MAX_TABLE_TOJOIN = 2;
    private static final int T = 1;
    private static final int F =0 ;

    //empty cons
    public Join(){ }

    /* join_tables()
     * driver function for joining tables*/
    public void join_tables(String[] str, Surly sur){
        ReadFile r = new ReadFile();
        Relations[] table = retrieve_relations(str, sur, r);
        if(valid_tables(str, sur, r)) {
           combine_tables(table, str, sur, r);
        }
    }

    /* combine_tables()
     * checks which method to call based on tables' size of tuples */
    void combine_tables(Relations[] table, String[] str, Surly sur, ReadFile r){
        int tot_numcol = table[FIRST_TABLE].getColumn() + table[SECOND_TABLE].getColumn();
        int table1_tupsize = table[FIRST_TABLE].getTuples().size();
        int table2_tupsize = table[SECOND_TABLE].getTuples().size();

        if(table1_tupsize == table2_tupsize) {
            com_even_table(table[FIRST_TABLE], table[SECOND_TABLE], tot_numcol,(str[NAME]), sur, r);
        }
        else {
            com_uneven_table(table[FIRST_TABLE], table[SECOND_TABLE], tot_numcol,r.cleanName(str[NAME]), str, sur, r);
        }
    }

    /*  retrieve_relations()
     *  temporarily stores the 2 tables in an array of relations */
    public Relations[] retrieve_relations(String[] str, Surly sur, ReadFile r){

        Relations relation[] = new Relations[MAX_TABLE_TOJOIN];
        relation[FIRST_TABLE] = sur.getRelation(r.cleanName(str[TAB1_INDEX]));
        relation[SECOND_TABLE] = sur.getRelation(r.cleanName(str[TAB2_INDEX]));

        return relation;
    }

    /* valid_tables()
     * validates table existence specifies which table doesn't exist */
    public boolean valid_tables(String[] str, Surly sur, ReadFile r){
        boolean exists = false;
        if (sur.isRelation(r.cleanName(str[TAB1_INDEX])) == T && sur.isRelation(r.cleanName(str[TAB2_INDEX])) == T)
            exists = true;
        else{
            if(sur.isRelation(r.cleanName(str[TAB1_INDEX])) == F && sur.isRelation(r.cleanName(str[TAB2_INDEX])) == F)
                print("TABLE " + str[TAB1_INDEX] + " AND TABLE " + r.cleanName(str[TAB2_INDEX]) + " not in database");
            else if(sur.isRelation(r.cleanName(str[TAB1_INDEX])) == F)
                print("TABLE " + str[TAB1_INDEX] + " doesnt exist can't execute join");
            else
                print("TABLE " + r.cleanName(str[TAB2_INDEX]) + " doesn't exist can't execute join");
        }
        return exists;
    }

    /*  com_even_table()
     *  Call if both tables have same size tuple (rows) */
    public void com_even_table(Relations t1, Relations t2, int tot_col, String tabName, Surly sur, ReadFile r){
        LinkedList<Tuple> fresh_tup = new LinkedList<Tuple>();
        Relations new_tempR;
        LinkedList<Attribute> new_att;

        LinkedList<Tuple> newSchema = set_up_schema(t1, t2);

        for(int i=0; i<t1.getTuples().size(); i++){
            new_att = new LinkedList<Attribute>();
            no_padding(i, t1.getColumn(), t2.getColumn(), new_att, t1,t2);
            Tuple new_tup = new Tuple(new_att);
            fresh_tup.add(new_tup);
        }
        String table_name = r.cleanName(tabName);
        new_tempR = new Relations(table_name, tot_col, newSchema, fresh_tup);
        sur.addTemp(new_tempR);
    }

    /* com_uneven_table()
     * combines tables with different different size tuples  */
    public void com_uneven_table(Relations t1, Relations t2, int tot_col, String tabName, String[] str, Surly sur, ReadFile r){
        LinkedList<Tuple> fresh_tup = new LinkedList<Tuple>();
        LinkedList<Attribute> new_att;
        LinkedList<Tuple> newSchema = set_up_schema(t1, t2);
        Relations[] rel = {t1, t2};
        int max_tup = get_maxtuple(rel, str, r);
        if(max_tup < 0){
            print("attribute link not in schema");
        }
        for (int i = 0; i < max_tup; i++) {
            new_att = new LinkedList<>();
            if(left_tableHeavy(rel) > 0){
                new_att = table2_padded(i, t1.getColumn(), t2.getColumn(), t1, t2);
            } else {
                new_att = table1_padded(i, t1.getColumn(), t2.getColumn(), t1, t2);
            }
        Tuple new_tup = new Tuple(new_att);
        fresh_tup.add(new_tup);
        }
        Relations new_tempR = new Relations(tabName, tot_col, newSchema, fresh_tup);
        sur.addTemp(new_tempR);
    }

    /* helper to that determines which table has more tuples */
    public static int left_tableHeavy(Relations[] rel){
        return rel[FIRST_TABLE].getTuples().size() - rel[SECOND_TABLE].getTuples().size();
    }

    /* get_maxtuple()
     * returns the boundary size of tuple for both tables */
    public int get_maxtuple(Relations[] rel, String[] str, ReadFile r){

        int tupsize_table1 = rel[FIRST_TABLE].getTuples().size();
        int tupsize_table2 = rel[SECOND_TABLE].getTuples().size();
        int max_tup;

        if(hasCondition(str) && has_attrib_toConnect(str, rel, r)) {
            max_tup = tupsize_table1 > tupsize_table2 ? tupsize_table2 : tupsize_table1;
        }
        else{
             max_tup = -1;
        }
        if(!hasCondition(str)){
            max_tup = tupsize_table1 > tupsize_table2 ? tupsize_table1 : tupsize_table2;
        }
        return max_tup;
    }

    /* table2_padded()
     * handles when table 1 has more tuples than table 2 empty attributes is padded with null */
    public LinkedList<Attribute> table2_padded(int i, int numCol1, int numCol2, Relations t1, Relations t2){
        LinkedList<Tuple> tup1 = t1.getTuples();
        LinkedList<Tuple> tup2 = t2.getTuples();
        LinkedList<Attribute> newatt = new LinkedList<Attribute>();
        Attribute att = new Attribute();

        for(int j=0; j<numCol1; j++){
            newatt.add(tup1.get(i).getAttributes().get(j));
        }
        if(i >= tup2.size()){
            for (int k = 0; k < numCol2; k++) {
                att.setChar("null", 5);
                newatt.add(att);
            }
        } else {
            for (int j = 0; j < numCol2; j++) {
                newatt.add(tup2.get(i).getAttributes().get(j));
            }
        }
        return newatt;
    }


    /* table1_padded()
     * handles when table2 has more tuples than table1 empty attributes is padded with null  */
    public LinkedList<Attribute> table1_padded(int i, int numCol1, int numCol2, Relations t1, Relations t2){
        LinkedList<Tuple> tup1 = t1.getTuples();
        LinkedList<Tuple> tup2 = t2.getTuples();
        LinkedList<Attribute> newatt = new LinkedList<Attribute>();
        Attribute att = new Attribute();
        if(i >= tup1.size()){
            for (int k = 0; k < numCol1; k++) {
                att.setChar("null", 5);
                newatt.add(att);
            }
        } else{
            for (int j = 0; j < numCol1; j++) {
                newatt.add(tup1.get(i).getAttributes().get(j));
            }
        }
        for (int j = 0; j < numCol2; j++) {
            newatt.add(tup2.get(i).getAttributes().get(j));
        }
        return newatt;
    }


    /* no_padding()
     * handles attributes for tables with equal size tuple */
    public void no_padding(int i, int numCol1, int numCol2, LinkedList<Attribute> newatt, Relations t1, Relations t2){
        LinkedList<Tuple> tup1 = t1.getTuples();
        LinkedList<Tuple> tup2 = t2.getTuples();

        for(int j=0; j<numCol1; j++){
            newatt.add(tup1.get(i).getAttributes().get(j));
        }
        for(int j=0; j<numCol2; j++){
            newatt.add(tup2.get(i).getAttributes().get(j));
        }

    }

     /* hasCondition()
      * returns true if join has a condition to follow*/
    public boolean hasCondition(String[] str){
        boolean cond_exists = false;

        for(String s : str){
            if(s.contains("ON")) {
                cond_exists = true;
                break;
            }
        }
        return cond_exists;
    }

    /* has_attrib_toConnect()
     * returns true if the connecting attributes are valid  */
    public boolean has_attrib_toConnect(String[] str, Relations[] rel, ReadFile r) {

        boolean attribute_good = false;
        String s1 = str[COND_TABLE1].replaceAll("(?=^.)[A-Za-z0-9]*[.]", "");
        String s2 = str[COND_TABLE2].replaceAll("(?=^.)[A-Za-z0-9]*[.]", "");

        if(r.cleanName(s1).equals(r.cleanName(s2)) || (exists_inSchema(rel, r.cleanName(s1), r) &&
                exists_inSchema(rel, r.cleanName(s2), r))){
            attribute_good = true;

        }
        return attribute_good;
    }


    /*  set_up_schema()  */
    LinkedList<Tuple> set_up_schema(Relations t1, Relations t2){
        LinkedList<Tuple> sch1 = t1.getSchema();
        LinkedList<Tuple> sch2 = t2.getSchema();

        for(int i=0; i<sch2.size(); i++) {
            sch1.add(sch2.get(i));
        }
        return sch1;
    }

    /*  exists_inSchema()
     * returns true if the attributes to link exists in combined schema table */
    public boolean exists_inSchema(Relations[] rel, String str, ReadFile r){

        boolean isValid_att = false;
        LinkedList<Tuple> sch = set_up_schema(rel[FIRST_TABLE], rel[SECOND_TABLE]);

       for(int i = 0; i< sch.size(); i++) {
            if(sch.get(i).getTitle().equals(r.cleanName(str)))
                isValid_att = true;
       }

        return isValid_att;
    }

    /* utility : print stuff faster */
    public static void print(String string){
        System.out.println(string);
    }
}
