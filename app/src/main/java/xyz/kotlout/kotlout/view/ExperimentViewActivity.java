package xyz.kotlout.kotlout.view;

import android.os.Bundle;

import android.view.Menu;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;
import xyz.kotlout.kotlout.R;

public class ExperimentViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tl = findViewById(R.id.tl_experiment_view);

//        tl.addOnTabSelectedListener(onTabSelectedListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.experiment_view_menu, menu);
        return true;
    }

}