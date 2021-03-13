package xyz.kotlout.kotlout.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.List;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.controller.ExperimentListController;
import xyz.kotlout.kotlout.model.experiment.BinomialExperiment;
import xyz.kotlout.kotlout.model.experiment.Experiment;

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

  /**
   * An adapter that keeps the experiment list up-to-date.
   */
  public class ExperimentListAdapter extends BaseExpandableListAdapter {

    private static final String TAG = "EXP_LIST_ADAPTER";
    private ExperimentListController experimentListController;
    private Query myExperimentsRef;
    private List<Pair<String, List<Experiment>>> myExperiments;

    public ExperimentListAdapter(String userUuid) {
      experimentListController = new ExperimentListController(userUuid);
      myExperiments = experimentListController.initializeMyExperiments();

      myExperimentsRef = experimentListController.getGetUserExperiments();
      myExperimentsRef.addSnapshotListener((queryDocumentSnapshots, e) -> {
        myExperiments.get(0).second.clear();
        myExperiments.get(1).second.clear();

        for (QueryDocumentSnapshot experimentDoc : queryDocumentSnapshots) {
          Log.d(TAG, experimentDoc.getId() + " => " + experimentDoc.getData());
          Experiment experiment = experimentDoc
              .toObject(BinomialExperiment.class); // TODO: handle different types of experiment

          if (experiment.getIsOngoing()) {
            myExperiments.get(0).second.add(experiment);
          } else {
            myExperiments.get(1).second.add(experiment);
          }
        }
        this.notifyDataSetChanged();
      });
    }

    @Override
    public int getGroupCount() {
      return myExperiments.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
      return myExperiments.get(groupPosition).second.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
      return myExperiments.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
      return myExperiments.get(groupPosition).second.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
      return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
      return childPosition;
    }

    @Override
    public boolean hasStableIds() {
      return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
        ViewGroup parent) {

      if (convertView == null) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        convertView = inflater.inflate(R.layout.experiment_list_group, parent, false);
      }

      TextView tvGroup = convertView.findViewById(R.id.tv_experiment_list_group);

      tvGroup.setText(myExperiments.get(groupPosition).first);
      return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
        View convertView, ViewGroup parent) {

      if (convertView == null) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        convertView = inflater.inflate(R.layout.experiment_list_item, parent, false);
      }

      TextView experimentDescription = convertView
          .findViewById(R.id.tv_experiment_list_description);

      experimentDescription
          .setText(myExperiments.get(groupPosition).second.get(childPosition).getDescription());

      return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
      return false;
    }
  }

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

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    ExpandableListView elv = view.findViewById(R.id.elv_main_experiment_list);
    experimentListAdapter = new ExperimentListAdapter("0"); // TODO: get current user uuid
    elv.setAdapter(experimentListAdapter);
  }
}

