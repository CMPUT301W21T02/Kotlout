package xyz.kotlout.kotlout.model.experiment;

import android.util.Pair;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * A bundled result created from computing statistics on an experiment.
 */
public class ExperimentStatistics {

  Map<Integer, Double> histogramValues;
  List<Pair<Date, Number>> timePlot;
  private double mean;
  private double standardDeviation;
  private double[] quartiles;
}
