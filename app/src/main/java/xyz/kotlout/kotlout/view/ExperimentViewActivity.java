package xyz.kotlout.kotlout.view;

import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.controller.ExperimentController;

public class ExperimentViewActivity extends AppCompatActivity {

    public static String EXPERIMENT_ARGUMENT = "EXPERIMENT";

    ExperimentController experimentController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_view);

        Intent intent = getIntent();
        String experimentId = intent.getStringExtra(EXPERIMENT_ARGUMENT);

        experimentController = new ExperimentController(experimentId);

        InfoHeaderView infoHeader = findViewById(R.id.ihv_experiment_info);
        infoHeader.setExperiment(experimentController.getExperimentContext());

    TabLayout tl = findViewById(R.id.tl_experiment_view);

//        tl.addOnTabSelectedListener(onTabSelectedListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.experiment_view_menu, menu);
        return true;
    }

}