/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nosqldetection;

/**
 *
 * @author Black_Knight
 */
public class DataRow {
    private String query;
    private int value;
    
    FeatureVector vector;
    
    public DataRow(String q, int v)
    {
        query = q;
        value = v;
        vector = new FeatureVector(query);
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getValue() {
        return value;
    }
    
    public void print()
    {
        System.out.println("Query: "+query);
        System.out.println("Label: "+value);
        System.out.println("---");
    }

    public void setValue(int value) {
        this.value = value;
    }
    
    
}
