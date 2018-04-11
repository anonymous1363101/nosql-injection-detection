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
public class FeatureVector {
    public static final int NUM_FEATURES = 10;//9;//18;
    public int vector[] = new int[NUM_FEATURES];
    String query;
    
    public FeatureVector(String q)
    {
        query = q;//1,2,4,8,(9/14),10,11,15,16
        FeatureExtractor fe = new FeatureExtractor(query);
        //vector[0] = fe.isNullComparison();
        vector[0] = fe.containsEmptyString();
        vector[1] = fe.containsPayload();
        //vector[3] = fe.containsEndQuery();
        vector[2] = fe.containsNotEqual();
        //vector[5] = fe.doesTargetTable();
        //vector[6] = fe.doesAlterCollection();
        //vector[7] = fe.doesDropDatabase();
        vector[3] = fe.containsCondition();
        vector[4] = fe.containsLogicalOp();
        vector[5] = fe.evalQueryOperations();
        vector[6] = fe.containsReturn();
        //vector[12] = fe.isUpdateQuery();
        //vector[13] = fe.isRemoveQuery();
        //vector[14] = fe.isProjectionQuery();
        //vector[14] = fe.containsLimit();
        vector[7] = fe.containsRegexTrue();
        vector[8] = fe.isNewQuery();
        //new
        vector[9] = fe.elementQueryOperations();
                //new
        //vector[17] = fe.isWhileTrue();
       /* vector[16] = fe.containsEmptyArray();

        vector[18] = fe.isWhileTrue();*/
    }
    
    public void print()
    {
        for(int i=0;i<vector.length;i++)
        {
            System.out.print(vector[i]+"\t,");
        }
        System.out.println();
    }
}
