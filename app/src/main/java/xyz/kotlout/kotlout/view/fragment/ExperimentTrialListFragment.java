package xyz.kotlout.kotlout.view.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.model.ExperimentType;
import xyz.kotlout.kotlout.model.experiment.Experiment;

public class ExperimentTrialListFragment extends Fragment {

  private static final String ARG_EXPERIMENT = "EXPERIMENT";
  private static final String ARG_EXPERIMENT_TYPE = "EXPERIMENT_TYPE";

  Experiment experiment;
  ExperimentType type;

  public static ExperimentTrialListFragment newInstance(Experiment experiment, ExperimentType type) {
    ExperimentTrialListFragment fragment = new ExperimentTrialListFragment();
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
    View view = inflater.inflate(R.layout.fragment_experiment_trials, container, false);

    return view;
  }
}