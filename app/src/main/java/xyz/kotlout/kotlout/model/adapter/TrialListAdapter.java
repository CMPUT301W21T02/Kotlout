package xyz.kotlout.kotlout.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.controller.ExperimentController;
import xyz.kotlout.kotlout.controller.UserHelper;
import xyz.kotlout.kotlout.model.ExperimentType;
import xyz.kotlout.kotlout.model.experiment.trial.BinomialTrial;
import xyz.kotlout.kotlout.model.experiment.trial.CountTrial;
import xyz.kotlout.kotlout.model.experiment.trial.MeasurementTrial;
import xyz.kotlout.kotlout.model.experiment.trial.NonNegativeTrial;
import xyz.kotlout.kotlout.model.experiment.trial.Trial;

public class TrialListAdapter extends BaseExpandableListAdapter {

  private List<? extends Trial> trialList;
  Map<String, ? extends List<? extends Trial>> ByExperimenter;

  private final Context context;

  ExperimentType type;
  String experimentId;

  ExperimentController controller;

  List<String> Experimenters;

  public TrialListAdapter(Context context, String experimentId, ExperimentType type) {
    this.context = context;
    this.experimentId = experimentId;
    this.type = type;

    controller = new ExperimentController(experimentId, this::onExperimentLoaded, this::onExperimentLoaded);

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

    return ByExperimenter.get(experimenterName).size();
  }

  @Override
  public Object getGroup(int groupPosition) {
    String uuid = Experimenters.get(groupPosition);
    return UserHelper.fetchUser(uuid).getDisplayName();
  }

  @Override
  public Object getChild(int groupPosition, int childPosition) {
    String experimenterName = Experimenters.get(groupPosition);
    return ByExperimenter.get(experimenterName).get(childPosition);
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
      LayoutInflater inflater = LayoutInflater.from(context);
      convertView = inflater.inflate(R.layout.trial_list_group, parent, false);
    }

    TextView tvGroup = convertView.findViewById(R.id.tv_trial_list_group);

    tvGroup.setText(Experimenters.get(groupPosition));
    return convertView;
  }

  @Override
  public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
      View convertView, ViewGroup parent) {

    if (convertView == null) {
      LayoutInflater inflater = LayoutInflater.from(context);
      convertView = inflater.inflate(R.layout.trial_list_item, parent, false);
    }

    TextView trialResult = convertView
        .findViewById(R.id.tv_trial_list_result);

    switch (type) {
      case BINOMIAL:
        BinomialTrial binomialTrial = (BinomialTrial) getChild(groupPosition, childPosition);
        trialResult.setText(binomialTrial.getResult() ? "Pass" : "Fail");
        break;
      case NON_NEGATIVE_INTEGER:
        NonNegativeTrial nonNegativeTrial = (NonNegativeTrial) getChild(groupPosition, childPosition);
        trialResult.setText(nonNegativeTrial.getResult());
        break;
      case COUNT:
        CountTrial countTrial = (CountTrial) getChild(groupPosition, childPosition);
        trialResult.setText(countTrial.getResult());
        break;
      case MEASUREMENT:
        MeasurementTrial measurementTrial = (MeasurementTrial) getChild(groupPosition, childPosition);
        trialResult.setText(Double.toString(measurementTrial.getResult()));
        break;
    }

    return convertView;
  }

  @Override
  public boolean isChildSelectable(int groupPosition, int childPosition) {
    return false;
  }

  public void onExperimentLoaded() {
    trialList = controller.getListTrials();

    String myUuid = UserHelper.readUuid();

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