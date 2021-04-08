package xyz.kotlout.kotlout.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.ArrayList;
import java.util.List;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.controller.ExperimentController;
import xyz.kotlout.kotlout.controller.UserController;
import xyz.kotlout.kotlout.controller.UserHelper;
import xyz.kotlout.kotlout.model.experiment.trial.Trial;
import xyz.kotlout.kotlout.view.fragment.ExperimentInfoFragment;
import xyz.kotlout.kotlout.view.fragment.ExperimentMapFragment;
import xyz.kotlout.kotlout.view.fragment.ExperimentTrialListFragment;

public class ExperimentViewActivity extends AppCompatActivity {

  public static final int VIEW_EXPERIMENT_REQUEST = 0;
  public static final String EXPERIMENT_ID = "EXPERIMENT";
  public static final String TAG = "EXPERIMENT_VIEW";

  private ExperimentViewFragmentsAdapter adapter;
  private ViewPager2 viewPager;
  private TabLayout tabLayout;
  private FloatingActionButton trialFab;

  private ExperimentController experimentController;
  private String experimentId;

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
      trialFab = findViewById(R.id.fab_view_add_trial);

      experimentController = new ExperimentController(experimentId, () -> {

        ExperimentInfoFragment infoFragment = ExperimentInfoFragment
            .newInstance(experimentController.getExperimentId(),
                experimentController.getType());

        ExperimentMapFragment mapFragment = ExperimentMapFragment
            .newInstance(experimentController.getExperimentId());

        ExperimentTrialListFragment trialListFragment = ExperimentTrialListFragment
            .newInstance(experimentController.getExperimentId(),
                experimentController.getType());

        adapter.addFragment(infoFragment);
        adapter.addFragment(mapFragment);
        adapter.addFragment(trialListFragment);

        viewPager.setAdapter(adapter);

        viewPager.registerOnPageChangeCallback(new OnPageChangeCallback() {
          @Override
          public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            if (position == 2) {
              trialFab.show();
            } else {
              trialFab.hide();
            }
          }

          @Override
          public void onPageSelected(int position) {
            super.onPageSelected(position);
            if (position == 2) {
              trialFab.show();
            } else {
              trialFab.hide();
            }
          }
        });

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

      }, null);

    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == TrialNewActivity.NEW_TRIAL_REQUEST) {
      if (resultCode == RESULT_OK) {
        assert data != null;
        experimentController.addTrial((Trial) data.getSerializableExtra(TrialNewActivity.TRIAL_EXTRA));
      }
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.experiment_view_menu, menu);
    return true;
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    // Set Subscribe and Unsubscribe visibility based on user subscriptions
    MenuItem subscribeItem = menu.findItem(R.id.subscribe_experiment);
    MenuItem unsubscribeItem = menu.findItem(R.id.unsubscribe_experiment);

    UserController userController = new UserController(UserHelper.readUuid());
    userController.setUpdateCallback(user -> {
      if (user.getSubscriptions().contains(experimentId)) {
        subscribeItem.setVisible(false);
        unsubscribeItem.setVisible(true);
      } else {
        subscribeItem.setVisible(true);
        unsubscribeItem.setVisible(false);
      }
      userController.unregisterSnapshotListener();
    });
    return super.onPrepareOptionsMenu(menu);
  }

  public void showOwner(View view) {
    // TODO: Add actual behavior
    AlertDialog.Builder builder = new AlertDialog.Builder(this);

    builder.setMessage(experimentController.getExperimentContext().getOwnerUuid())
        .setTitle("DING DONG");

    AlertDialog dialog = builder.create();

    dialog.show();
  }

  public void fabNewTrial(View view) {
    Intent intent = new Intent(this, TrialNewActivity.class);
    intent.putExtra(TrialNewActivity.EXPERIMENT_ID, experimentId);
    intent.putExtra(TrialNewActivity.EXPERIMENT_TYPE, experimentController.getType());
    startActivityForResult(intent, TrialNewActivity.NEW_TRIAL_REQUEST);
  }

  static class ExperimentViewFragmentsAdapter extends FragmentStateAdapter {

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

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    int itemId = item.getItemId();

    // subscribe to experiment
    if (itemId == R.id.subscribe_experiment) {
      UserController userController = new UserController(UserHelper.readUuid());
      userController.setUpdateCallback(user -> userController.addSubscription(experimentId));
    }
    // unsubscribe to experiment
    else if (itemId == R.id.unsubscribe_experiment) {
      UserController userController = new UserController(UserHelper.readUuid());
      userController.setUpdateCallback(user -> userController.removeSubscription(experimentId));
    } else {
      return super.onOptionsItemSelected(item);
    }
    return true;
  }
}
