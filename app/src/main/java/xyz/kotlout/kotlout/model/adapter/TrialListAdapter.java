package xyz.kotlout.kotlout.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.model.experiment.trial.BinomialTrial;

public class TrialListAdapter extends BaseExpandableListAdapter {

  private List<BinomialTrial> trialList;
  private final Context context;
  Map<String, List<BinomialTrial>> ByExperimenter;
  List<String> Experimenters;

  public TrialListAdapter(Context context) {
    this.context = context;
    Experimenters = new ArrayList<>();

    trialList = Arrays.asList(
        new BinomialTrial(true, "Me"),
        new BinomialTrial(true, "Me"),
        new BinomialTrial(true, "Anmol"),
        new BinomialTrial(true, "Amir"),
        new BinomialTrial(true, "Dillon"),
        new BinomialTrial(false, "Me"),
        new BinomialTrial(true, "Tharidu"),
        new BinomialTrial(false, "Me")
    );

    ByExperimenter = trialList.parallelStream().collect(Collectors.groupingBy(BinomialTrial::getExperimenter));
    Experimenters = trialList.parallelStream().filter(trial -> !trial.getExperimenter().equals("Me"))
        .map(BinomialTrial::getExperimenter).sorted().collect(
            Collectors.toList());
    Experimenters.remove("Me");
    Experimenters.add(0, "Me");
  }

  @Override
  public int getGroupCount() {
    return ByExperimenter.size();
  }

  @Override
  public int getChildrenCount(int groupPosition) {
    String experimenterName = Experimenters.get(groupPosition);
    return ByExperimenter.get(experimenterName).size();
  }

  @Override
  public Object getGroup(int groupPosition) {
    return Experimenters.get(groupPosition);
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

    BinomialTrial trial = (BinomialTrial) getChild(groupPosition, childPosition);

    TextView trialResult = convertView
        .findViewById(R.id.tv_trial_list_result);

    trialResult.setText(trial.getResult() ? "Pass" : "Fail");
    return convertView;
  }

  @Override
  public boolean isChildSelectable(int groupPosition, int childPosition) {
    return false;
  }
}