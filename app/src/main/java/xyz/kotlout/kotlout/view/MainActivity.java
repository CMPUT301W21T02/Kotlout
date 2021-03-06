package xyz.kotlout.kotlout.view;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.view.fragment.ExperimentListFragment;
import xyz.kotlout.kotlout.view.fragment.ExperimentListFragment.ListType;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    if (savedInstanceState == null) {
      ExperimentListFragment fragment = ExperimentListFragment.newInstance(ListType.MINE);
      getSupportFragmentManager()
          .beginTransaction()
          .add(R.id.fragment_frame, fragment)
          .commit();
    }

    BottomNavigationView bnv = findViewById(R.id.bottom_navigation);
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
          .replace(R.id.fragment_frame, fragment)
          .commit();
      return true;
    } else if (id == R.id.all_experiments_view) {
      ExperimentListFragment fragment = ExperimentListFragment.newInstance(ListType.ALL);
      getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.fragment_frame, fragment)
          .commit();
      return true;
    } else if (id == R.id.subscribed_experiments_view) {
      ExperimentListFragment fragment = ExperimentListFragment.newInstance(ListType.SUBSCRIBED);
      getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.fragment_frame, fragment)
          .commit();
      return true;
    }

    return false;
  }
}