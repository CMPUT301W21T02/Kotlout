package xyz.kotlout.kotlout.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import androidx.fragment.app.Fragment;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.model.ExperimentType;
import xyz.kotlout.kotlout.model.adapter.TrialListAdapter;

public class ExperimentTrialListFragment extends Fragment {

  private static final String ARG_EXPERIMENT_ID = "EXPERIMENT_ID";
  private static final String ARG_EXPERIMENT_TYPE = "EXPERIMENT_TYPE";

  String experimentId;
  ExperimentType type;

  public static ExperimentTrialListFragment newInstance(String experimentId, ExperimentType type) {
    ExperimentTrialListFragment fragment = new ExperimentTrialListFragment();
    Bundle args = new Bundle();
    args.putString(ARG_EXPERIMENT_ID, experimentId);
    args.putSerializable(ARG_EXPERIMENT_TYPE, type);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      experimentId = getArguments().getString(ARG_EXPERIMENT_ID);
      type = (ExperimentType) getArguments().getSerializable(ARG_EXPERIMENT_TYPE);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_experiment_trials, container, false);
    ExpandableListView elv = view.findViewById(R.id.elv_experiment_trials);
    TrialListAdapter trialListAdapter = new TrialListAdapter(getContext(), experimentId, type);

    elv.setAdapter(trialListAdapter);

    return view;
  }
}
