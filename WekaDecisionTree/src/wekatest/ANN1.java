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
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.trees.Id3;
import weka.core.Instance;
import weka.core.Instances;
import static wekatest.Id3Classifier.displayResult;

/**
 *
 * @author Black_Knight
 */
public class ANN1 {

    public static void displayResult(Evaluation eval, BufferedWriter output) throws IOException {

        output.write(eval.toSummaryString("\nResults\n======\n", false));
        //System.out.println(eval.toSummaryString("\nResults\n======\n", false));
        System.out.println();
        output.write("Precision: " + eval.precision(1) + "\n");
        output.write("Recall: " + eval.recall(1) + "\n");
        output.write("TPR: " + eval.truePositiveRate(1) + "\n");
        output.write("TNR: " + eval.trueNegativeRate(1) + "\n");
        output.write("FPR: " + eval.falsePositiveRate(1) + "\n");
        output.write("FNR: " + eval.falseNegativeRate(1) + "\n");
        output.write("F-Score: " + eval.fMeasure(1) + "\n");
    }

    public static void kFoldValidationANN(Instances newData) throws IOException, Exception {
        BufferedWriter output = null;
        File file = new File("ann_kFold_stat.txt");
        output = new BufferedWriter(new FileWriter(file));

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        output.write(dateFormat.format(date) + "\n");
        double acc = 0, prec = 0, recall = 0, f1 = 0, error = 0, confidencIntervalLower = 0, confidencIntervalUpper = 0;
        for (int l = 0; l < 50; l++) {
            double TPR = 0, TNR = 0, FPR = 0, FNR = 0;
            int TP = 0, TN = 0, FP = 0, FN = 0;
            newData.randomize(new Random(System.currentTimeMillis()));
            //System.out.println(newData.numClasses());
            int k = 10;
            int foldSize = (int) Math.round(newData.numInstances() * 0.10);
            int lastFoldSize = newData.numInstances() - foldSize * (k - 1);

            Instances folds[] = new Instances[k];
            for (int i = 0; i < k; i++) {
                if (i != k - 1) {
                    folds[i] = new Instances(newData, foldSize * i, foldSize);
                } else {
                    folds[i] = new Instances(newData, foldSize * i, lastFoldSize);
                }
            }

            for (int i = 0; i < k; i++) {
                Instances test = new Instances(folds[i]);
                Instances train = null;
                boolean start = true;
                for (int j = 0; j < k; j++) {
                    if (j == i) {
                        continue;
                    }

                    if (start) {
                        train = new Instances(folds[j]);
                        start = false;
                        System.out.print("");
                        continue;
                    }

                    for (int t = 0; t < folds[j].numInstances(); t++) {
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

                MultilayerPerceptron classifier = new MultilayerPerceptron();
                classifier.setSeed(20);
                setParams(classifier);

                classifier.buildClassifier(train);

                Evaluation eval = new Evaluation(test);
                eval.evaluateModel(classifier, test);
                // System.out.println(eval.toSummaryString());
                TPR += eval.truePositiveRate(1);
                TNR += eval.trueNegativeRate(1);
                FPR += eval.falsePositiveRate(1);
                FNR += eval.falseNegativeRate(1);

                TP += eval.numTruePositives(1);
                TN += eval.numTrueNegatives(1);
                FP += eval.numFalsePositives(1);
                FN += eval.numFalseNegatives(1);

                //System.out.println("TP:"+eval.numTruePositives(1));
                //System.out.println("FP:"+eval.numFalsePositives(1));
                //System.out.println("TN:"+eval.numTrueNegatives(1));
                //System.out.println("FN:"+eval.numFalseNegatives(1));
            }

            acc += (double) (TP + TN) / (TP + TN + FP + FN);
            double err = (double) (FP + FN) / (TP + TN + FP + FN);
            error += err;
            double prc = (double) (TP) / (TP + FP);
            prec += prc;
            double rcl = (double) (TP) / (TP + FN);
            recall += rcl;
            f1 += 2 * (rcl * prc) / (rcl + prc);

            double[] consts = {1.64, 1.96, 2.33, 2.58};
            double[] percConst = {90, 95, 98, 99};

            double cofidenceConst = 1.96;
            double cl = err - cofidenceConst * (Math.sqrt(err * (1 - err)) / (TP + TN + FP + FN));
            confidencIntervalLower += cl < 0.0 ? 0.0 : cl;
            double cu = err + cofidenceConst * (Math.sqrt(err * (1 - err)) / (TP + TN + FP + FN));
            confidencIntervalUpper += cu > 1.0 ? 1.0 : cu;
            System.out.println(confidencIntervalLower + ", " + confidencIntervalUpper);

        }

        System.out.println("Accuracy: " + acc / 50);
        System.out.println("Error: " + error / 50);
        System.out.println("Precision: " + prec / 50);
        System.out.println("Recall: " + recall / 50);
        System.out.println("F1 Score: " + f1 / 50);
        System.out.println("confidence interval: (" + confidencIntervalLower / 50 + ", " + confidencIntervalUpper / 50 + ")");

        System.out.println("DBG_ANN_END\n\n");

        output.flush();
        output.close();
    }

//    public static String InstanceToString(Instance instance) {
//        String str = "";
//        str = instance.toString();
//        return str;
//    }

    public static void saveWrongInstances(Instances newData) throws IOException, Exception {
        BufferedWriter output = null;
        File file = new File("wrongClassification.txt");
        output = new BufferedWriter(new FileWriter(file));

        newData.randomize(new Random(System.currentTimeMillis()));
        //System.out.println(newData.numClasses());
        int k = 10;
        int foldSize = (int) Math.round(newData.numInstances() * 0.10);
        int lastFoldSize = newData.numInstances() - foldSize * (k - 1);

        Instances folds[] = new Instances[k];
        for (int i = 0; i < k; i++) {
            if (i != k - 1) {
                folds[i] = new Instances(newData, foldSize * i, foldSize);
            } else {
                folds[i] = new Instances(newData, foldSize * i, lastFoldSize);
            }
        }

        for (int i = 0; i < k; i++) {
            Instances test = new Instances(folds[i]);
            Instances train = null;
            boolean start = true;
            for (int j = 0; j < k; j++) {
                if (j == i) {
                    continue;
                }

                if (start) {
                    train = new Instances(folds[j]);
                    start = false;
                    System.out.print("");
                    continue;
                }

                for (int t = 0; t < folds[j].numInstances(); t++) {
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

            MultilayerPerceptron classifier = new MultilayerPerceptron();
            classifier.setSeed(20);
            setParams(classifier);

            classifier.buildClassifier(train);
            
            //Evaluation eval = new Evaluation(test);
            //eval.evaluateModel(classifier, test);
            
            for (int x=0;x<test.numInstances();x++) {
                //System.out.println(eval.)
                double result = classifier.classifyInstance(test.instance(x));
                double label = test.instance(x).classValue();
                //System.out.println(result+"||"+label);
                if(result!=label)
                {
                    output.write(test.instance(x).toString()+"\n");
                }
            }

            
                // System.out.println(eval.toSummaryString());

        }

        output.flush();
        output.close();
    }

    public static void performanceMeasure(Instances newData) throws Exception {
        BufferedWriter output = null;
        File file = new File("AnnAvgStat.txt");
        output = new BufferedWriter(new FileWriter(file));

        int NUMBER_OF_RUNS = 100;
        double TPR = 0, TNR = 0, FPR = 0, FNR = 0;
        int TP = 0, TN = 0, FP = 0, FN = 0;

        for (int i = 0; i < NUMBER_OF_RUNS; i++) {

            newData.randomize(new Random(System.currentTimeMillis()));
            int trainSize = (int) Math.round(newData.numInstances() * 0.70);
            int testSize = newData.numInstances() - trainSize;

            Instances train = new Instances(newData, 0, trainSize);
            Instances test = new Instances(newData, trainSize, testSize);

            train.setClassIndex(train.numAttributes() - 1);
            test.setClassIndex(test.numAttributes() - 1);

            MultilayerPerceptron classifier = new MultilayerPerceptron();
            setParams(classifier);

            classifier.buildClassifier(train);
            Evaluation eval = new Evaluation(test);
            eval.evaluateModel(classifier, test);

            TPR += eval.truePositiveRate(1);
            TNR += eval.trueNegativeRate(1);
            FPR += eval.falsePositiveRate(1);
            FNR += eval.falseNegativeRate(1);

            TP += eval.numTruePositives(1);
            TN += eval.numTrueNegatives(1);
            FP += eval.numFalsePositives(1);
            TN += eval.numFalseNegatives(1);
        }

        output.write("TPR: " + (TPR / NUMBER_OF_RUNS) + "\n");
        output.write("TNR: " + (TNR / NUMBER_OF_RUNS) + "\n");
        output.write("FPR: " + (FPR / NUMBER_OF_RUNS) + "\n");
        output.write("FNR: " + (FNR / NUMBER_OF_RUNS) + "\n");

        double acc, prec, recall, f1;
        acc = (double) (TP + TN) / (TP + TN + FP + FN);
        prec = (double) (TP) / (TP + FP);
        recall = (double) (TP) / (TP + FN);
        f1 = 2 * (recall * prec) / (recall + prec);

        output.write("Accuracy: " + acc + "\n");
        output.write("Precision: " + prec + "\n");
        output.write("Recall: " + recall + "\n");
        output.write("F1 Score: " + f1 + "\n");

        output.flush();
        output.close();
    }

    public static void setParams(MultilayerPerceptron mlp) {
        mlp.setLearningRate(0.05);
        mlp.setMomentum(0.2);
        mlp.setTrainingTime(2000);
        mlp.setHiddenLayers("a");
    }

    public static void classify(Instances newData) throws Exception {
        File file = new File("AnnStat.txt");
        BufferedWriter output = new BufferedWriter(new FileWriter(file));

        newData.setClassIndex(newData.numAttributes() - 1);
        MultilayerPerceptron mlp = new MultilayerPerceptron();

        setParams(mlp);
        mlp.buildClassifier(newData);

        Evaluation eval = new Evaluation(newData);
        eval.evaluateModel(mlp, newData);
        displayResult(eval, output);

        output.flush();
        output.close();
    }
}
