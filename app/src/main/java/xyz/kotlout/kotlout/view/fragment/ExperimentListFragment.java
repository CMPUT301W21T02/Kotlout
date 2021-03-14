package xyz.kotlout.kotlout.view.fragment;

import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.model.adapter.ExperimentListAdapter;
import xyz.kotlout.kotlout.controller.ExperimentController;
import xyz.kotlout.kotlout.controller.ExperimentListController;
import xyz.kotlout.kotlout.controller.MyExperimentGroup;
import xyz.kotlout.kotlout.model.experiment.Experiment;
import xyz.kotlout.kotlout.view.ExperimentViewActivity;

/**
 * A fragment used to represent the user's experiments in a list.
 */
public class ExperimentListFragment extends Fragment {

  private ExperimentListAdapter experimentListAdapter;

  public static String ARG_TYPE = "TYPE";

  public enum ListType {
    MINE,
    ALL,
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
    View view = inflater.inflate(R.layout.fragment_experiment_list, container, false);

    ExpandableListView elv = view.findViewById(R.id.elv_main_experiment_list);
    experimentListAdapter = new ExperimentListAdapter("0", getContext()); // TODO: get current user uuid
    elv.setAdapter(experimentListAdapter);
    elv.setOnChildClickListener(this::onChildClick);

    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

  }

  boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition,
      long id) {
    ExperimentController tapped = (ExperimentController) experimentListAdapter
        .getChild(groupPosition, childPosition);

    Intent intent = new Intent(getContext(), ExperimentViewActivity.class);
    intent.putExtra(ExperimentViewActivity.EXPERIMENT_ID, tapped.getExperimentId());
    startActivityForResult(intent, 0);

    return true;
  }
}

