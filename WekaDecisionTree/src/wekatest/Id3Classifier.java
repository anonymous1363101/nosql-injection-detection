/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekatest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import weka.attributeSelection.*;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.meta.AttributeSelectedClassifier;
import weka.classifiers.trees.Id3;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Black_Knight
 */
public class Id3Classifier {

    public static void displayResult(Evaluation eval, BufferedWriter output) throws IOException {

        output.write(eval.toSummaryString("\nResults\n======\n", false));
        //System.out.println(eval.toSummaryString("\nResults\n======\n", false));

        output.write("Precision: " + eval.precision(1)+"\n");
        output.write("Recall: " + eval.recall(1)+"\n");
        output.write("TPR: " + eval.truePositiveRate(1)+"\n");
        output.write("TNR: " + eval.trueNegativeRate(1)+"\n");
        output.write("FPR: " + eval.falsePositiveRate(1)+"\n");
        output.write("FNR: " + eval.falseNegativeRate(1)+"\n");
        output.write("F-Score: " + eval.fMeasure(1)+"\n");
    }

    public static void kFoldValidation(Instances newData) throws IOException, Exception
    {
        BufferedWriter output = null;
        File file = new File("id3_kFold_stat.txt");
        output = new BufferedWriter(new FileWriter(file));

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        output.write(dateFormat.format(date)+"\n");
        double acc=0,prec=0,recall=0,f1=0,error=0, confidencIntervalLower=0,confidencIntervalUpper=0;
        long tPos=0, tNeg=0, fPos=0, fNeg=0;
        int TP=0,TN=0,FP=0,FN=0;
        for(int l=0;l<100;l++){
            double TPR=0, TNR=0, FPR=0, FNR=0;
            TP=0;TN=0;FP=0;FN=0;
            newData.randomize(new Random(System.currentTimeMillis()));
            //System.out.println(newData.numClasses());
            int k = 10;
            int foldSize = (int) Math.round(newData.numInstances() * 0.10);
            int lastFoldSize = newData.numInstances() - foldSize*(k-1);

            Instances folds[] = new Instances[k];
            for(int i=0;i<k;i++)
            {
                if(i!=k-1) folds[i] = new Instances(newData,foldSize*i,foldSize);
                else folds[i] = new Instances(newData,foldSize*i,lastFoldSize);
            }



            for(int i=0;i<k;i++)
            {
                Instances test = new Instances(folds[i]);
                Instances train = null;
                boolean start = true;
                for(int j=0;j<k;j++)
                {
                    if(j==i) continue;

                    if(start)
                    {
                        train = new Instances(folds[j]);
                        start = false;
                        System.out.print("");
                        continue;
                    }

                    for (int t=0;t<folds[j].numInstances();t++) {
                        train.add(folds[j].instance(t));

                    }
                }

                //System.out.println("Train Size: "+train.numInstances());
                //System.out.println("Test Size: "+test.numInstances());
                //Id3 id3 = new Id3();

                //System.out.println(train.toSummaryString());
                train.setClassIndex(train.numAttributes() - 1);
                test.setClassIndex(test.numAttributes() - 1);
                //train.setClassIndex(16);
                //test.setClassIndex(16);


                Id3 classifier = new Id3();
                //classifier.setSeed(100);
                //setParams(classifier);
                //classifier.setClassifier(new J48());

                classifier.buildClassifier(train);

                Evaluation eval = new Evaluation(test);
                eval.evaluateModel(classifier, test);
                // System.out.println(eval.toSummaryString());
                TPR+=eval.truePositiveRate(1);
                TNR+=eval.trueNegativeRate(1);
                FPR+=eval.falsePositiveRate(1);
                FNR+=eval.falseNegativeRate(1);

                TP+=eval.numTruePositives(1);
                TN+=eval.numTrueNegatives(1);
                FP+=eval.numFalsePositives(1);
                FN+=eval.numFalseNegatives(1);

                tPos+=TP;
                tNeg+=TN;
                fPos+=FP;
                fNeg+=FN;

                //System.out.println("TP:"+eval.numTruePositives(1));
                //System.out.println("FP:"+eval.numFalsePositives(1));
                //System.out.println("TN:"+eval.numTrueNegatives(1));
                //System.out.println("FN:"+eval.numFalseNegatives(1));
            }





            acc += (double)(TP+TN)/(TP+TN+FP+FN);
            double err = (double)(FP+FN)/(TP+TN+FP+FN);
            error += err;
            double prc = (double)(TP)/(TP+FP);
            prec += prc;
            double rcl = (double)(TP)/(TP+FN);
            recall += rcl;
            f1 += 2*(rcl*prc)/(rcl+prc);


            double cofidenceConst = 1.96;
            double cl = err - cofidenceConst*(Math.sqrt(err*(1-err))/(TP+TN+FP+FN));
            confidencIntervalLower += cl <0.0?0.0:cl;
            double cu = err + cofidenceConst*(Math.sqrt(err*(1-err))/(TP+TN+FP+FN));
            confidencIntervalUpper += cu> 1.0?1.0:cu;
           // System.out.println(confidencIntervalLower+", "+confidencIntervalUpper);


        }

        System.out.println("TP: "+TP);
        System.out.println("TN: "+TN);
        System.out.println("FP: "+FP);
        System.out.println("FN: "+FN);



        System.out.println("Accuracy: "+acc/100);
        System.out.println("Error: "+error/100);
        System.out.println("Precision: "+prec/100);
        System.out.println("Recall: "+recall/100);
        System.out.println("F1 Score: "+f1/100);
        System.out.println("confidence interval: ("+confidencIntervalLower/100+", "+confidencIntervalUpper/100+")");


        System.out.println("DBG_ID3_END\n\n");

        output.flush();
        output.close();
    }
    
    public static void performanceMeasure(Instances newData) throws Exception
    {
        BufferedWriter output = null;
        File file = new File("id3AvgStat.txt");
        output = new BufferedWriter(new FileWriter(file));
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        output.write(dateFormat.format(date)+"\n");

        int NUMBER_OF_RUNS = 100;
        double TPR=0, TNR=0, FPR=0, FNR=0;
        int TP=0,TN=0,FP=0,FN=0;
        
        for(int i=0;i<NUMBER_OF_RUNS;i++)
        {
            Id3 id3 = new Id3();
            
            newData.randomize(new Random(System.currentTimeMillis()));
            int trainSize = (int) Math.round(newData.numInstances() * 0.70);
            int testSize = newData.numInstances() - trainSize;

            Instances train = new Instances(newData, 0, trainSize);
            Instances test = new Instances(newData, trainSize, testSize);
            
            train.setClassIndex(train.numAttributes() - 1);
            test.setClassIndex(test.numAttributes() - 1);
            
            Id3 classifier = new Id3();
            classifier.buildClassifier(train);
            Evaluation eval = new Evaluation(test);
            eval.evaluateModel(classifier, test);
            
            TPR+=eval.truePositiveRate(1);
            TNR+=eval.trueNegativeRate(1);
            FPR+=eval.falsePositiveRate(1);
            FNR+=eval.falseNegativeRate(1);
            
            TP+=eval.numTruePositives(1);
            TN+=eval.numTrueNegatives(1);
            FP+=eval.numFalsePositives(1);
            TN+=eval.numFalseNegatives(1);
        }
        
        output.write("TPR: "+(TPR/NUMBER_OF_RUNS)+"\n");
        output.write("TNR: "+(TNR/NUMBER_OF_RUNS)+"\n");
        output.write("FPR: "+(FPR/NUMBER_OF_RUNS)+"\n");
        output.write("FNR: "+(FNR/NUMBER_OF_RUNS)+"\n");
        
        double acc,prec,recall,f1;
        acc = (double)(TP+TN)/(TP+TN+FP+FN);
        prec = (double)(TP)/(TP+FP);
        recall = (double)(TP)/(TP+FN);
        f1 = 2*(recall*prec)/(recall+prec);
        
        output.write("Accuracy: "+acc+"\n");
        output.write("Precision: "+prec+"\n");
        output.write("Recall: "+recall+"\n");
        output.write("F1 Score: "+f1+"\n");
        
        output.flush();
        output.close();
    }
    
    public static void classify(Instances newData) throws Exception {
        BufferedWriter output = null;
        try {
            File file = new File("id3Stat.txt");
            output = new BufferedWriter(new FileWriter(file));
            //output.write(text);

            Id3 id3 = new Id3();
            System.out.println("Statistics on Full Dataset: ");

            newData.setClassIndex(newData.numAttributes() - 1);
            //System.out.println(newData.toSummaryString());
            id3.buildClassifier(newData);

            for (int i = 0; i < newData.numInstances(); i++) {
                Instance ins = newData.instance(i);
                int d = (int) id3.classifyInstance(ins);
                //System.out.println(ins + "\t \t" + "Predicted Class: " + d);

                /**
                 * Print incorrect classifications
                 */
                if (d != ins.classValue()) {
                    System.out.println(ins + "\t \t" + "Predicted Class: " + d);
                }
            }

            output.write("Statistics on Full Dataset: ");
            Evaluation eval = new Evaluation(newData);
            eval.evaluateModel(id3, newData);
            displayResult(eval, output);
            

            /**
             * 60% Training, 20% Testing, 20% Validation
             */
            newData.randomize(new java.util.Random(0));
            int trainSize = (int) Math.round(newData.numInstances() * 0.6);
            int testSize = (int) Math.round(newData.numInstances() * 0.2);
            int validationSize = newData.numInstances() - trainSize - testSize;

            Instances train = new Instances(newData, 0, trainSize);
            Instances test = new Instances(newData, trainSize, testSize);
            Instances validate = new Instances(newData, trainSize + testSize, validationSize);

            train.setClassIndex(train.numAttributes() - 1);
            test.setClassIndex(test.numAttributes() - 1);
            validate.setClassIndex(validate.numAttributes() - 1);

            Id3 classifier = new Id3();
            classifier.buildClassifier(train);
            output.write("Statistics on Test (20%) Dataset: ");
            Evaluation eval2 = new Evaluation(test);
            eval2.evaluateModel(classifier, test);
            displayResult(eval2, output);
            //Id3 classifier = new Id3();
            //classifier.buildClassifier(train);
            output.write("Statistics on Validation (20%) Dataset: ");
            Evaluation eval3 = new Evaluation(validate);
            eval3.evaluateModel(classifier, validate);
            displayResult(eval3, output);
            
            /**
             * Without Validation
             */
            
            newData.randomize(new Random(System.currentTimeMillis()));
            trainSize = (int) Math.round(newData.numInstances() * 0.70);
            testSize = newData.numInstances() - trainSize;

            train = new Instances(newData, 0, trainSize);
            test = new Instances(newData, trainSize, testSize);
            
            train.setClassIndex(train.numAttributes() - 1);
            test.setClassIndex(test.numAttributes() - 1);
            
            Id3 classifier2 = new Id3();
            classifier2.buildClassifier(train);
            output.write("Statistics(Without Validation) on Test (25%) Dataset: ");
            Evaluation eval4 = new Evaluation(test);
            eval4.evaluateModel(classifier2, test);
            displayResult(eval4, output);
            
            output.flush();
        } catch (IOException e) {
            System.err.println("Can't open file");
            System.exit(-1);
        }

    }
}
