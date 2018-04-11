/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wekatest;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;

/**
 *
 * @author Black_Knight
 */
public class WekaTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        CSVLoader loader = new CSVLoader();
        CSVLoader loader1 = new CSVLoader();
        Instances data = null;
        Instances test = null;

        Smote sm = new Smote();
        
        try {
            loader.setSource(new File("training.csv"));
            loader1.setSource(new File("test_old.csv"));

        } catch (IOException ex) {
            Logger.getLogger(WekaTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            data = loader.getDataSet();
            test = loader1.getDataSet();
            ArffSaver saver = new ArffSaver();
            ArffSaver saver1 = new ArffSaver();
            saver.setInstances(data);
            saver1.setInstances(test);
            saver.setFile(new File("test1.arff"));
            saver1.setFile(new File("test2.arff"));
            saver.writeBatch();
            saver1.writeBatch();
        } catch (Exception ex) {
            Logger.getLogger(WekaTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try
        {
            BufferedReader reader = new BufferedReader(
                                  new FileReader("test1.arff"));
            BufferedReader reader1 = new BufferedReader(new FileReader("test2.arff"));
            data = new Instances(reader);
            test = new Instances(reader1);

            reader.close();
            reader1.close();
            
            NumericToNominal convert= new NumericToNominal();
            NumericToNominal convert1= new NumericToNominal();
            String[] options= new String[2];
            options[0]="-R";
            options[1]="1-11";  //range of variables to make numeric


            convert.setOptions(options);
            convert.setInputFormat(data);
            convert1.setOptions(options);
            convert1.setInputFormat(test);
            
            Instances newData = Filter.useFilter(data, convert);
            Instances testData = Filter.useFilter(test, convert1);


            // Smote begins
            Instances smoteData;
            double smotePrc = 250.0;
            int smoteSeed = 10;
            int smoteNeighbour = 2;
            String smoteValue = "0";
            newData.setClassIndex(newData.numAttributes()-1);
            sm.setRandomSeed(smoteSeed);
            sm.setPercentage(smotePrc);
            sm.setNearestNeighbors(smoteNeighbour);
            sm.setClassValue(smoteValue);
            sm.setInputFormat(newData);
            for(int i=0;i<newData.numInstances();i++){
                sm.input(newData.instance(i));
            }
            System.out.println("Smote percentage: "+smotePrc);
            System.out.println("Smote neighbour: "+smoteNeighbour+"\n\n");
            //System.out.println("\n\n\nhahahaha\n\n\n");
            sm.doSMOTE();
            //System.out.println("\n____Smote End____\n");


            smoteData = new Instances(sm.getOutputFormat());
            int count=0;
            int count_1=0;
            ArrayList<String> smotes = new ArrayList<>();
            while(sm.outputPeek() != null){
                Instance temp = sm.output();
                smoteData.add(temp);
                System.out.println(temp);
                if(temp.value(temp.numAttributes()-1) == 1) count_1++;
                smotes.add(temp.toString());
                count++;
            }
            System.out.println("count = "+count);
            System.out.println("count of 1 = "+count_1);
            System.out.println(smoteData.numAttributes()-1);
           // CsvFileWriter.writeCsvFile("test_genetic.csv", smotes);
            // Smote ends

            //sm.output()
            /*PrincipalComponents pca = new PrincipalComponents();
            pca.setMaximumAttributes(15);
            pca.setInputFormat(smoteData);
            Instances pcaData = Filter.useFilter(smoteData, pca);*/
            //System.out.println(pcaData);
           // pca.principalComponentsSummary();
            //SelectAttributes attSel = new SelectAttributes();
            //attSel.selectedFeatures(smoteData);
            Id3Classifier.kFoldValidation(smoteData);
            //RandomForestClassifier.kFoldValidationRF(smoteData);
            //AdaboostClassifier.kFoldValidationAB(smoteData);
            //RandomForestClassifier.testingRF(smoteData, testData);

            //KNeighborClassifier.kFoldValidationK(smoteData);
            //ANN.kFoldValidationANN(smoteData);
            //SVMClassifier.kFoldValidationSMV(smoteData);
            //ANN1.saveWrongInstances(smoteData);

        }
        catch(Exception e)
        {
            Logger.getLogger(WekaTest.class.getName()).log(Level.SEVERE, null, e);
        }
        
        //output.close();
    }
}
