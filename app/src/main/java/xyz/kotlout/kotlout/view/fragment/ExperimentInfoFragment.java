package xyz.kotlout.kotlout.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.type.Color;
import java.util.ArrayList;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.model.ExperimentType;
import xyz.kotlout.kotlout.model.experiment.Experiment;
import xyz.kotlout.kotlout.model.experiment.HistogramData;
import xyz.kotlout.kotlout.view.InfoHeaderView;

/**
 * Provides information about the current trial
 */
public class ExperimentInfoFragment extends Fragment {

  private static final String ARG_EXPERIMENT = "EXPERIMENT";
  private static final String ARG_EXPERIMENT_TYPE = "EXPERIMENT_TYPE";

  // Declaration of objects
  Experiment experiment;
  ExperimentType type;
  BarChart histogram, trialInfo;
  ArrayList<BarEntry> barEntries, trialEntries;
  ArrayList<String> labels, trialLabels;

  ArrayList<HistogramData> histogramData = new ArrayList<>();
  ArrayList<HistogramData> trialData = new ArrayList<>();

  public static ExperimentInfoFragment newInstance(Experiment experiment, ExperimentType type) {
    ExperimentInfoFragment fragment = new ExperimentInfoFragment();
    Bundle args = new Bundle();
    args.putSerializable(ARG_EXPERIMENT, experiment);
    args.putSerializable(ARG_EXPERIMENT_TYPE, type);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      experiment = (Experiment) getArguments().getSerializable(ARG_EXPERIMENT);
      type = (ExperimentType) getArguments().getSerializable(ARG_EXPERIMENT_TYPE);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_experiment_info, container, false);

    InfoHeaderView infoHeader = view.findViewById(R.id.ihv_experiment_info);
    infoHeader.setExperiment(experiment, type);
    
    histogram = view.findViewById(R.id.histogram);
    barEntries = new ArrayList<>();
    labels = new ArrayList<>();

    fillData();
    ArrayList<HistogramData> merged = mergeData(histogramData);
    FormatHistogram(histogram);

    //------------------------------------------
    trialInfo = view.findViewById(R.id.trialInfo);
    trialEntries = new ArrayList<>();
    trialLabels = new ArrayList<>();

    fillEntries();

    FormatNonNegMeasurement(trialInfo);

//    switch (ARG_EXPERIMENT_TYPE){
//      case "EXPERIMENT":
//        FormatCountNonNegMeasurement(trialInfo);
//      case "BINOMIAL":
//    }


    return view;
  }

  private void fillTrialData(){

  }

  private void FormatBinomial (BarChart trialInfo){

  }

  private void FormatNonNegMeasurement (BarChart histogram){
    BarDataSet barDataSet = new BarDataSet(trialEntries, "Means");
    barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
    Description description = new Description();
    description.setText("Means");
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
   * @param histogram the histogram with the data loaded in
   */
  private void FormatHistogram(BarChart histogram) {
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
   * Merges the counts of all trials with the same results
   * @param histogramData The list of all trial results
   * @return A list of the trials that are merged on their counts if they have the same result
   */
  public ArrayList<HistogramData> mergeData(ArrayList<HistogramData> histogramData) {
    ArrayList<HistogramData> merged = new ArrayList<>();
    for (HistogramData h : histogramData) {
      int index = merged.indexOf(h);
      if(index != -1) {
        merged.set(index, merged.get(index).merge(h));
      } else {
        merged.add(h);
      }
    }

    for (int i = 0; i < merged.size(); i++) {
      String date = merged.get(i).getResult();
      int amount = merged.get(i).getCount();

      barEntries.add(new BarEntry(i, amount));
      labels.add(date);
    }

    return merged;
  }

  // Loads dummy data for now as we don't have trials set-up yet
  private void fillEntries() {
    trialData.add(new HistogramData("2021-01-05", 56));
    trialData.add(new HistogramData("2021-01-06", 63));
    trialData.add(new HistogramData("2021-01-07", 72));
    trialData.add(new HistogramData("2021-01-08", 97));
    trialData.add(new HistogramData("2021-01-09", 12));
    trialData.add(new HistogramData("2021-01-10", 55));

    for (int i = 0; i < trialData.size(); i++) {
      String date = trialData.get(i).getResult();
      int amount = trialData.get(i).getCount();

      trialEntries.add(new BarEntry(i, amount));
      trialLabels.add(date);
    }
  }

  // Loads dummy data for now as we don't have trials set-up yet
  private void fillData() {
    histogramData.add(new HistogramData("Success", 1));
    histogramData.add(new HistogramData("Success", 1));
    histogramData.add(new HistogramData("Failure", 1));
    histogramData.add(new HistogramData("Failure", 1));
    histogramData.add(new HistogramData("Failure", 1));
    histogramData.add(new HistogramData("Failure", 1));
    histogramData.add(new HistogramData("Failure", 1));
    histogramData.add(new HistogramData("Failure", 1));
    histogramData.add(new HistogramData("Failure", 1));
    histogramData.add(new HistogramData("Success", 1));
    histogramData.add(new HistogramData("Success", 1));
    histogramData.add(new HistogramData("Failure", 1));
    histogramData.add(new HistogramData("Failure", 1));
  }
}