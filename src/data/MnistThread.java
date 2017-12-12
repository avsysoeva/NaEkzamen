package data;

import java.util.concurrent.CountDownLatch;

import static jdk.nashorn.internal.objects.Global.print;

public class MnistThread extends Thread {
    MnistDb mnistDb;
    int k;
    int n;
    int m;
    int threadQty;
    int size;

    public void setSize(int size) {
        this.size = size;
    }

    public void setM(int m) {
        this.m = m;
    }

    int counter;
    static int total = 1;
    Classifier classifier;

    public Classifier getClassifier() {
        return classifier;
    }

    public void setClassifier(Classifier classifier) {
        this.classifier = classifier;
    }

    public static int getTotal() {
        return total;
    }

    CountDownLatch doneSignal;

    MnistThread(CountDownLatch doneSignal, int threadQty) {
        this.doneSignal = doneSignal;
        this.threadQty = threadQty;
    }

    String labelCompareTo = null;
    String labelCompareWith = null;

    public void setMnistDb(MnistDb mnistDb) {
        this.mnistDb = mnistDb;
    }

    public void setK(int k) {
        this.k = k;
    }

    public void setN(int n) {
        this.n = n;
    }

    public void run() {
        doWork(threadQty);
        doneSignal.countDown();
    }

    void doWork(int threadQty) {

        for (int i = m; i < size + m; i++) {
            labelCompareTo = classifier.getLabelID(mnistDb.getImagesList().get(i), k);
            labelCompareWith = mnistDb.getLabelsList().get(i);
            if (labelCompareTo.equals(labelCompareWith)) {
                counter = classifier.getCounter() + 1;
                classifier.setCounter(counter);
            }

            total++;

            double output = ((double) total / (double) (mnistDb.getImagesList().size())) * 100;
            String outputString = String.format("%.2f", output);
            System.out.print("\rDone: " + outputString + "%");
            }
            classifier.setWrong(mnistDb.getImagesList().size() - counter);
        }
    }
