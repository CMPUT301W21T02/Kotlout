package xyz.kotlout.kotlout.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.controller.UserHelper;
import xyz.kotlout.kotlout.model.ExperimentType;
import xyz.kotlout.kotlout.model.experiment.trial.BinomialTrial;
import xyz.kotlout.kotlout.model.experiment.trial.CountTrial;
import xyz.kotlout.kotlout.model.experiment.trial.MeasurementTrial;
import xyz.kotlout.kotlout.model.experiment.trial.NonNegativeTrial;

public class TrialNewActivity extends AppCompatActivity {

  public static final int NEW_TRIAL_REQUEST = 0;
  public static final String EXPERIMENT_ID = "EXPERIMENT";
  public static final String EXPERIMENT_TYPE = "TYPE";
  public static final String TRIAL_EXTRA = "TRIAL";

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
        radioButtons.setVisibility(View.VISIBLE);
        break;
      case MEASUREMENT:
        textEntry.setHint("Decimal Number");
        textEntry.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
        textEntry.setInputType(InputType.TYPE_CLASS_NUMBER);
        textEntry.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        radioButtons.setVisibility(View.GONE);
        textEntry.setVisibility(View.VISIBLE);
        break;
      case NON_NEGATIVE_INTEGER:
        textEntry.setHint("Non Negative Integer");
        textEntry.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        textEntry.setInputType(InputType.TYPE_CLASS_NUMBER);
        radioButtons.setVisibility(View.GONE);
        textEntry.setVisibility(View.VISIBLE);
        break;
      case COUNT:
        textEntry.setHint("Any Integer");
        textEntry.setKeyListener(DigitsKeyListener.getInstance("0123456789-"));
        textEntry.setInputType(InputType.TYPE_CLASS_NUMBER);
        textEntry.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
        radioButtons.setVisibility(View.GONE);
        textEntry.setVisibility(View.VISIBLE);
        break;
      case UNKNOWN:
        break;
    }

    Button submitTrial = findViewById(R.id.btn_new_trial_submit);
    submitTrial.setOnClickListener(this::submitTrial);
  }

  private void submitTrial(View v) {

    String uuid = UserHelper.readUuid();
    String userInput = textEntry.getText().toString();
    Intent intent = new Intent();
    try {
      switch (type) {

        case BINOMIAL:
          boolean result = radioButtons.getCheckedRadioButtonId() == R.id.radio_success;
          BinomialTrial newBinomialTrial = new BinomialTrial(result, uuid);
          intent.putExtra(TRIAL_EXTRA, newBinomialTrial);
          break;
        case NON_NEGATIVE_INTEGER:

          NonNegativeTrial newNonNegativeTrial = new NonNegativeTrial(Long.parseLong(userInput), uuid);
          intent.putExtra(TRIAL_EXTRA, newNonNegativeTrial);
          break;
        case COUNT:
          CountTrial newCountTrial = new CountTrial(Long.parseLong(userInput), uuid);
          intent.putExtra(TRIAL_EXTRA, newCountTrial);
          break;
        case MEASUREMENT:
          MeasurementTrial measurementTrial = new MeasurementTrial(Double.parseDouble(userInput), uuid);
          intent.putExtra(TRIAL_EXTRA, measurementTrial);
          break;
        case UNKNOWN:
          break;
      }

      setResult(RESULT_OK, intent);
      finish();

    } catch (NumberFormatException exception) {
      textEntry.setError("Number too large");
    }
  }
}
