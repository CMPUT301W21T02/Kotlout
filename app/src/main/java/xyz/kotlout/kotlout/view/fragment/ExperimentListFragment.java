package xyz.kotlout.kotlout.view.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.controller.ExperimentController;
import xyz.kotlout.kotlout.controller.UserHelper;
import xyz.kotlout.kotlout.model.adapter.ExperimentListAdapter;
import xyz.kotlout.kotlout.model.experiment.Experiment;
import xyz.kotlout.kotlout.view.ExperimentViewActivity;

/**
 * A fragment used to represent the user's experiments in a list.
 */
public class ExperimentListFragment extends Fragment {

  public enum ListType {
    MINE,
    ALL,
    SUBSCRIBED
  }

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

    ExpandableListView elv = view.findViewById(R.id.elv_main_experiments);
    experimentListAdapter = new ExperimentListAdapter(getContext(), UserHelper.readUuid(), type);
    elv.setAdapter(experimentListAdapter);
    elv.setOnChildClickListener(this::onChildClick);
    elv.setOnItemLongClickListener(this::onItemLongClick);

    return view;
  }

  /**
   * Shows a context menu for experiment list items to modify their state.
   *
   * @return True if the event was handled. False otherwise.
   */
  private boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
    // https://stackoverflow.com/questions/2353074/android-long-click-on-the-child-views-of-a-expandablelistview/8320128#8320128
    // Accessed 2021-04-04, Author: Nicholas Harlen, License: CC BY-SA 4.0

    // Only consider child items (not parent groups)
    if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
      int groupPosition = ExpandableListView.getPackedPositionGroup(id);
      int childPosition = ExpandableListView.getPackedPositionChild(id);
      ExperimentController experimentController = (ExperimentController) experimentListAdapter
          .getChild(groupPosition, childPosition);
      Experiment experiment = experimentController.getExperimentContext();

      // Don't give options for experiments not owned by the user
      if (!experiment.getOwnerUuid().equals(UserHelper.readUuid())) {
        return false;
      }

      // Show options to user to modify the experiment state
      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
      builder.setTitle(R.string.experiment_long_click_menu_title)
          .setItems(getExperimentMenuOptions(experiment), (dialog, which) -> {
            switch (which) {
              // Publish or Unpublish
              case 0:
                if (experiment.isPublished()) {
                  experimentController.unpublish();
                } else {
                  experimentController.publish();
                }
                break;

              // End or Resume
              case 1:
                if (experiment.isOngoing()) {
                  experimentController.end();
                } else {
                  experimentController.resume();
                }
                break;
            }
          }).show();

      return true;
    }

    return false;
  }

  /**
   * Generates menu options for modifying the state of an experiment.
   *
   * @param experiment An instance of Experiment.
   * @return An array of menu option strings.
   */
  private CharSequence[] getExperimentMenuOptions(Experiment experiment) {
    CharSequence[] menuOptions = new CharSequence[]{"", ""};
    menuOptions[0] = experiment.isPublished() ? getString(R.string.unpublish_experiment_option)
        : getString(R.string.publish_experiment_option);
    menuOptions[1] =
        experiment.isOngoing() ? getString(R.string.end_experiment_option) : getString(R.string.resume_experiment_option);
    return menuOptions;
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

  public void addController() {

  }
}

