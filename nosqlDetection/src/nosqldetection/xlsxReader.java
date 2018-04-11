/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nosqldetection;

import com.opencsv.CSVWriter;
import com.opencsv.bean.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Black_Knight
 */
public class xlsxReader {
    private String fileName;
    List beans = new ArrayList<BeanClass>();
    
    public xlsxReader(String fName)
    {
        fileName = fName;
        try {
            Scanner input = new Scanner(new BufferedInputStream(new FileInputStream(new File(fileName)), 20*1024*1024), "utf-8");

            int count_1 = 0;
            int count_0 = 0;
            while(input.hasNext())
            {
                String temp = input.nextLine();
                //System.out.println(temp);
                
                String arr[] = temp.split("::::");
                DataRow row = new DataRow(arr[0], Integer.parseInt(arr[1]));

                if(Integer.parseInt(arr[1])== 1){
                    count_1++;
                    System.out.println(Integer.parseInt(arr[1]));
                }
                else count_0++;

                
                BeanClass bean = new BeanClass(row.vector, row.getValue());
                beans.add(bean);
                
                NosqlDetection.dataset.add(row);
            }
            System.out.println("Total 1 = "+count_1);
            System.out.println("Total 0 = "+count_0);
            CsvFileWriter.writeCsvFile("test.csv", beans);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(xlsxReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
