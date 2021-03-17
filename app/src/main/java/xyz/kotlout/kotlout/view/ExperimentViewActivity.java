package xyz.kotlout.kotlout.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import xyz.kotlout.kotlout.R;

public class ExperimentViewActivity extends AppCompatActivity {

    public static final int VIEW_EXPERIMENT_REQUEST = 0;
    public static final String EXPERIMENT_ID = "EXPERIMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}