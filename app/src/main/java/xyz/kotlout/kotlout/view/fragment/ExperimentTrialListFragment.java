package xyz.kotlout.kotlout.view.fragment;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import java.util.Set;
import java.util.TreeSet;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.controller.UserController;
import xyz.kotlout.kotlout.model.ExperimentType;
import xyz.kotlout.kotlout.model.adapter.TrialListAdapter;

public class ExperimentTrialListFragment extends Fragment implements OnCreateContextMenuListener {

  String experimentId;
  ExperimentType type;
  TrialListAdapter trialListAdapter;

  SharedPreferences sharedPrefs;
  Set<String> blockList;
  Runnable listener;

  public ExperimentTrialListFragment(String experimentId, ExperimentType type, SharedPreferences sharedPrefs) {
    this.experimentId = experimentId;
    this.type = type;

    this.sharedPrefs = sharedPrefs;
    blockList = new TreeSet<>();

    blockList.addAll(sharedPrefs.getStringSet(experimentId, new TreeSet<>()));
  }

  public static ExperimentTrialListFragment newInstance(String experimentId, ExperimentType type,
      SharedPreferences sharedPrefs) {
    return new ExperimentTrialListFragment(experimentId, type, sharedPrefs);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_experiment_trials, container, false);
    ExpandableListView elv = view.findViewById(R.id.elv_experiment_trials);
    trialListAdapter = new TrialListAdapter(getContext(), experimentId, type);
    elv.setAdapter(trialListAdapter);
    registerForContextMenu(elv);

    return view;
  }

  @Override
  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//    super.onCreateContextMenu(menu, v, menuInfo);
    ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;
    if (ExpandableListView.getPackedPositionType(info.packedPosition) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
      MenuInflater inflater = getActivity().getMenuInflater();
      inflater.inflate(R.menu.trial_list_menu, menu);
    }

  }

  @Override
  public boolean onContextItemSelected(@NonNull MenuItem item) {
    ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) item
        .getMenuInfo();

    int groupPosition = ExpandableListView.getPackedPositionGroup(info.packedPosition);
    String blockUuid = (String) trialListAdapter.getGroup(groupPosition);

    int itemId = item.getItemId();
    if (itemId == R.id.block_user) {
      Editor editor = sharedPrefs.edit();
      blockList.add(blockUuid);
      editor.putStringSet(experimentId, blockList);
      editor.apply();
      listener.run();
      trialListAdapter.notifyDataSetChanged();
      return true;
    } else if (itemId == R.id.show_profile) {
      UserController userController = new UserController(blockUuid);
      userController.setUpdateCallback(user -> {
        ProfileSheetFragment profile = new ProfileSheetFragment(user);
        profile.show(getChildFragmentManager(), "TrialList");
      });
      return true;
    }
    return super.onContextItemSelected(item);
  }


  public void addIgnoreListener(Runnable listener) {
    this.listener = listener;
  }
}