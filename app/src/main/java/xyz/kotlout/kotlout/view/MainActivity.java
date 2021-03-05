package xyz.kotlout.kotlout.view;

import android.os.Bundle;

import android.view.Menu;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import xyz.kotlout.kotlout.R;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.experiment_list_menu, menu);
    return true;
  }

  public void onNavigationBarTapped(@NonNull View item) {
    int id = item.getId();

    if (id == R.id.my_experiments_view) {

    } else if (id == R.id.all_experiments_view) {

    } else if (id == R.id.subscribed_experiments_view) {

    }
  }
}