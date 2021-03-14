package xyz.kotlout.kotlout.view;

import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener;
import com.google.android.material.tabs.TabLayout.Tab;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.controller.ExperimentController;
import xyz.kotlout.kotlout.model.ExperimentType;
import xyz.kotlout.kotlout.view.fragment.ExperimentInfoFragment;
import xyz.kotlout.kotlout.view.fragment.ExperimentListFragment;
import xyz.kotlout.kotlout.view.fragment.ExperimentListFragment.ListType;

public class ExperimentViewActivity extends AppCompatActivity {

  public static final int VIEW_EXPERIMENT_REQUEST = 0;
  public static final String EXPERIMENT_ID = "EXPERIMENT";

  ExperimentController experimentController;

  String experimentId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_experiment_view);

    if (savedInstanceState == null) {
      Intent intent = getIntent();
      experimentId = intent.getStringExtra(EXPERIMENT_ID);

      FrameLayout experimentViewFrame = findViewById(R.id.frame_experiment_view);

      ProgressBar loadingBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
      loadingBar.setIndeterminate(true);

      experimentViewFrame.addView(loadingBar);

      experimentController = new ExperimentController(experimentId, () -> {
        experimentViewFrame.removeView(loadingBar);
        ExperimentInfoFragment fragment = ExperimentInfoFragment.newInstance(experimentController.getExperimentContext());
        getSupportFragmentManager()
            .beginTransaction()
            .add(R.id.frame_experiment_view, fragment)
            .commit();
      });
    }

    TabLayout tl = findViewById(R.id.tl_experiment_view);
    tl.addOnTabSelectedListener(new OnTabSelectedListener() {
      @Override
      public void onTabSelected(Tab tab) {

      }

      @Override
      public void onTabUnselected(Tab tab) {

      }

      @Override
      public void onTabReselected(Tab tab) {

      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.experiment_view_menu, menu);
    return true;
  }

}