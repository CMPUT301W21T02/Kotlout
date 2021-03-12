package xyz.kotlout.kotlout.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.model.adapter.TrialListAdapter;

public class ExperimentTrialListFragment extends Fragment {

  public static ExperimentTrialListFragment newInstance() {
    return new ExperimentTrialListFragment();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_experiment_list, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    ExpandableListView elv = view.findViewById(R.id.elv_main_experiment_list);
    elv.setAdapter(new TrialListAdapter(getContext()));

  }
}

