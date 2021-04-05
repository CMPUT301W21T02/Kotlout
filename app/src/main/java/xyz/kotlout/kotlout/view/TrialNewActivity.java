package xyz.kotlout.kotlout.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.model.ExperimentType;

public class TrialNewActivity extends AppCompatActivity {

  public static final int NEW_TRIAL_REQUEST = 0;
  public static final String TRIAL_EXTRA = "TRIAL";
  public static final String EXPERIMENT_ID = "EXPERIMENT";
  public static final String EXPERIMENT_TYPE = "TYPE";

  ExperimentType type;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_trial);

    RadioGroup radioButtons = findViewById(R.id.rg_new_trial);
    EditText textEntry = findViewById(R.id.editTextNumber);
    Intent intent = getIntent();
    type = (ExperimentType) intent.getSerializableExtra(EXPERIMENT_TYPE);

    switch (type) {
      case BINOMIAL:
        textEntry.setVisibility(View.GONE);
        break;
      case NON_NEGATIVE_INTEGER:
      case COUNT:
      case MEASUREMENT:
        radioButtons.setVisibility(View.GONE);
        break;
      case UNKNOWN:
        break;
    }


  }
}
