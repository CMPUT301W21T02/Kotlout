package xyz.kotlout.kotlout.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.controller.ExperimentController;
import xyz.kotlout.kotlout.controller.UserHelper;
import xyz.kotlout.kotlout.model.ExperimentType;
import xyz.kotlout.kotlout.model.experiment.Experiment;
import xyz.kotlout.kotlout.view.fragment.ExperimentListFragment;
import xyz.kotlout.kotlout.view.fragment.ExperimentListFragment.ListType;

public class MainActivity extends AppCompatActivity {

  private ViewPager2 viewPager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    if (savedInstanceState == null) {
      BottomNavigationView bnv = findViewById(R.id.nav_main);
      ExperimentListFragmentsAdapter adapter = new ExperimentListFragmentsAdapter(getSupportFragmentManager(), getLifecycle());
      viewPager = findViewById(R.id.pager_experiment_lists);
      viewPager.setAdapter(adapter);
      bnv.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.experiment_list_menu, menu);

    SearchManager searchManager =
        (SearchManager) getSystemService(Context.SEARCH_SERVICE);
    SearchView searchView =
        (SearchView) menu.findItem(R.id.search_experiments).getActionView();
    searchView.setSearchableInfo(
        searchManager.getSearchableInfo(getComponentName()));

    return true;
  }

  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    int id = item.getItemId();

    if (id == R.id.my_experiments_view) {
      viewPager.setCurrentItem(0);
      return true;
    } else if (id == R.id.all_experiments_view) {
      viewPager.setCurrentItem(1);
      return true;
    } else if (id == R.id.subscribed_experiments_view) {
      viewPager.setCurrentItem(2);
      return true;
    }
    return false;
  }

  public void fabNewExperiment(View view) {
    Intent intent = new Intent(this, ExperimentNewActivity.class);
    startActivityForResult(intent, ExperimentNewActivity.NEW_EXPERIMENT_REQUEST);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == ExperimentNewActivity.NEW_EXPERIMENT_REQUEST) {
      // Get newly created experiment and publish it to the database
      Experiment newExperiment;
      ExperimentType experimentType;
      try {
        newExperiment = (Experiment) data
            .getSerializableExtra(ExperimentNewActivity.EXPERIMENT_EXTRA);
        experimentType = (ExperimentType) data.getSerializableExtra(ExperimentNewActivity.TYPE_EXTRA);
      } catch (NullPointerException ignored) {
        return;
      }

      newExperiment.setOwnerUuid(UserHelper.readUuid());
      ExperimentController experimentController = new ExperimentController(newExperiment, experimentType);
      experimentController.publishNewExperiment();
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int itemId = item.getItemId();
    if (itemId == R.id.search_experiments) {
      return true;
    } else if (itemId == R.id.show_profile) {
      Intent intent = new Intent(this, ProfileActivity.class);
      intent.putExtra(UserHelper.UUID_INTENT, UserHelper.readUuid());
      startActivity(intent);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  /**
   * Adapter for the ViewPager2 in the MainActivity. This allows for movement and swipe motion.
   */
  static class ExperimentListFragmentsAdapter extends FragmentStateAdapter {

    ExperimentListFragment[] fragmentList;

    public ExperimentListFragmentsAdapter(@NonNull FragmentManager fragmentManager,
        @NonNull Lifecycle lifecycle) {
      super(fragmentManager, lifecycle);

      fragmentList = new ExperimentListFragment[]{
          ExperimentListFragment.newInstance(ListType.MINE),
          ExperimentListFragment.newInstance(ListType.ALL),
          ExperimentListFragment.newInstance(ListType.SUBSCRIBED)
      };
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
      return fragmentList[position];
    }

    @Override
    public int getItemCount() {
      return fragmentList.length;
    }
  }
}

