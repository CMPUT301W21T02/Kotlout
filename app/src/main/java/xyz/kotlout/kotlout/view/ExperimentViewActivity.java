package xyz.kotlout.kotlout.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.ArrayList;
import java.util.List;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.controller.ExperimentController;
import xyz.kotlout.kotlout.view.fragment.ExperimentInfoFragment;
import xyz.kotlout.kotlout.view.fragment.ExperimentMapFragment;
import xyz.kotlout.kotlout.view.fragment.ExperimentTrialListFragment;

public class ExperimentViewActivity extends AppCompatActivity {

  public static final int VIEW_EXPERIMENT_REQUEST = 0;
  public static final String EXPERIMENT_ID = "EXPERIMENT";

  ExperimentViewFragmentsAdapter adapter;
  ViewPager2 viewPager;
  TabLayout tabLayout;

  ExperimentController experimentController;
  String experimentId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_experiment_view);

    if (savedInstanceState == null) {
      Intent intent = getIntent();
      experimentId = intent.getStringExtra(EXPERIMENT_ID);

      adapter = new ExperimentViewFragmentsAdapter(getSupportFragmentManager(), getLifecycle());
      tabLayout = findViewById(R.id.tl_experiment_view);
      viewPager = findViewById(R.id.pager_experiment_view);

      experimentController = new ExperimentController(experimentId, () -> {

        ExperimentInfoFragment infoFragment = ExperimentInfoFragment
            .newInstance(experimentController.getExperimentContext(),
                experimentController.getType());
        adapter.addFragment(infoFragment);

        ExperimentMapFragment mapFragment = ExperimentMapFragment
            .newInstance(experimentController.getExperimentContext());
        adapter.addFragment(mapFragment);

        ExperimentTrialListFragment trialListFragment = ExperimentTrialListFragment
            .newInstance(experimentController.getExperimentContext(),
                experimentController.getType());
        adapter.addFragment(trialListFragment);

        viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager,
            (tab, position) -> {
              switch (position) {
                case 0:
                  tab.setText(R.string.view_info_text);
                  break;
                case 1:
                  tab.setText(R.string.view_map_text);
                  break;
                case 2:
                  tab.setText(R.string.view_trials_text);
                  break;
              }
            }
        ).attach();
      });
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.experiment_view_menu, menu);
    return true;
  }

  public void showOwner(View view) {
    // TODO: Add actual behavior
    AlertDialog.Builder builder = new AlertDialog.Builder(this);

    builder.setMessage(experimentController.getExperimentContext().getOwnerUuid())
        .setTitle("DING DONG");

    AlertDialog dialog = builder.create();

    dialog.show();
  }

  public static class ExperimentViewFragmentsAdapter extends FragmentStateAdapter {

    List<Fragment> fragmentList = new ArrayList<>();

    public ExperimentViewFragmentsAdapter(@NonNull FragmentManager fragmentManager,
        @NonNull Lifecycle lifecycle) {
      super(fragmentManager, lifecycle);
    }

    public void addFragment(Fragment fragment) {
      fragmentList.add(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
      return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
      return fragmentList.size();
    }
  }

}
