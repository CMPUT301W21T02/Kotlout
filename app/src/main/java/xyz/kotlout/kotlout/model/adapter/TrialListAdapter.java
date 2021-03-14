package xyz.kotlout.kotlout.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import xyz.kotlout.kotlout.R;

public class TrialListAdapter extends BaseExpandableListAdapter {

  private final String[] groups = { "Mine", "Amir", "Tharidu"};
  private final String[][] children = {{"peepee"}, {"Poopoo"}};
  private final Context context;

  public TrialListAdapter(Context context) {
    this.context = context;
  }
  @Override
  public int getGroupCount() {
    return groups.length;
  }

  @Override
  public int getChildrenCount(int groupPosition) {
    return children[groupPosition].length;
  }

  @Override
  public Object getGroup(int groupPosition) {
    return groups[groupPosition];
  }

  @Override
  public Object getChild(int groupPosition, int childPosition) {
    return children[groupPosition][childPosition];
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
      convertView = inflater.inflate(R.layout.experiment_list_group, parent, false);
    }

    TextView tvGroup = convertView.findViewById(R.id.tv_experiment_list_group);

    tvGroup.setText(groups[groupPosition]);
    return convertView;
  }

  @Override
  public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
      View convertView, ViewGroup parent) {

    if (convertView == null) {
      LayoutInflater inflater = LayoutInflater.from(context);
      convertView = inflater.inflate(R.layout.experiment_list_item, parent, false);
    }

    TextView experimentDescription = convertView
        .findViewById(R.id.tv_experiment_list_description);

    experimentDescription.setText(children[groupPosition][childPosition]);
    return convertView;
  }

  @Override
  public boolean isChildSelectable(int groupPosition, int childPosition) {
    return false;
  }
}