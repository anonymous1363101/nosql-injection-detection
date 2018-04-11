/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nosqldetection;

import java.util.ArrayList;

/**
 *
 * @author Black_Knight
 */
public class NosqlDetection {

    public static String datasetFile = "test_dataset.txt";
    public static ArrayList<DataRow> dataset = new ArrayList<DataRow>();
    
    /**
     * @param args the command line arguments
     */

    public static void main(String[] args) {
        // TODO code application logic here
        xlsxReader reader = new xlsxReader(datasetFile);
        //printDataset();
        //CsvFileWriter.writeCsvFile("hello.csv");
    }
    
    public static void printDataset()
    {
        for(int i=0;i<dataset.size();i++)
        {
            DataRow row = dataset.get(i);
            row.print();
        }
    }
    
}
