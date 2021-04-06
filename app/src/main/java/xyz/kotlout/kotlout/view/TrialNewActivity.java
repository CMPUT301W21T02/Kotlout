package xyz.kotlout.kotlout.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.model.ExperimentType;

public class TrialNewActivity extends AppCompatActivity {

  public static final int NEW_TRIAL_REQUEST = 0;
  public static final String EXPERIMENT_ID = "EXPERIMENT";
  public static final String EXPERIMENT_TYPE = "TYPE";

  ExperimentType type;

  RadioGroup radioButtons;
  EditText textEntry;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_trial);

    Intent intent = getIntent();
    type = (ExperimentType) intent.getSerializableExtra(EXPERIMENT_TYPE);

    radioButtons = findViewById(R.id.rg_new_trial);
    textEntry = findViewById(R.id.editTextNumber);

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

    Button submitTrial = findViewById(R.id.btn_new_trial_submit);
    submitTrial.setOnClickListener(this::submitTrial);
  }

  private void submitTrial(View v) {
    // TODO: Figure out how to do this
      switch (type) {
        case BINOMIAL:
          break;
        case NON_NEGATIVE_INTEGER:
        case COUNT:
        case MEASUREMENT:

          break;
        case UNKNOWN:
          break;
      }


  }
}
