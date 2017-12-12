package data;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

public class Classifier {

    private List<int[]> imagesList;
    private List<String> labelsList;

    private int counter;
    private int wrong;

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public void setWrong(int wrong) {
        this.wrong = wrong;
    }

    public Classifier(List<int[]> imagesList, List<String> labelsList)
    {
        this.imagesList = imagesList;
        this.labelsList = labelsList;
    }

    public int getWrong() {
        return wrong;
    }
    public int getCounter() {
        return counter;
    }

    public double getDistance(int[] imagesCompareWith, int[] imagesCompareTo)
    {
        double distance = 0;
        for (int i = 0; i < imagesCompareWith.length; i++)
        {
            distance += Math.pow(imagesCompareWith[i] - imagesCompareTo[i], 2);
        }

        return Math.sqrt(distance);
    }

    public String getLabelID (int[] images, int k)
    {
        Map<Double, String> map = new HashMap<Double, String>();
        ValueComparator mvc = new ValueComparator(map);
        SortedMap<Double, String> distanceMap = new TreeMap<Double, String>(mvc);

        double distance = 0;
        for (int i = 0; i < imagesList.size(); i++)
        {
            distance = getDistance(images, imagesList.get(i));
            map.put(distance, labelsList.get(i));
        }
        distanceMap.putAll(map);
        List<String> distanceMapValues = new ArrayList<>(distanceMap.values());
        List<String> knnList = new ArrayList<String>();

        for (int i = 0; i < distanceMapValues.size(); i++)
        {
            knnList.add(distanceMapValues.get(i));
            if (knnList.size() == k) break;
        }

        //System.out.println(knnList.toString());

        return findLabel(knnList);
    }

    public void getAccuracy(MnistDb mnistDb, int k, int n, Classifier classifier)
    {
        int m = 0;
        int size = mnistDb.getImagesList().size();
        int [] array = new int [n];

        if (n>1) {
        for (int threadQty=0;threadQty<n-1;threadQty++)
        {
            array[threadQty] = size/n;
            m+=array[threadQty];
        }
        array[n-1] = size - m;}
        else {array[0] = size;}

        CountDownLatch doneSignal = new CountDownLatch(n);
        Executor e = new Executor() {
            @Override
            public void execute(Runnable mnistThread) {
                new Thread(mnistThread).start();
            }
        };

        m = 0;
        for (int threadQty=0;threadQty<n;threadQty++)
        {
            MnistThread mnistThread = new MnistThread(doneSignal, threadQty);
            mnistThread.setMnistDb(mnistDb);
            mnistThread.setK(k);
            mnistThread.setN(n);
            mnistThread.setClassifier(classifier);
            mnistThread.setM(m);
            mnistThread.setSize(array[threadQty]);
            e.execute(mnistThread);
            m+=array[threadQty];
            classifier = mnistThread.getClassifier();
        }

        try {
            doneSignal.await();
            //Thread.currentThread().sleep(500);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        double total = (double)MnistThread.getTotal()/(double)size*100;
        String totalString = String.format("%.2f", total);
        System.out.print("\rDone: " + totalString + "%");
        System.out.println();
        System.out.println("Guessed wrong: " + classifier.getWrong());

        double output = ((double)classifier.getCounter() / (double)mnistDb.getImagesList().size()*100);
        String outputString = String.format("%.2f", output);

        System.out.println("Calculation accuracy: " + outputString + "%");
    }

    public String findLabel(List<String> knnList)
    {
        Map<String, Integer> map = new HashMap<>();
        int max=0;
        String output=null;

            for (int i = 0; i < knnList.size(); i++) {
                if (map.containsKey(knnList.get(i))) {
                    int frequency = map.get(knnList.get(i));
                    if (frequency > max)
                    {
                        max = frequency;
                        output = knnList.get(i);
                    }
                    map.put(knnList.get(i), frequency + 1);
                } else {
                        map.put(knnList.get(i), 1);
                }
            }

        return output;
    }
}
