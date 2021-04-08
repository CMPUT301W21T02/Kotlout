package xyz.kotlout.kotlout.model.experiment;

import java.util.ArrayList;
import java.util.Collections;

public class StatCalculator {

  public StatCalculator() {
  }

  public double getMean(ArrayList<Double> numberArray) {
    Collections.sort(numberArray);
    double sum = 0;
    for (double d : numberArray) {
      sum += d;
    }
    double mean = sum / numberArray.size();
    return mean;
  }

  public double getMedian(ArrayList<Double> numberArray) {
    Collections.sort(numberArray);
    return numberArray.get(numberArray.size() / 2);
  }

  public double getStdDev(ArrayList<Double> numberArray) {
    Collections.sort(numberArray);
    double sum = 0;
    double stdDev = 0;
    for (double d : numberArray) {
      sum += d;
    }

    double mean = sum / numberArray.size();

    for (double d : numberArray) {
      stdDev += Math.pow(d - mean, 2);
    }

    return Math.sqrt(stdDev / numberArray.size());
  }

  public double getQ1(ArrayList<Double> numberArray) {
    Collections.sort(numberArray);
    int medianIndex = numberArray.size() / 2;
    double q1 = numberArray.get(medianIndex / 2);
    return q1;
  }

  public double getQ3(ArrayList<Double> numberArray) {
    Collections.sort(numberArray);
    int medianIndex = numberArray.size() / 2;
    double q3 = numberArray.get((medianIndex + numberArray.size()) / 2);
    return q3;
  }


}
