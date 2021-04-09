package xyz.kotlout.kotlout.view.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.controller.ExperimentController;
import xyz.kotlout.kotlout.controller.ExperimentController.ExperimentLoadedObserver;
import xyz.kotlout.kotlout.model.ExperimentType;
import xyz.kotlout.kotlout.model.experiment.Experiment;
import xyz.kotlout.kotlout.model.experiment.HistogramComparator;
import xyz.kotlout.kotlout.model.experiment.HistogramData;
import xyz.kotlout.kotlout.model.experiment.StatCalculator;
import xyz.kotlout.kotlout.model.experiment.trial.BinomialInfo;
import xyz.kotlout.kotlout.model.experiment.trial.BinomialTrial;
import xyz.kotlout.kotlout.model.experiment.trial.CountTrial;
import xyz.kotlout.kotlout.model.experiment.trial.MeasurementTrial;
import xyz.kotlout.kotlout.model.experiment.trial.NonNegativeTrial;
import xyz.kotlout.kotlout.model.experiment.trial.Trial;
import xyz.kotlout.kotlout.view.InfoHeaderView;

/**
 * Provides information about the current trial
 */
public class ExperimentInfoFragment extends Fragment implements ExperimentLoadedObserver {

  private static final String ARG_EXPERIMENT = "EXPERIMENT";
  private static final String ARG_EXPERIMENT_TYPE = "EXPERIMENT_TYPE";

  // Declaration of objects
  TextView mean, median, stdDev, q1, q3;
  String experimentId;
  ExperimentType type;
  Experiment experiment;
  BarChart histogram, trialInfo;
  ArrayList<BarEntry> barEntries, trialEntries;
  ArrayList<String> labels, trialLabels;
  ExperimentController controller;
  List<? extends Trial> trialList;
  SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.CANADA);
  DecimalFormat df = new DecimalFormat("#.####");
  String dataType;

  ArrayList<HistogramData> histogramData = new ArrayList<>();
  ArrayList<HistogramData> trialData = new ArrayList<>();
  ArrayList<HistogramData> merged;

  SharedPreferences sharedPrefs;

  @NonNull
  public static ExperimentInfoFragment newInstance(String experimentId, ExperimentType type, SharedPreferences sharedPrefs) {
    ExperimentInfoFragment fragment = new ExperimentInfoFragment();
    Bundle args = new Bundle();
    args.putString(ARG_EXPERIMENT, experimentId);
    args.putSerializable(ARG_EXPERIMENT_TYPE, type);
    fragment.setArguments(args);
    fragment.sharedPrefs = sharedPrefs;
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      experimentId = getArguments().getString(ARG_EXPERIMENT);
      type = (ExperimentType) getArguments().getSerializable(ARG_EXPERIMENT_TYPE);
      controller = new ExperimentController(experimentId, this, null);
    }
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_experiment_info, container, false);

    InfoHeaderView infoHeader = view.findViewById(R.id.ihv_experiment_info);
    infoHeader.setExperiment(experimentId, type);

    mean = view.findViewById(R.id.mean_text_view);
    median = view.findViewById(R.id.median_text_view);
    stdDev = view.findViewById(R.id.std_dev_text_view);
    q1 = view.findViewById(R.id.q1_text_view);
    q3 = view.findViewById(R.id.q3_text_view);

    // Instantiating objects related to the histograms
    histogram = view.findViewById(R.id.histogram);
    barEntries = new ArrayList<>();
    labels = new ArrayList<>();

    // Instantiating object related to the trial plots
    trialInfo = view.findViewById(R.id.trialInfo);
    trialEntries = new ArrayList<>();
    trialLabels = new ArrayList<>();

    return view;
  }

  /**
   * calculates the mean, median, standard deviation, first quartile, and third quartile from a list of trials
   *
   * @param trialList a list of all trials in the current experiment
   */
  public void calculateStats(List<? extends Trial> trialList) {
    double calcMean, calcMedian, calcStdDev, calcQ1, calcQ3;
    StatCalculator calculator = new StatCalculator();
    ArrayList<Double> countList;
    switch (type) {
      case COUNT:
        countList = new ArrayList<>();
        for (Trial t : trialList) {
          countList.add((double) ((CountTrial) t).getResult());
        }
        setStats(countList, calculator, mean, median, stdDev, q1, q3);
        break;
      case BINOMIAL:
        countList = new ArrayList<>();
        for (Trial t : trialList) {
          boolean result = ((BinomialTrial) t).getResult();
          if (result) {
            countList.add((double) 1);
          } else {
            countList.add((double) 0);
          }
        }
        setStats(countList, calculator, mean, median, stdDev, q1, q3);
        break;
      case MEASUREMENT:
        countList = new ArrayList<>();
        for (Trial t : trialList) {
          countList.add(((MeasurementTrial) t).getResult());
        }
        setStats(countList, calculator, mean, median, stdDev, q1, q3);
        break;
      case NON_NEGATIVE_INTEGER:
        countList = new ArrayList<>();
        for (Trial t : trialList) {
          countList.add((double) ((NonNegativeTrial) t).getResult());
        }
        setStats(countList, calculator, mean, median, stdDev, q1, q3);
        break;
    }

  }

  /**
   * @param countList  a list of trials
   * @param calculator a StatCalculator object which will perform the necessary operations
   * @param mean       a textView which shows the mean on the GUI
   * @param median     a textView which shows the median on the GUI
   * @param stdDev     a textView which shows the standard deviation on the GUI
   * @param q1         a textView which shows the first quartile on the GUI
   * @param q3         a textView which shows the third quartile on the GUI
   */
  private void setStats(ArrayList<Double> countList, StatCalculator calculator, TextView mean, TextView median, TextView stdDev,
      TextView q1, TextView q3) {
    double calcMedian = calculator.getMedian(countList);
    median.setText(df.format(calcMedian));
    double calcMean = calculator.getMean(countList);
    mean.setText(df.format(calcMean));
    double calcStdDev = calculator.getStdDev(countList);
    stdDev.setText(df.format(calcStdDev));
    double calcQ1 = calculator.getQ1(countList);
    q1.setText(df.format(calcQ1));
    double calcQ3 = calculator.getQ3(countList);
    q3.setText(df.format(calcQ3));
  }

  /**
   * Displays the data of trials, arranged by date Graphs were made using the MPAndroidChart Library under the Apache 2.0
   * License https://github.com/PhilJay/MPAndroidChart
   *
   * @param histogram a BarChart object used to display trial data
   * @param dataType  what the bars in the data are representing
   */
  private void formatPlot(BarChart histogram, String dataType) {
    BarDataSet barDataSet = new BarDataSet(trialEntries, dataType);
    barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
    Description description = new Description();
    description.setText(dataType);
    histogram.setDescription(description);
    BarData barData = new BarData(barDataSet);
    histogram.setData(barData);

    XAxis xAxis = histogram.getXAxis();
    xAxis.setValueFormatter(new IndexAxisValueFormatter(trialLabels));
    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    xAxis.setDrawGridLines(false);
    xAxis.setDrawAxisLine(false);
    xAxis.setGranularity(1f);
    xAxis.setLabelCount(trialLabels.size());
    xAxis.setLabelRotationAngle(0);
    YAxis left = histogram.getAxisLeft();
    YAxis right = histogram.getAxisRight();
    left.setAxisMinimum(0);
    right.setAxisMinimum(0);
    histogram.animateY(1000);
    histogram.invalidate();
    histogram.setScaleY(1);
  }

  /**
   * Formats the axes and bars of the histogram Graphs were made using the MPAndroidChart Library under the Apache 2.0 License
   * https://github.com/PhilJay/MPAndroidChart
   *
   * @param histogram the histogram with the data loaded in
   */
  private void formatHistogram(BarChart histogram) {
    BarDataSet barDataSet = new BarDataSet(barEntries, "Answers");
    barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
    Description description = new Description();
    description.setText("Answers");
    histogram.setDescription(description);
    BarData barData = new BarData(barDataSet);
    histogram.setData(barData);

    XAxis xAxis = histogram.getXAxis();
    xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    xAxis.setDrawGridLines(false);
    xAxis.setDrawAxisLine(false);
    xAxis.setGranularity(1f);
    xAxis.setLabelCount(labels.size());
    xAxis.setLabelRotationAngle(0);
    YAxis left = histogram.getAxisLeft();
    YAxis right = histogram.getAxisRight();
    left.setAxisMinimum(0);
    right.setAxisMinimum(0);
    histogram.animateY(1000);
    histogram.invalidate();
    histogram.setScaleY(1);
  }

  /**
   * This method is used to grab the list of trials and display their data in the GUI
   */
  @Override
  public void onExperimentLoaded() {
    experiment = controller.getExperimentContext();

    Set<String> ignoredUuids = sharedPrefs.getStringSet(experimentId, new TreeSet<>());

    trialList = controller.getListTrials().parallelStream().filter((trial) -> !ignoredUuids.contains(trial.getExperimenterId()))
        .collect(Collectors.toList());

    if (trialList.size() > 0) {
      calculateStats(trialList);
      switch (type) {
        case NON_NEGATIVE_INTEGER:
          dataType = "Mean Per Day";
          for (Trial t : trialList) {
            histogramData.add(new HistogramData(String.valueOf(((NonNegativeTrial) t).getResult()), 1));
            trialData.add(new HistogramData(sdf.format(t.getTimestamp()), (float) ((NonNegativeTrial) t).getResult()));
          }
          ArrayList<String> processedDates = new ArrayList<>();
          ArrayList<HistogramData> meanTrialData = new ArrayList<>();
          for (HistogramData h : trialData) {
            if (!processedDates.contains(h.getResult())) {
              processedDates.add(h.getResult());
              float current = 0;
              float total = 0;
              for (HistogramData data : trialData) {
                Log.d("Date", data.getResult());
                if (data.getResult().equals(h.getResult())) {
                  current += data.getCount();
                  total++;
                }
              }
              meanTrialData.add(new HistogramData(h.getResult(), current / total));
            }
          }
          trialData = meanTrialData;

          trialData.sort((t1, t2) -> t1.getResult().compareTo(t2.getResult()));
          break;

        case BINOMIAL:
          dataType = "Success % Per Day";
          ArrayList<BinomialInfo> binomialInfo = new ArrayList<>();
          for (Trial t : trialList) {
            histogramData.add(new HistogramData(String.valueOf(((BinomialTrial) t).getResult()), 1));
            String date = sdf.format(t.getTimestamp());
            Log.d("TAG", String.valueOf(hasDate(binomialInfo, date)));
            Log.d("TAG", String.valueOf(binomialInfo.size()));
            if (hasDate(binomialInfo, date)) {
              boolean result = ((BinomialTrial) t).getResult();
              for (BinomialInfo b : binomialInfo) {
                if (b.getDate().equals(date)) {
                  if (result) {
                    b.incrementSuccess();
                    Log.d("TAG", "Success");
                  } else {
                    b.incrementFailure();
                    Log.d("TAG", "Failure");
                  }
                }
              }
            } else {
              binomialInfo.add(new BinomialInfo(date));
              boolean result = ((BinomialTrial) t).getResult();
              for (BinomialInfo b : binomialInfo) {
                if (b.getDate().equals(date)) {
                  if (result) {
                    b.incrementSuccess();
                    Log.d("TAG", "Success");
                  } else {
                    b.incrementFailure();
                    Log.d("TAG", "Failure");
                  }
                }
              }
            }
          }
          for (BinomialInfo b : binomialInfo) {
            trialData.add(new HistogramData(b.getDate(), b.successProportion()));
          }

          trialData.sort((t1, t2) -> t1.getResult().compareTo(t2.getResult()));
          break;

        case MEASUREMENT:
          dataType = "Mean Per Day";
          for (Trial t : trialList) {
            histogramData.add(new HistogramData(String.valueOf(((MeasurementTrial) t).getResult()), 1));
            trialData.add(new HistogramData(sdf.format(t.getTimestamp()), (float) ((MeasurementTrial) t).getResult()));
          }
          ArrayList<String> seenDates = new ArrayList<>();
          ArrayList<HistogramData> meanData = new ArrayList<>();
          for (HistogramData h : trialData) {
            if (!seenDates.contains(h.getResult())) {
              seenDates.add(h.getResult());
              float current = 0;
              float total = 0;
              for (HistogramData data : trialData) {
                Log.d("Date", data.getResult());
                if (data.getResult().equals(h.getResult())) {
                  current += data.getCount();
                  total++;
                }
              }
              meanData.add(new HistogramData(h.getResult(), current / total));
            }
          }
          trialData = meanData;

          trialData.sort((t1, t2) -> t1.getResult().compareTo(t2.getResult()));

          break;

        case COUNT:
          dataType = "Cumulative Count";
          for (Trial t : trialList) {
            histogramData.add(new HistogramData(String.valueOf(((CountTrial) t).getResult()), 1));
            trialData.add(new HistogramData(sdf.format(t.getTimestamp()), ((CountTrial) t).getResult()));
          }
          trialData.sort((t1, t2) -> t1.getResult().compareTo(t2.getResult()));
          trialData = mergeTrials(trialData);
          if (trialData.size() > 1) {
            for (int i = 1; i < trialData.size(); i++) {
              trialData.get(i).setCount(trialData.get(i).getCount() + trialData.get(i - 1).getCount());
            }
          }
          break;
      }

      merged = mergeData(histogramData);
      Log.d("TAG", String.valueOf(merged));
      formatHistogram(histogram);

      //--------------------------------------------

      for (int i = 0; i < trialData.size(); i++) {
        String date = trialData.get(i).getResult();
        float amount = trialData.get(i).getCount();

        trialEntries.add(new BarEntry(i, amount));
        trialLabels.add(date);
      }
      formatPlot(trialInfo, dataType);
    }
  }

  /**
   * @param binomialInfo A list of objects representing data points for binomial trials
   * @param date         A date represented as a formatted string
   * @return Whether there is a trial with that date in the list of BinomialInfo objects
   */
  public boolean hasDate(ArrayList<BinomialInfo> binomialInfo, String date) {
    for (BinomialInfo b : binomialInfo) {
      if (b.getDate().equals(date)) {
        return true;
      }
    }
    return false;
  }

  /**
   * @param trialData the trials which are to have their information displayed
   * @return a list of trials that have their results merged if they have the same date
   */
  public ArrayList<HistogramData> mergeTrials(ArrayList<HistogramData> trialData) {
    ArrayList<HistogramData> merged = new ArrayList<>();
    for (HistogramData h : trialData) {
      int index = merged.indexOf(h);
      if (index != -1) {
        merged.set(index, merged.get(index).merge(h));
      } else {
        merged.add(h);
      }
    }
    return merged;
  }

  /**
   * Merges the counts of all trials with the same results
   *
   * @param histogramData The list of all trial results
   * @return A list of the trials that are merged on their counts if they have the same result
   */
  public ArrayList<HistogramData> mergeData(ArrayList<HistogramData> histogramData) {
    ArrayList<HistogramData> merged = new ArrayList<>();
    for (HistogramData h : histogramData) {
      int index = merged.indexOf(h);
      if (index != -1) {
        merged.set(index, merged.get(index).merge(h));
      } else {
        merged.add(h);
      }
    }

    merged.sort(new HistogramComparator());
    for (int i = 0; i < merged.size(); i++) {
      String date = merged.get(i).getResult();
      float amount = merged.get(i).getCount();
      Log.d("I NEED THIS TAG", String.valueOf(merged.get(i).getCount()));

      barEntries.add(new BarEntry(i, amount));
      labels.add(date);
    }

    return merged;
  }

  public void ignoreListUpdated() {
    onExperimentLoaded();
  }
}