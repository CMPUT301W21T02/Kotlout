package xyz.kotlout.kotlout.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

/**
 * This is the activity for when an experiment is launched.
 */
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
        SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.blocklist_file_key), Context.MODE_PRIVATE);

        ExperimentInfoFragment infoFragment = ExperimentInfoFragment
            .newInstance(experimentController.getExperimentId(),
                experimentController.getType(), sharedPrefs);

        ExperimentMapFragment mapFragment = ExperimentMapFragment
            .newInstance(experimentController.getExperimentId());

        ExperimentTrialListFragment trialListFragment = ExperimentTrialListFragment
            .newInstance(experimentController.getExperimentId(),
                experimentController.getType(), sharedPrefs);

        adapter.addFragment(infoFragment);
        adapter.addFragment(mapFragment);
        adapter.addFragment(trialListFragment);

        trialListFragment.setIgnoreListener(infoFragment::ignoreListUpdated);

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

    UserController userController = new UserController(UserHelper.readUuid());
    userController.setUpdateCallback(user -> {
      if (user.getSubscriptions().contains(experimentId)) {
        subscribeItem.setIcon(R.drawable.ic_baseline_bookmark);
      } else {
        subscribeItem.setIcon(R.drawable.ic_baseline_bookmark_border);
      }
      userController.unregisterSnapshotListener();
    });
    return super.onPrepareOptionsMenu(menu);
  }

  /**
   * Callback for the floating action button
   * which launches a fragment to add additional trials.
   * @param view The floating action button
   */
  public void fabNewTrial(View view) {
    Intent intent = new Intent(this, TrialNewActivity.class);
    intent.putExtra(TrialNewActivity.EXPERIMENT_ID, experimentId);
    intent.putExtra(TrialNewActivity.EXPERIMENT_TYPE, experimentController.getType());
    intent.putExtra(TrialNewActivity.REQUIRE_LOCATION, experimentController.getExperimentContext().isGeolocationRequired());
    startActivityForResult(intent, TrialNewActivity.NEW_TRIAL_REQUEST);
  }


  static class ExperimentViewFragmentsAdapter extends FragmentStateAdapter {

    List<Fragment> fragmentList = new ArrayList<>();


    /**
     * Initializes the adapter
     * @param fragmentManager Its manager
     * @param lifecycle Its lifecycle
     */
    public ExperimentViewFragmentsAdapter(@NonNull FragmentManager fragmentManager,
        @NonNull Lifecycle lifecycle) {
      super(fragmentManager, lifecycle);
    }

    /**
     * Add a fragment
     * @param fragment the fragment
     */
    public void addFragment(Fragment fragment) {
      fragmentList.add(fragment);
    }

    /**
     * Remove a fragment
     * @param fragment the fragment
     */
    public void removeFragment(Fragment fragment) {
      fragmentList.remove(fragment);
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
    int i = item.getItemId();
    if (i == R.id.open_discussion_posts) {

      Intent intent = new Intent(this, DiscussionPostsActivity.class);
      intent.putExtra(DiscussionPostsActivity.ON_EXPERIMENT_INTENT, experimentId);
      startActivity(intent);
      return true;

    } else if (i == R.id.subscribe_experiment) {
      UserController userController = new UserController(UserHelper.readUuid());
      userController.setUpdateCallback(user -> {
        if (user.getSubscriptions().contains(experimentId)) {
          userController.removeSubscription(experimentId);
          item.setIcon(R.drawable.ic_baseline_bookmark_border);
        } else {
          userController.addSubscription(experimentId);
          item.setIcon(R.drawable.ic_baseline_bookmark);
        }
      });
      return true;
    } else {
      return super.onOptionsItemSelected(item);
    }
  }
}
