package xyz.kotlout.kotlout.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import xyz.kotlout.kotlout.R;

public class ExperimentListFragment extends Fragment {

  public static String ARG_TYPE = "TYPE";

  public enum ListType {
    MINE,
    ALL,
    SUBSCRIBED
  }

  private ListType type;

  public class ExperimentListAdapter extends BaseExpandableListAdapter {

    private String[] groups = {"Open experiments", "Closed Experiments"};
    private String[][] children = {{"peepee"}, {"Poopoo"}};

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
        LayoutInflater inflater = getActivity().getLayoutInflater();
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
        LayoutInflater inflater = getActivity().getLayoutInflater();
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
    elv.setAdapter(new ExperimentListAdapter());

  }
}

