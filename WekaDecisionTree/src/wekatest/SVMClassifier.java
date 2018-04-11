package wekatest;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.SMO;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.trees.J48;
import weka.core.Instances;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by ASUS on 11/21/2017.
 */
public class SVMClassifier {
    public static void kFoldValidationSMV(Instances newData) throws IOException, Exception
    {
        BufferedWriter output = null;
        File file = new File("smv_kFold_stat.txt");
        output = new BufferedWriter(new FileWriter(file));

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        output.write(dateFormat.format(date)+"\n");
        double acc=0,prec=0,recall=0,f1=0,error=0, confidencIntervalLower=0,confidencIntervalUpper=0;
        for(int l=0;l<100;l++){
            double TPR=0, TNR=0, FPR=0, FNR=0;
            int TP=0,TN=0,FP=0,FN=0;
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


                LibSVM classifier = new LibSVM();
                classifier.setSeed(50);
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
            //System.out.println(confidencIntervalLower+", "+confidencIntervalUpper);


        }





        System.out.println("Accuracy: "+acc/100);
        System.out.println("Error: "+error/100);
        System.out.println("Precision: "+prec/100);
        System.out.println("Recall: "+recall/100);
        System.out.println("F1 Score: "+f1/100);
        System.out.println("confidence interval: ("+confidencIntervalLower/100+", "+confidencIntervalUpper/100+")");


        System.out.println("DBG_SMV_END\n\n");

        output.flush();
        output.close();
    }
}
