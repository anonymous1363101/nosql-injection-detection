/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nosqldetection;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ashraf
 * 
 */
public class CsvFileWriter {
	
	//Delimiter used in CSV file
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";
	
	//CSV file header
	private static final String FILE_HEADER = "Feature0,Feature1,Feature2,Feature3,Feature4,Feature5,Feature6,Feature7,Feature8,Feature9,ClassLabel";//,Feature9,Feature10,Feature11,Feature12,Feature13,Feature14,Feature15,Feature16,Feature17,ClassLabel";//Feature16,Feature17,Feature18,

	public static void writeCsvFile(String fileName, List beans) {
		/**
		//Create new students objects
		BeanClass bean1 = new BeanClass(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0);
		BeanClass bean2 = new BeanClass(1,1,0,0,1,1,1,1,1,1,1,1,1,1,0,1);
		
		//Create a new list of student objects
		List beans = new ArrayList();
		beans.add(bean1);
                beans.add(bean2);
		*/
		FileWriter fileWriter = null;
				
		try {
			fileWriter = new FileWriter(fileName);

			//Write the CSV file header
			fileWriter.append(FILE_HEADER.toString());
			
			//Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);
			
			//Write a new student object list to the CSV file
			for (Object bean: beans) {
                            BeanClass student = (BeanClass) bean;
				fileWriter.append(String.valueOf(student.getFeature0()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(student.getFeature1()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(student.getFeature2()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(student.getFeature3()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(student.getFeature4()));
                                fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(student.getFeature5()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(student.getFeature6()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(student.getFeature7()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(student.getFeature8()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(student.getFeature9()));
				fileWriter.append(COMMA_DELIMITER);
				/*fileWriter.append(String.valueOf(student.getFeature10()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(student.getFeature11()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(student.getFeature12()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(student.getFeature13()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(student.getFeature14()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(student.getFeature15()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(student.getFeature16()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(student.getFeature17()));
				fileWriter.append(COMMA_DELIMITER);*/
				/*
				fileWriter.append(String.valueOf(student.getFeature16()));
                fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(student.getFeature17()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(student.getFeature18()));
				fileWriter.append(COMMA_DELIMITER);*/
				fileWriter.append(String.valueOf(student.getClassLabel()));
                                
				fileWriter.append(NEW_LINE_SEPARATOR);
			}

			
			
			System.out.println("CSV file was created successfully !!!");
			
		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {
			
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
			}
			
		}
	}
}