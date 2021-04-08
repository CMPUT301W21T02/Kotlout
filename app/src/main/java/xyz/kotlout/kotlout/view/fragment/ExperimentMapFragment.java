package xyz.kotlout.kotlout.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.model.ExperimentType;
import xyz.kotlout.kotlout.model.experiment.Experiment;

public class ExperimentMapFragment extends Fragment {

  private static final String ARG_EXPERIMENT = "EXPERIMENT";

  Experiment experiment;
  ExperimentType type;

  public static ExperimentMapFragment newInstance(Experiment experiment) {
    ExperimentMapFragment fragment = new ExperimentMapFragment();
    Bundle args = new Bundle();
    args.putSerializable(ARG_EXPERIMENT, experiment);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      experiment = (Experiment) getArguments().getSerializable(ARG_EXPERIMENT);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_experiment_map, container, false);
    return view;
  }
}