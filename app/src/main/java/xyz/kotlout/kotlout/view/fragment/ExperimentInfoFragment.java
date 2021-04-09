package xyz.kotlout.kotlout.view.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import xyz.kotlout.kotlout.model.experiment.HistogramData;
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
  String experimentId;
  ExperimentType type;
  Experiment experiment;
  BarChart histogram, trialInfo;
  ArrayList<BarEntry> barEntries, trialEntries;
  ArrayList<String> labels, trialLabels;
  ExperimentController controller;
  List<? extends Trial> trialList;
  SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.CANADA);
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

    histogram = view.findViewById(R.id.histogram);
    barEntries = new ArrayList<>();
    labels = new ArrayList<>();

    //------------------------------------------
    trialInfo = view.findViewById(R.id.trialInfo);
    trialEntries = new ArrayList<>();
    trialLabels = new ArrayList<>();

    return view;
  }

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
   * Formats the axes and bars of the histogram
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

  @Override
  public void onExperimentLoaded() {
    experiment = controller.getExperimentContext();

    Set<String> ignoredUuids = sharedPrefs.getStringSet(experimentId, new TreeSet<>());

    trialList = controller.getListTrials().parallelStream().filter((trial) -> !ignoredUuids.contains(trial.getExperimenterId()))
        .collect(Collectors.toList());

    if (trialList.size() > 0) {
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

  public boolean hasDate(ArrayList<BinomialInfo> binomialInfo, String date) {
    for (BinomialInfo b : binomialInfo) {
      if (b.getDate().equals(date)) {
        return true;
      }
    }
    return false;
  }

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

    for (int i = 0; i < merged.size(); i++) {
      String date = merged.get(i).getResult();
      float amount = merged.get(i).getCount();

      barEntries.add(new BarEntry(i, amount));
      labels.add(date);
    }

    return merged;
  }

  public void ignoreListUpdated() {
    onExperimentLoaded();
  }
}