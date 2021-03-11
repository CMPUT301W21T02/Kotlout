package xyz.kotlout.kotlout.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.controller.ExperimentController;
import xyz.kotlout.kotlout.controller.UserController;
import xyz.kotlout.kotlout.model.experiment.Experiment;
import xyz.kotlout.kotlout.model.user.User;
import xyz.kotlout.kotlout.view.fragment.ExperimentListFragment;
import xyz.kotlout.kotlout.view.fragment.ExperimentListFragment.ListType;

public class MainActivity extends AppCompatActivity {

  private UserController userController;
  List<Experiment> experimentList = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    if (savedInstanceState == null) {
      ExperimentListFragment fragment = ExperimentListFragment.newInstance(ListType.MINE);
      getSupportFragmentManager()
          .beginTransaction()
          .add(R.id.frame_main, fragment)
          .commit();
    }

    BottomNavigationView bnv = findViewById(R.id.nav_main);
    bnv.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.experiment_list_menu, menu);
    return true;
  }

  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    int id = item.getItemId();

    if (id == R.id.my_experiments_view) {
      ExperimentListFragment fragment = ExperimentListFragment.newInstance(ListType.MINE);
      getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.frame_main, fragment)
          .commit();

      return true;
    } else if (id == R.id.all_experiments_view) {
      ExperimentListFragment fragment = ExperimentListFragment.newInstance(ListType.ALL);
      getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.frame_main, fragment)
          .commit();

      return true;
    } else if (id == R.id.subscribed_experiments_view) {
      ExperimentListFragment fragment = ExperimentListFragment.newInstance(ListType.SUBSCRIBED);
      getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.frame_main, fragment)
          .commit();

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
      Experiment newExperiment = (Experiment) data
          .getSerializableExtra(ExperimentNewActivity.EXPERIMENT_EXTRA);
//      newExperiment.setOwner(userController.getUser()); // TODO
      newExperiment.setOwner(new User("Bobby", "Tables",
          "btables@ualberta.ca", "123-456-7890"));

      ExperimentController experimentController = new ExperimentController(newExperiment);
      experimentController.publish();
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.sync_experiments:
        return true;

      case R.id.show_profile:
        Intent intent = new Intent(this, TestUserActivity.class);
        this.startActivity(intent);
        return true;

      case R.id.search_experiments:
        return true;

      default:
        return super.onOptionsItemSelected(item);
    }
  }
}