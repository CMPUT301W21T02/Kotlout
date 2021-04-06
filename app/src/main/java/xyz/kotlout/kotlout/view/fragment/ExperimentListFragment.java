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
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.controller.ExperimentController;
import xyz.kotlout.kotlout.controller.ExperimentGroup;
import xyz.kotlout.kotlout.controller.ExperimentListController;
import xyz.kotlout.kotlout.controller.UserController;
import xyz.kotlout.kotlout.controller.UserHelper;
import xyz.kotlout.kotlout.model.experiment.Experiment;
import xyz.kotlout.kotlout.view.ExperimentViewActivity;

/**
 * A fragment used to represent the user's experiments in a list.
 */
public class ExperimentListFragment extends Fragment {

  public static String ARG_TYPE = "TYPE";
  private ExperimentListAdapter experimentListAdapter;
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
    experimentListAdapter = new ExperimentListAdapter(UserHelper.readUuid());
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

  public enum ListType {
    MINE,
    ALL,
    SUBSCRIBED
  }

  /**
   * An adapter that keeps the experiment list up-to-date.
   */
  public class ExperimentListAdapter extends BaseExpandableListAdapter {

    private static final String TAG = "EXP_LIST_ADAPTER";
    private ExperimentListController experimentListController;
    private Query getMyExperimentsQuery;
    private Query getSubscribedExperimentsQuery;
    private Map<ExperimentGroup, List<ExperimentController>> experimentGroups;

    /**
     * Initializes the adapter for the given user's open and closed experiments.
     *
     * @param userUuid User identifier
     */
    public ExperimentListAdapter(String userUuid) {
      experimentGroups = initializeExperimentGroups();
      experimentListController = new ExperimentListController(userUuid);

      switch (type) {
        case MINE:
          getMyExperimentsQuery = experimentListController.getUserExperiments();
          getMyExperimentsQuery.addSnapshotListener(this::showMyExperiments);
          break;

        case ALL:
          // TODO
          break;

        case SUBSCRIBED:
          getSubscribedExperimentsQuery = experimentListController.getSubscribedExperiments();
          getSubscribedExperimentsQuery.addSnapshotListener(this::showSubscribedExperiments);
          break;
      }
    }

    /**
     * Initialize the list groups for different categories of experiments.
     *
     * @return An empty list for user experiments grouped by Open and Closed.
     */
    public Map<ExperimentGroup, List<ExperimentController>> initializeExperimentGroups() {

      Map<ExperimentGroup, List<ExperimentController>> experimentGroups = new HashMap<>();

      for (ExperimentGroup group : ExperimentGroup.values()) {
        experimentGroups.put(group, new ArrayList<>());
      }
      return experimentGroups;
    }

    /**
     * Updates the fragment with all experiments that the user has subscribed to.
     * @param queryDocumentSnapshots All experiments found in firestore.
     * @param e A firestore exception
     */
    private void showSubscribedExperiments(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
      clearExperimentGroups();

      UserController userController = new UserController(UserHelper.readUuid());
      userController.setUpdateCallback(user -> {
        List<String> subscriptions = user.getSubscriptions();

        // no subscriptions yet
        if (subscriptions.isEmpty()) {
          return;
        }

        // Filter experiments by user subscriptions
        for (QueryDocumentSnapshot experimentDoc : queryDocumentSnapshots) {
          Log.d(TAG, experimentDoc.getId() + " => " + experimentDoc.getData());

          int subscriptionIndex = subscriptions.indexOf(experimentDoc.getId());
          if (subscriptionIndex == -1) {
            continue;
          }
          addExperimentToGroup(experimentDoc);
          subscriptions.remove(subscriptionIndex);
          if (subscriptions.isEmpty()) {
            break;
          }
        }
        this.notifyDataSetChanged();
      });
    }

    /**
     * Adds an experiment to its corresponding list group.
     * @param experimentDoc A snapshot of an experiment in firestore.
     */
    private void addExperimentToGroup(QueryDocumentSnapshot experimentDoc) {
      ExperimentController controller = new ExperimentController(experimentDoc);
      Experiment experiment = controller.getExperimentContext();

      if (experiment.getIsOngoing()) {
        experimentGroups.get(ExperimentGroup.OPEN).add(controller);
      } else {
        experimentGroups.get(ExperimentGroup.CLOSED).add(controller);
      }
    }

    /**
     * Adds the user's experiments to the list fragment.
     * @param queryDocumentSnapshots A snapshot of experiments belonging to the user.
     * @param e A firestore exception.
     */
    private void showMyExperiments(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
      clearExperimentGroups();

      for (QueryDocumentSnapshot experimentDoc : queryDocumentSnapshots) {
        Log.d(TAG, experimentDoc.getId() + " => " + experimentDoc.getData());

        addExperimentToGroup(experimentDoc);
      }
      this.notifyDataSetChanged();
    }

    /**
     * Clears all experiment list groups.
     */
    private void clearExperimentGroups() {
      for (Entry<ExperimentGroup, List<ExperimentController>> pair : experimentGroups.entrySet()) {
        pair.getValue().clear();
      }
    }

    @Override
    public int getGroupCount() {
      return experimentGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
      ExperimentGroup experimentGroup = ExperimentGroup.getByOrder(groupPosition);
      return experimentGroups.get(experimentGroup).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
      ExperimentGroup experimentGroup = ExperimentGroup.getByOrder(groupPosition);
      return experimentGroups.get(experimentGroup);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
      ExperimentGroup experimentGroup = ExperimentGroup.getByOrder(groupPosition);
      return experimentGroups.get(experimentGroup).get(childPosition);
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

      ExperimentGroup experimentGroup = ExperimentGroup.getByOrder(groupPosition);
      tvGroup.setText(experimentGroup.toString());

      if (experimentGroup == ExperimentGroup.OPEN) {
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

      ExperimentGroup experimentGroup = ExperimentGroup.getByOrder(groupPosition);

      description.setText(experimentGroups.get(experimentGroup)
          .get(childPosition).getExperimentContext().getDescription());
      region.setText(experimentGroups.get(experimentGroup)
          .get(childPosition).getExperimentContext().getRegion());
      counter.setText(experimentGroups.get(experimentGroup).get(childPosition).generateCountText());
      type.setText("Binomial"); //TODO: Figure out how to get this working

      return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
      return true;
    }
  }
}

