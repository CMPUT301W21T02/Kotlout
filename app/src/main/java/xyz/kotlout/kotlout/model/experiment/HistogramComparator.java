package xyz.kotlout.kotlout.model.experiment;

import java.util.Comparator;

/**
 * Allow two HistogramData objects to be compared so that an ArrayList of them can be sorted using Collections.sort()
 */
public class HistogramComparator implements Comparator<HistogramData> {

  /**
   * Compares two histogram objects based on their results
   *
   * @param h1 an object of type HistogramData
   * @param h2 an object of type HistogramData
   * @return 0 if they're equal
   */
  @Override
  public int compare(HistogramData h1, HistogramData h2) {
    try {
      double d = Double.parseDouble(h1.result) - Double.parseDouble(h2.result);
      return (int) d;
    } catch (Exception e) {
      return h1.getResult().compareTo(h2.getResult());
    }
  }
}
