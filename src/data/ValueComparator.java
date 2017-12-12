package data;

import java.util.Comparator;
import java.util.Map;

class ValueComparator implements Comparator<Double> {
    Map<Double, String> map;

    public ValueComparator(Map<Double, String> map) {
        this.map = map;
    }

    public int compare(Double distance1, Double distance2) {
        return (int)(distance1 - distance2);
    }
}