package wekatest;

import weka.attributeSelection.*;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ASUS on 11/10/2017.
 */
public class SelectAttributes {
    public SelectAttributes() {
    }

    public void selectedFeatures(Instances instances){
        AttributeSelection attributeSelection = new AttributeSelection();
        InfoGainAttributeEval cfsEval = new InfoGainAttributeEval();
        //ClassifierSubsetEval cfsEval = new ClassifierSubsetEval();
        //CfsSubsetEval cfsEval = new CfsSubsetEval();
        Ranker search = new Ranker();
        //GreedyStepwise search = new GreedyStepwise();
        search.setGenerateRanking(true);
        //cfsEval.setClassifier(new J48());
        //search.setNumToSelect(9);
       // search.setSearchBackwards(true);
        //search.setConservativeForwardSelection(true);

       // cfsEval.setClassifier(new J48());

        attributeSelection.setEvaluator(cfsEval);
        attributeSelection.setSearch(search);
        try {
            attributeSelection.setSeed(10);
            attributeSelection.setFolds(10);

            attributeSelection.setRanking(true);

            attributeSelection.setXval(true);
            //attributeSelection.CrossValidateAttributes();
            attributeSelection.SelectAttributes(instances);

            System.out.println(attributeSelection.toResultsString());
            int[] indices = attributeSelection.selectedAttributes();
            double[][] d = attributeSelection.rankedAttributes();
            for(int i=0; i< indices.length; i++){
                System.out.println(indices[i]);
            }
            for(int i=0;i<d.length;i++){
                for(int j=0;j<d.length;j++){
                    System.out.print(d[i][j]+" ");
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
