package nosqldetection;


import com.opencsv.bean.CsvBindByName;
import nosqldetection.FeatureVector;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Black_Knight
 */
public class BeanClass {
    @CsvBindByName(column = "Feature0", required = true)
    private int Feature0;

    @CsvBindByName(column = "Feature1", required = true)
    private int Feature1;
    
    @CsvBindByName(column = "Feature2", required = true)
    private int Feature2;
    
    @CsvBindByName(column = "Feature3", required = true)
    private int Feature3;
    
    @CsvBindByName(column = "Feature4", required = true)
    private int Feature4;

    @CsvBindByName(column = "Feature5", required = true)
    private int Feature5;
    
    @CsvBindByName(column = "Feature6", required = true)
    private int Feature6;
    
    @CsvBindByName(column = "Feature7", required = true)
    private int Feature7;
    
    @CsvBindByName(column = "Feature8", required = true)
    private int Feature8;

    @CsvBindByName(column = "Feature9", required = true)
    private int Feature9;
    
    @CsvBindByName(column = "Feature10", required = true)
    private int Feature10;
    
    @CsvBindByName(column = "Feature11", required = true)
    private int Feature11;
    
    @CsvBindByName(column = "Feature12", required = true)
    private int Feature12;

    @CsvBindByName(column = "Feature13", required = true)
    private int Feature13;
    
    @CsvBindByName(column = "Feature14", required = true)
    private int Feature14;
    
    @CsvBindByName(column = "Feature15", required = true)
    private int Feature15;

    @CsvBindByName(column = "Feature16", required = true)
    private int Feature16;

    @CsvBindByName(column = "Feature17", required = true)
    private int Feature17;

   /* @CsvBindByName(column = "Feature16", required = true)
    private int Feature16;

    @CsvBindByName(column = "Feature17", required = true)
    private int Feature17;

    @CsvBindByName(column = "Feature18", required = true)
    private int Feature18;*/
    
    @CsvBindByName(column = "ClassLabel", required = true)
    private int classLabel;
    
    
    public void setFeatures(FeatureVector v, int value)
    {
        classLabel = value;
        Feature0 = v.vector[0];
        Feature1 = v.vector[1];
        Feature2 = v.vector[2];
        Feature3 = v.vector[3];
        Feature4 = v.vector[4];
        Feature5 = v.vector[5];
        Feature6 = v.vector[6];
        Feature7 = v.vector[7];
        Feature8 = v.vector[8];
        Feature9 = v.vector[9];
       /* Feature10 = v.vector[10];
        Feature11 = v.vector[11];
        Feature12 = v.vector[12];
        Feature13 = v.vector[13];
        Feature14 = v.vector[14];
        Feature15 = v.vector[15];
        Feature16 = v.vector[16];
        Feature17 = v.vector[17];*/
       /*

        Feature18 = v.vector[18];*/
    }

    
    /**
    public BeanClass(int Feature0, int Feature1, int Feature2, int Feature3, int Feature4, int Feature5, int Feature6, int Feature7, int Feature8, int Feature9, int Feature10, int Feature11, int Feature12, int Feature13, int Feature14,int label) {
        this.Feature0 = Feature0;
        this.Feature1 = Feature1;
        this.Feature2 = Feature2;
        this.Feature3 = Feature3;
        this.Feature4 = Feature4;
        this.Feature5 = Feature5;
        this.Feature6 = Feature6;
        this.Feature7 = Feature7;
        this.Feature8 = Feature8;
        this.Feature9 = Feature9;
        this.Feature10 = Feature10;
        this.Feature11 = Feature11;
        this.Feature12 = Feature12;
        this.Feature13 = Feature13;
        this.Feature14 = Feature14;
        
        this.classLabel = label;
    }
    */
    
    
    public BeanClass(FeatureVector v, int classLabel)
    {
        setFeatures(v,classLabel);
    }

    public int getFeature0() {
        return Feature0;
    }

    public void setFeature0(int Feature0) {
        this.Feature0 = Feature0;
    }

    public int getFeature1() {
        return Feature1;
    }

    public int getClassLabel() {
        return classLabel;
    }

    public void setClassLabel(int classLabel) {
        this.classLabel = classLabel;
    }
    
    

    public void setFeature1(int Feature1) {
        this.Feature1 = Feature1;
    }

    public int getFeature2() {
        return Feature2;
    }

    public void setFeature2(int Feature2) {
        this.Feature2 = Feature2;
    }

    public int getFeature3() {
        return Feature3;
    }

    public void setFeature3(int Feature3) {
        this.Feature3 = Feature3;
    }

    public int getFeature4() {
        return Feature4;
    }

    public void setFeature4(int Feature4) {
        this.Feature4 = Feature4;
    }

    public int getFeature5() {
        return Feature5;
    }

    public void setFeature5(int Feature5) {
        this.Feature5 = Feature5;
    }

    public int getFeature6() {
        return Feature6;
    }

    public void setFeature6(int Feature6) {
        this.Feature6 = Feature6;
    }

    public int getFeature7() {
        return Feature7;
    }

    public int getFeature15() {
        return Feature15;
    }

    public void setFeature15(int Feature15) {
        this.Feature15 = Feature15;
    }

    public void setFeature7(int Feature7) {
        this.Feature7 = Feature7;
    }

    public int getFeature8() {
        return Feature8;
    }

    public void setFeature8(int Feature8) {
        this.Feature8 = Feature8;
    }

    public int getFeature9() {
        return Feature9;
    }

    public void setFeature9(int Feature9) {
        this.Feature9 = Feature9;
    }

    public int getFeature10() {
        return Feature10;
    }

    public void setFeature10(int Feature10) {
        this.Feature10 = Feature10;
    }

    public int getFeature11() {
        return Feature11;
    }

    public void setFeature11(int Feature11) {
        this.Feature11 = Feature11;
    }

    public int getFeature12() {
        return Feature12;
    }

    public void setFeature12(int Feature12) {
        this.Feature12 = Feature12;
    }

    public int getFeature13() {
        return Feature13;
    }

    public void setFeature13(int Feature13) {
        this.Feature13 = Feature13;
    }

    public int getFeature14() {
        return Feature14;
    }

    public void setFeature14(int Feature14) {
        this.Feature14 = Feature14;
    }

    public int getFeature16() {
        return Feature16;
    }

    public void setFeature16(int feature16) {
        Feature16 = feature16;
    }

    public int getFeature17() {
        return Feature17;
    }

    public void setFeature17(int feature17) {
        Feature17 = feature17;
    }
   /*

    public int getFeature17() {
        return Feature17;
    }

    public void setFeature17(int feature17) {
        Feature17 = feature17;
    }

    public int getFeature18() {
        return Feature18;
    }

    public void setFeature18(int feature18) {
        Feature18 = feature18;
    }*/
}
