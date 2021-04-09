package xyz.kotlout.kotlout.model.experiment;

import java.util.Comparator;

public class HistogramComparator implements Comparator<HistogramData> {

  @Override
  public int compare(HistogramData o1, HistogramData o2) {
    try {
      double d = Double.parseDouble(o1.result) - Double.parseDouble(o2.result);
      return (int) d;
    } catch (Exception e) {
      return o1.getResult().compareTo(o2.getResult());
    }
  }
}
