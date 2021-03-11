package xyz.kotlout.kotlout.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.controller.ExperimentController;
import xyz.kotlout.kotlout.model.ExperimentType;

/**
 * Activity used for creating a new experiment.
 */
public class ExperimentNewActivity extends AppCompatActivity {

  public static final int NEW_EXPERIMENT_REQUEST = 0;
  public static final String EXPERIMENT_EXTRA = "EXPERIMENT";
  EditText descriptionEditText;
  Button submitButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_experiment_new);

    submitButton = this.findViewById(R.id.btn_experiment_new_add);
    descriptionEditText = (EditText) this.findViewById(R.id.et_experiment_new_description);
    descriptionEditText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        // Prevent creating a new experiment without a description
        String description = s.toString();
        submitButton.setEnabled(!description.trim().isEmpty());
      }
    });
  }

  /**
   * Gather details for the new experiment and close the activity.
   *
   * @param view The floating action button from the main activity.
   */
  public void addNewExperiment(View view) {

    String description = ((EditText) this.findViewById(R.id.et_experiment_new_description))
        .getText().toString();

    String region = ((EditText) this.findViewById(R.id.et_experiment_new_region)).getText()
        .toString();

    // default minimum trials is 0 if field left blank
    int minTrials = 0;
    try {
      Integer.parseInt(
          ((EditText) this.findViewById(R.id.et_experiment_new_min_trials)).getText().toString());

    } catch (NumberFormatException ignored) {
    }

    int typeId = (int) ((Spinner) this.findViewById(R.id.sp_experiment_new_type))
        .getSelectedItemId();

    ExperimentController experimentController = new ExperimentController();
    experimentController.setExperimentContext(description, region,
        minTrials, ExperimentType.values()[typeId]);

    Intent intent = new Intent();
    intent.putExtra(EXPERIMENT_EXTRA, experimentController.getExperimentContext());
    setResult(NEW_EXPERIMENT_REQUEST, intent);
    finish();
  }
}