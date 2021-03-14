package xyz.kotlout.kotlout.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Map;
import java.util.Map.Entry;
import xyz.kotlout.kotlout.R;
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

  /**
   * An adapter that keeps the experiment list up-to-date.
   */
  public class ExperimentListAdapter extends BaseExpandableListAdapter {

    private static final String TAG = "EXP_LIST_ADAPTER";
    private ExperimentListController experimentListController;
    private Query myExperimentsRef;
    private Map<MyExperimentGroup, List<ExperimentController>> myExperiments;

    public ExperimentListAdapter(String userUuid) {
      experimentListController = new ExperimentListController(userUuid);
      myExperiments = experimentListController.initializeMyExperiments();

      myExperimentsRef = experimentListController.getGetUserExperiments();

      myExperimentsRef.addSnapshotListener((queryDocumentSnapshots, e) -> {

        for (Entry<MyExperimentGroup, List<ExperimentController>> pair : myExperiments.entrySet()) {
          pair.getValue().clear();
        }

        for (QueryDocumentSnapshot experimentDoc : queryDocumentSnapshots) {
          Log.d(TAG, experimentDoc.getId() + " => " + experimentDoc.getData());

          ExperimentController controller = new ExperimentController(experimentDoc);
          Experiment experiment = controller.getExperimentContext();

          if (experiment.getIsOngoing()) {
            myExperiments.get(MyExperimentGroup.OPEN).add(controller);
          } else {
            myExperiments.get(MyExperimentGroup.CLOSED).add(controller);
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
      MyExperimentGroup experimentGroup = MyExperimentGroup.getByOrder(groupPosition);
      return myExperiments.get(experimentGroup).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
      MyExperimentGroup experimentGroup = MyExperimentGroup.getByOrder(groupPosition);
      return myExperiments.get(experimentGroup);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
      MyExperimentGroup experimentGroup = MyExperimentGroup.getByOrder(groupPosition);
      return myExperiments.get(experimentGroup).get(childPosition);
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

      MyExperimentGroup experimentGroup = MyExperimentGroup.getByOrder(groupPosition);
      tvGroup.setText(experimentGroup.toString());

      if (experimentGroup == MyExperimentGroup.OPEN) {
        ExpandableListView elv = (ExpandableListView) parent;
        elv.expandGroup(groupPosition);
      }

      return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
        View convertView, ViewGroup parent) {

      if (convertView == null) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        convertView = inflater.inflate(R.layout.experiment_list_item, parent, false);
      }

      TextView description = convertView
          .findViewById(R.id.tv_experiment_list_description);
      TextView region = convertView.findViewById(R.id.tv_experiment_list_region);
      TextView counter = convertView.findViewById(R.id.tv_experiment_list_counter);
      TextView type = convertView.findViewById(R.id.tv_experiment_list_type);

      MyExperimentGroup experimentGroup = MyExperimentGroup.getByOrder(groupPosition);

      description.setText(myExperiments.get(experimentGroup)
          .get(childPosition).getExperimentContext().getDescription());
      region.setText(myExperiments.get(experimentGroup)
          .get(childPosition).getExperimentContext().getRegion());
      counter.setText(myExperiments.get(experimentGroup).get(childPosition).generateCountText());
      type.setText("Binomial"); //TODO: Figure out how to get this working

      return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
      return true;
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
    View view = inflater.inflate(R.layout.fragment_experiment_list, container, false);

    ExpandableListView elv = view.findViewById(R.id.elv_main_experiment_list);
    experimentListAdapter = new ExperimentListAdapter("0"); // TODO: get current user uuid
    elv.setAdapter(experimentListAdapter);
    elv.setOnChildClickListener(this::onChildClick);

    // Inflate the layout for this fragment
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

