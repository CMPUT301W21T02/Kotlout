package xyz.kotlout.kotlout.view;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import xyz.kotlout.kotlout.R;

public class TrialNewActivity extends AppCompatActivity {

  public static final int NEW_TRIAL_REQUEST = 0;
  public static final String TRIAL_EXTRA = "TRIAL";
  public static final String EXPERIMENT_ID = "EXPERIMENT";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }
}
