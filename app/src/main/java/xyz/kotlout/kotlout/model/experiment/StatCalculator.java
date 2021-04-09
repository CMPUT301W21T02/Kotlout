package xyz.kotlout.kotlout.model.experiment;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Calculates the stats when an ArrayList of numbers is passed to its methods
 */
public class StatCalculator {

  /**
   * Empty constructor as the object requires no parameters
   */
  public StatCalculator() {
  }

  /**
   *
   * @param numberArray a list of numbers
   * @return the mean of the numbers in the array
   */
  public double getMean(ArrayList<Double> numberArray) {
    Collections.sort(numberArray);
    double sum = 0;
    for (double d : numberArray) {
      sum += d;
    }
    double mean = sum / numberArray.size();
    return mean;
  }

  /**
   *
   * @param numberArray a list of numbers
   * @return the median of the numbers in the array
   */
  public double getMedian(ArrayList<Double> numberArray) {
    Collections.sort(numberArray);
    return numberArray.get(numberArray.size() / 2);
  }

  /**
   *
   * @param numberArray a list of numbers
   * @return the standard deviation of the numbers in the array
   */
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

  /**
   *
   * @param numberArray a list of numbers
   * @return the first quartile of the numbers in the array
   */
  public double getQ1(ArrayList<Double> numberArray) {
    Collections.sort(numberArray);
    int medianIndex = numberArray.size() / 2;
    double q1 = numberArray.get(medianIndex / 2);
    return q1;
  }

  /**
   *
   * @param numberArray a list of numbers
   * @return the third quartile of the numbers in the array
   */
  public double getQ3(ArrayList<Double> numberArray) {
    Collections.sort(numberArray);
    int medianIndex = numberArray.size() / 2;
    double q3 = numberArray.get((medianIndex + numberArray.size()) / 2);
    return q3;
  }


}
