package wekatest;

/**
 * Created by ASUS on 10/31/2017.
 */

import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import weka.core.Attribute;
import weka.core.Capabilities;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Option;
import weka.core.OptionHandler;
import weka.core.RevisionUtils;
import weka.core.TechnicalInformation;
import weka.core.TechnicalInformationHandler;
import weka.core.Utils;
import weka.core.Capabilities.Capability;
import weka.core.TechnicalInformation.Field;
import weka.core.TechnicalInformation.Type;
import weka.filters.Filter;
import weka.filters.SupervisedFilter;

public class Smote extends Filter implements SupervisedFilter, OptionHandler, TechnicalInformationHandler {
    static final long serialVersionUID = -1653880819059250364L;
    protected int m_NearestNeighbors = 5;
    protected int m_RandomSeed = 1;
    protected double m_Percentage = 100.0D;
    protected String m_ClassValueIndex = "0";
    protected boolean m_DetectMinorityClass = true;

    public Smote() {
    }

    public String globalInfo() {
        return "Resamples a dataset by applying the Synthetic Minority Oversampling TEchnique (SMOTE). The original dataset must fit entirely in memory. The amount of SMOTE and number of nearest neighbors may be specified. For more information, see \n\n" + this.getTechnicalInformation().toString();
    }

    public TechnicalInformation getTechnicalInformation() {
        TechnicalInformation result = new TechnicalInformation(Type.ARTICLE);
        result.setValue(Field.AUTHOR, "Nitesh V. Chawla et. al.");
        result.setValue(Field.TITLE, "Synthetic Minority Over-sampling Technique");
        result.setValue(Field.JOURNAL, "Journal of Artificial Intelligence Research");
        result.setValue(Field.YEAR, "2002");
        result.setValue(Field.VOLUME, "16");
        result.setValue(Field.PAGES, "321-357");
        return result;
    }

    public String getRevision() {
        return RevisionUtils.extract("$Revision: 9657 $");
    }

    public Capabilities getCapabilities() {
        Capabilities result = super.getCapabilities();
        result.disableAll();
        result.enableAllAttributes();
        result.enable(Capability.MISSING_VALUES);
        result.enable(Capability.NOMINAL_CLASS);
        result.enable(Capability.MISSING_CLASS_VALUES);
        return result;
    }

    public Enumeration listOptions() {
        Vector newVector = new Vector();
        newVector.addElement(new Option("\tSpecifies the random number seed\n\t(default 1)", "S", 1, "-S <num>"));
        newVector.addElement(new Option("\tSpecifies percentage of SMOTE instances to create.\n\t(default 100.0)\n", "P", 1, "-P <percentage>"));
        newVector.addElement(new Option("\tSpecifies the number of nearest neighbors to use.\n\t(default 5)\n", "K", 1, "-K <nearest-neighbors>"));
        newVector.addElement(new Option("\tSpecifies the index of the nominal class value to SMOTE\n\t(default 0: auto-detect non-empty minority class))\n", "C", 1, "-C <value-index>"));
        return newVector.elements();
    }

    public void setOptions(String[] options) throws Exception {
        String seedStr = Utils.getOption('S', options);
        if(seedStr.length() != 0) {
            this.setRandomSeed(Integer.parseInt(seedStr));
        } else {
            this.setRandomSeed(1);
        }

        String percentageStr = Utils.getOption('P', options);
        if(percentageStr.length() != 0) {
            this.setPercentage((new Double(percentageStr)).doubleValue());
        } else {
            this.setPercentage(100.0D);
        }

        String nnStr = Utils.getOption('K', options);
        if(nnStr.length() != 0) {
            this.setNearestNeighbors(Integer.parseInt(nnStr));
        } else {
            this.setNearestNeighbors(5);
        }

        String classValueIndexStr = Utils.getOption('C', options);
        if(classValueIndexStr.length() != 0) {
            this.setClassValue(classValueIndexStr);
        } else {
            this.m_DetectMinorityClass = true;
        }

    }

    public String[] getOptions() {
        Vector result = new Vector();
        result.add("-C");
        result.add(this.getClassValue());
        result.add("-K");
        result.add("" + this.getNearestNeighbors());
        result.add("-P");
        result.add("" + this.getPercentage());
        result.add("-S");
        result.add("" + this.getRandomSeed());
        return (String[])result.toArray(new String[result.size()]);
    }

    public String randomSeedTipText() {
        return "The seed used for random sampling.";
    }

    public int getRandomSeed() {
        return this.m_RandomSeed;
    }

    public void setRandomSeed(int value) {
        this.m_RandomSeed = value;
    }

    public String percentageTipText() {
        return "The percentage of SMOTE instances to create.";
    }

    public void setPercentage(double value) {
        if(value >= 0.0D) {
            this.m_Percentage = value;
        } else {
            System.err.println("Percentage must be >= 0!");
        }

    }

    public double getPercentage() {
        return this.m_Percentage;
    }

    public String nearestNeighborsTipText() {
        return "The number of nearest neighbors to use.";
    }

    public void setNearestNeighbors(int value) {
        if(value >= 1) {
            this.m_NearestNeighbors = value;
        } else {
            System.err.println("At least 1 neighbor necessary!");
        }

    }

    public int getNearestNeighbors() {
        return this.m_NearestNeighbors;
    }

    public String classValueTipText() {
        return "The index of the class value to which SMOTE should be applied. Use a value of 0 to auto-detect the non-empty minority class.";
    }

    public void setClassValue(String value) {
        this.m_ClassValueIndex = value;
        if(this.m_ClassValueIndex.equals("0")) {
            this.m_DetectMinorityClass = true;
        } else {
            this.m_DetectMinorityClass = false;
        }

    }

    public String getClassValue() {
        return this.m_ClassValueIndex;
    }

    public boolean setInputFormat(Instances instanceInfo) throws Exception {
        super.setInputFormat(instanceInfo);
        super.setOutputFormat(instanceInfo);
        return true;
    }

    public boolean input(Instance instance) {
        if(this.getInputFormat() == null) {
            throw new IllegalStateException("No input instance format defined");
        } else {
            if(this.m_NewBatch) {
                this.resetQueue();
                this.m_NewBatch = false;
            }

            if(this.m_FirstBatchDone) {
                this.push(instance);
                return true;
            } else {
                this.bufferInput(instance);
                return false;
            }
        }
    }

    public boolean batchFinished() throws Exception {
        if(this.getInputFormat() == null) {
            throw new IllegalStateException("No input instance format defined");
        } else {
            if(!this.m_FirstBatchDone) {
                this.doSMOTE();
            }

            this.flushInput();
            this.m_NewBatch = true;
            this.m_FirstBatchDone = true;
            return this.numPendingOutput() != 0;
        }
    }

    public void doSMOTE() throws Exception {
        int minIndex = 0;
        int min = 2147483647;
        if(this.m_DetectMinorityClass) {
            int[] nearestNeighbors = this.getInputFormat().attributeStats(this.getInputFormat().classIndex()).nominalCounts;

            for(int sample = 0; sample < nearestNeighbors.length; ++sample) {
                if(nearestNeighbors[sample] != 0 && nearestNeighbors[sample] < min) {
                    min = nearestNeighbors[sample];
                    minIndex = sample;
                }
            }
        } else {
            String var29 = this.getClassValue();
            if(var29.equalsIgnoreCase("first")) {
                minIndex = 1;
            } else if(var29.equalsIgnoreCase("last")) {
                minIndex = this.getInputFormat().numClasses();
            } else {
                minIndex = Integer.parseInt(var29);
            }

            if(minIndex > this.getInputFormat().numClasses()) {
                throw new Exception("value index must be <= the number of classes");
            }

            --minIndex;
        }

        int var30;
        if(min <= this.getNearestNeighbors()) {
            var30 = min - 1;
        } else {
            var30 = this.getNearestNeighbors();
        }

        if(var30 < 1) {
            throw new Exception("Cannot use 0 neighbors!");
        } else {
            Instances var31 = this.getInputFormat().stringFreeStructure();
            Enumeration instanceEnum = this.getInputFormat().enumerateInstances();

            while(instanceEnum.hasMoreElements()) {
                Instance vdmMap = (Instance)instanceEnum.nextElement();
                this.push((Instance)vdmMap.copy());
                if((int)vdmMap.classValue() == minIndex) {
                    var31.add(vdmMap);
                }
            }

            HashMap var32 = new HashMap();
            Enumeration attrEnum = this.getInputFormat().enumerateAttributes();

            while(true) {
                Attribute rand;
                int extraIndexSet;
                double synthetic;
                double iVal;
                int var37;
                do {
                    do {
                        if(!attrEnum.hasMoreElements()) {
                            Random var33 = new Random((long)this.getRandomSeed());
                            LinkedList var34 = new LinkedList();
                            double var36 = this.getPercentage() / 100.0D - Math.floor(this.getPercentage() / 100.0D);
                            var37 = (int)(var36 * (double)var31.numInstances());
                            if(var37 >= 1) {
                                for(extraIndexSet = 0; extraIndexSet < var31.numInstances(); ++extraIndexSet) {
                                    var34.add(Integer.valueOf(extraIndexSet));
                                }
                            }

                            Collections.shuffle(var34, var33);
                            List var35 = var34.subList(0, var37);
                            HashSet var39 = new HashSet(var35);
                            Instance[] var40 = new Instance[var30];

                            for(int i = 0; i < var31.numInstances(); ++i) {
                                Instance var41 = var31.instance(i);
                                LinkedList var42 = new LinkedList();

                                for(int entryIterator = 0; entryIterator < var31.numInstances(); ++entryIterator) {
                                    Instance var44 = var31.instance(entryIterator);
                                    if(i != entryIterator) {
                                        double n = 0.0D;
                                        attrEnum = this.getInputFormat().enumerateAttributes();

                                        while(attrEnum.hasMoreElements()) {
                                            Attribute nn = (Attribute)attrEnum.nextElement();
                                            if(!nn.equals(this.getInputFormat().classAttribute())) {
                                                synthetic = var41.value(nn);
                                                iVal = var44.value(nn);
                                                if(nn.isNumeric()) {
                                                    n += Math.pow(synthetic - iVal, 2.0D);
                                                } else {
                                                    n += ((double[][])((double[][])var32.get(nn)))[(int)synthetic][(int)iVal];
                                                }
                                            }
                                        }

                                        n = Math.pow(n, 0.5D);
                                        var42.add(new Object[]{Double.valueOf(n), var44});
                                    }
                                }

                                Collections.sort(var42, new Comparator() {
                                    public int compare(Object o1, Object o2) {
                                        double distance1 = ((Double)((Object[])((Object[])o1))[0]).doubleValue();
                                        double distance2 = ((Double)((Object[])((Object[])o2))[0]).doubleValue();
                                        return Double.compare(distance1, distance2);
                                    }
                                });
                                Iterator var43 = var42.iterator();

                                for(int var45 = 0; var43.hasNext() && var45 < var30; ++var45) {
                                    var40[var45] = (Instance)((Object[])((Object[])var43.next()))[1];
                                }

                                label169:
                                for(int var46 = (int)Math.floor(this.getPercentage() / 100.0D); var46 > 0 || var39.remove(Integer.valueOf(i)); --var46) {
                                    double[] var47 = new double[var31.numAttributes()];
                                    int var48 = var33.nextInt(var30);
                                    attrEnum = this.getInputFormat().enumerateAttributes();

                                    while(true) {
                                        while(true) {
                                            Attribute var49;
                                            do {
                                                if(!attrEnum.hasMoreElements()) {
                                                    var47[var31.classIndex()] = (double)minIndex;
                                                    Instance var50 = new Instance(1.0D, var47);
                                                    this.push(var50);
                                                    continue label169;
                                                }

                                                var49 = (Attribute)attrEnum.nextElement();
                                            } while(var49.equals(this.getInputFormat().classAttribute()));

                                            double var51;
                                            double var53;
                                            if(var49.isNumeric()) {
                                                var51 = var40[var48].value(var49) - var41.value(var49);
                                                var53 = var33.nextDouble();
                                                var47[var49.index()] = var41.value(var49) + var53 * var51;
                                            } else if(var49.isDate()) {
                                                var51 = var40[var48].value(var49) - var41.value(var49);
                                                var53 = var33.nextDouble();
                                                var47[var49.index()] = (double)((long)(var41.value(var49) + var53 * var51));
                                            } else {
                                                int[] valueCounts = new int[var49.numValues()];
                                                int var52 = (int)var41.value(var49);
                                                ++valueCounts[var52];

                                                int maxIndex;
                                                int var54;
                                                for(maxIndex = 0; maxIndex < var30; ++maxIndex) {
                                                    var54 = (int)var40[maxIndex].value(var49);
                                                    ++valueCounts[var54];
                                                }

                                                maxIndex = 0;
                                                var54 = -2147483648;

                                                for(int index = 0; index < var49.numValues(); ++index) {
                                                    if(valueCounts[index] > var54) {
                                                        var54 = valueCounts[index];
                                                        maxIndex = index;
                                                    }
                                                }

                                                var47[var49.index()] = (double)maxIndex;
                                            }
                                        }
                                    }
                                }
                            }

                            return;
                        }

                        rand = (Attribute)attrEnum.nextElement();
                    } while(rand.equals(this.getInputFormat().classAttribute()));
                } while(!rand.isNominal() && !rand.isString());

                double[][] extraIndices = new double[rand.numValues()][rand.numValues()];
                var32.put(rand, extraIndices);
                int[] percentageRemainder = new int[rand.numValues()];
                int[][] featureValueCountsByClass = new int[this.getInputFormat().classAttribute().numValues()][rand.numValues()];

                int nnArray;
                for(instanceEnum = this.getInputFormat().enumerateInstances(); instanceEnum.hasMoreElements(); ++featureValueCountsByClass[nnArray][extraIndexSet]) {
                    Instance extraIndicesCount = (Instance)instanceEnum.nextElement();
                    extraIndexSet = (int)extraIndicesCount.value(rand);
                    nnArray = (int)extraIndicesCount.classValue();
                    ++percentageRemainder[extraIndexSet];
                }

                for(var37 = 0; var37 < rand.numValues(); ++var37) {
                    for(extraIndexSet = 0; extraIndexSet < rand.numValues(); ++extraIndexSet) {
                        double var38 = 0.0D;

                        for(int instanceI = 0; instanceI < this.getInputFormat().numClasses(); ++instanceI) {
                            double distanceToInstance = (double)featureValueCountsByClass[instanceI][var37];
                            double j = (double)featureValueCountsByClass[instanceI][extraIndexSet];
                            double values = (double)percentageRemainder[var37];
                            synthetic = (double)percentageRemainder[extraIndexSet];
                            iVal = distanceToInstance / values;
                            double max = j / synthetic;
                            var38 += Math.abs(iVal - max);
                        }

                        extraIndices[var37][extraIndexSet] = var38;
                    }
                }
            }
        }
    }

    /*public static void main(String[] args) {
        runFilter(new weka.filters.supervised.instance.SMOTE(), args);
    }*/
}