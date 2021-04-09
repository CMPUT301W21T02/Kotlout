package xyz.kotlout.kotlout.model.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.controller.ExperimentController;
import xyz.kotlout.kotlout.controller.UserController;
import xyz.kotlout.kotlout.controller.UserHelper;
import xyz.kotlout.kotlout.model.ExperimentType;
import xyz.kotlout.kotlout.model.experiment.trial.BinomialTrial;
import xyz.kotlout.kotlout.model.experiment.trial.CountTrial;
import xyz.kotlout.kotlout.model.experiment.trial.MeasurementTrial;
import xyz.kotlout.kotlout.model.experiment.trial.NonNegativeTrial;
import xyz.kotlout.kotlout.model.experiment.trial.Trial;
import xyz.kotlout.kotlout.model.user.User;

/**
 * Adapter class to show Trials in an Expandable List.
 */
public class TrialListAdapter extends BaseExpandableListAdapter {

  private final Context context;

  private List<? extends Trial> trialList;
  private Map<String, ? extends List<? extends Trial>> ByExperimenter;
  private List<String> Experimenters;

  private final ExperimentType type;
  private final ExperimentController controller;

  private final String myUuid;

  public TrialListAdapter(Context context, String experimentId, ExperimentType type) {
    this.context = context;
    this.type = type;

    controller = new ExperimentController(experimentId, this::onExperimentLoaded, this::onExperimentLoaded);
    myUuid = UserHelper.readUuid();
    Experimenters = new ArrayList<>();
    trialList = new ArrayList<>();
  }

  @Override
  public int getGroupCount() {
    return Experimenters.size();
  }

  @Override
  public int getChildrenCount(int groupPosition) {
    String experimenterName = Experimenters.get(groupPosition);
    if (Experimenters.size() == 0) {
      return 0;
    }

    return Objects.requireNonNull(ByExperimenter.get(experimenterName)).size();
  }

  @Override
  public Object getGroup(int groupPosition) {
    return Experimenters.get(groupPosition);
  }

  @Override
  public Object getChild(int groupPosition, int childPosition) {
    String experimenterName = Experimenters.get(groupPosition);
    return Objects.requireNonNull(ByExperimenter.get(experimenterName)).get(childPosition);
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

  @SuppressLint("SetTextI18n")
  @Override
  public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
      ViewGroup parent) {

    if (convertView == null) {
      LayoutInflater inflater = LayoutInflater.from(context);
      convertView = inflater.inflate(R.layout.trial_list_group, parent, false);
    }

    TextView tvGroup = convertView.findViewById(R.id.tv_trial_list_group);

    String groupUuid = (String) getGroup(groupPosition);

    UserController userController = new UserController(groupUuid);
    userController.setUpdateCallback(user -> {
      if (user.getUuid().equals(myUuid)) {
        tvGroup.setText("Me");
      } else {
        tvGroup.setText(user.getDisplayName());
      }
    });

    return convertView;
  }

  @SuppressLint("SetTextI18n")
  @Override
  public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
      View convertView, ViewGroup parent) {

    if (convertView == null) {
      LayoutInflater inflater = LayoutInflater.from(context);
      convertView = inflater.inflate(R.layout.trial_list_item, parent, false);
    }

    TextView trialResult = convertView.findViewById(R.id.tv_trial_list_result);
    ImageView trialGeolocation = convertView.findViewById(R.id.iv_trial_list_geolocation);
    TextView trialDate = convertView.findViewById(R.id.tv_trial_list_date);

    Trial trial = (Trial) getChild(groupPosition, childPosition);
    switch (type) {
      case BINOMIAL:
        BinomialTrial binomialTrial = (BinomialTrial) trial;
        trialResult.setText(binomialTrial.getResult() ? "Pass" : "Fail");
        break;
      case NON_NEGATIVE_INTEGER:
        NonNegativeTrial nonNegativeTrial = (NonNegativeTrial) trial;
        trialResult.setText(Long.toString(nonNegativeTrial.getResult()));
        break;
      case COUNT:
        CountTrial countTrial = (CountTrial) trial;
        trialResult.setText(Long.toString(countTrial.getResult()));
        break;
      case MEASUREMENT:
        MeasurementTrial measurementTrial = (MeasurementTrial) trial;
        trialResult.setText(Double.toString(measurementTrial.getResult()));
        break;
    }

    if (trial.getLocation() == null) {
      trialGeolocation.setImageResource(R.drawable.ic_baseline_location_off);
    } else {
      trialGeolocation.setImageResource(R.drawable.ic_baseline_location_on);
    }

    DateFormat local = new SimpleDateFormat("yyyy-MM-dd @ HH:mm", Locale.getDefault());
    trialDate.setText(local.format(trial.getTimestamp()));

    return convertView;
  }

  @Override
  public boolean isChildSelectable(int groupPosition, int childPosition) {
    return false;
  }

  /**
   * Callback function for when the experiment gets updated, causing the trial list to reload.
   */
  public void onExperimentLoaded() {
    trialList = controller.getListTrials();

    ByExperimenter = trialList.parallelStream().collect(Collectors.groupingBy(Trial::getExperimenterId));
    Experimenters = trialList.parallelStream().map(Trial::getExperimenterId).distinct().sorted().collect(Collectors.toList());

    if (Experimenters.contains(myUuid)) {
      Experimenters.remove(myUuid);
      Experimenters.add(0, myUuid);
    }

    this.notifyDataSetInvalidated();
    this.notifyDataSetChanged();
  }
}
