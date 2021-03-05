package xyz.kotlout.kotlout.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import xyz.kotlout.kotlout.R;

public class ExperimentListFragment extends Fragment {

  public static String ARG_TYPE = "TYPE";

  public enum ListType {
    OWNED,
    GLOBAL,
    SUBSCRIBED
  }

  private ListType type;

  public static ExperimentListFragment newInstance(@NonNull ListType type) {
    ExperimentListFragment fragment = new ExperimentListFragment();

    Bundle args = new Bundle();
    args.putSerializable(ARG_TYPE, type);

    fragment.setArguments(args);

    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      type = (ListType) getArguments().getSerializable(ARG_TYPE);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_experiment_list, container, false);
  }
}

