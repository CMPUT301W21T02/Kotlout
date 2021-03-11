package xyz.kotlout.kotlout.view;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import xyz.kotlout.kotlout.R;

public class ExperimentNewActivity extends AppCompatActivity {

  public static final int NEW_EXPERIMENT_REQUEST = 0;
  public static final String EXPERIMENT_EXTRA = "EXPERIMENT";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_experiment_new);
  }

  public void addNewExperiment(View view) {

    // Man this is ugly
    String description = ((EditText) view.findViewById(R.id.et_experiment_new_description)).getText().toString();
    String region = ((EditText) view.findViewById(R.id.et_experiment_new_region)).getText().toString();
    int minTrials = Integer.parseInt(((EditText) view.findViewById(R.id.et_experiment_new_min_trials)).getText().toString());

    view.findViewById(R.id.sp_experiment_new_type);

    Intent intent = new Intent();
//    intent.putExtra(EXPERIMENT_EXTRA, newExperiment);
    setResult(NEW_EXPERIMENT_REQUEST, intent);
    finish();
  }
}