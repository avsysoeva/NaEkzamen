package mnist;

import data.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        String mnistPath = "C:\\Users\\se\\Desktop\\NaEkzamen\\data\\";
//        String trainImgName = "train-images.idx3-ubyte";
//        String trainLblName = "train-labels.idx1-ubyte";
//        String testImgName = "t10k-images.idx3-ubyte";
//        String testLblName = "t10k-labels.idx1-ubyte";

        String trainImgName = args[0];
        String trainLblName = args[1];
        String testImgName = args[2];
        String testLblName = args[3];

        String fileTrainingImg = trainImgName; //mnistPath + trainImgName;
        String fileTestDataImg = testImgName; // mnistPath + testImgName;
        String fileTrainingLbl = trainLblName; //mnistPath + trainLblName;
        String fileTestDataLbl = testLblName; //mnistPath + testLblName;

        try
        {
            BufferedInputStream fileTrainingImages = new BufferedInputStream(new FileInputStream(new File(fileTrainingImg)));
            BufferedInputStream fileTrainingLabels = new BufferedInputStream(new FileInputStream(new File(fileTrainingLbl)));

            BufferedInputStream fileTestImages = new BufferedInputStream(new FileInputStream(new File(fileTestDataImg)));
            BufferedInputStream fileTestLabels = new BufferedInputStream(new FileInputStream(new File(fileTestDataLbl)));

            MnistDb trainDb = new MnistDb();
            MnistDb testDb = new MnistDb();

            Classifier classifier = new Classifier(trainDb.getImages(fileTrainingImages, 60000),
                    trainDb.getLabels(fileTrainingLabels, 60000));

            int k = Integer.valueOf(args[4]); //12;
            int n = Integer.valueOf(args[5]); //6;

            if (k==0) {throw new ArrayIndexOutOfBoundsException ("Neighbour qty must be >0 !");}
            if (n==0) {throw new ArrayIndexOutOfBoundsException ("Thread qty must be >0 !");}

//            System.out.println(Arrays.toString(trainDb.getImagesList().get(0)));
//            System.out.println(trainDb.getImagesList().size());
//            System.out.println(trainDb.getLabelsList().get(0));
//            System.out.println(trainDb.getLabelsList().size());

            testDb.getImages(fileTestImages, 10000);
            testDb.getLabels(fileTestLabels, 10000);

            classifier.getAccuracy(testDb, k, n, classifier);

        }
        catch (IOException io)
        {
            System.out.println("Cannot load file...");
        }
    }
}


