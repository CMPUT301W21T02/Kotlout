package xyz.kotlout.kotlout.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Map;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.controller.ExperimentController;
import xyz.kotlout.kotlout.controller.ExperimentListController;

/**
 * Base code used: https://developer.android.com/training/search
 */
public class SearchActivity extends AppCompatActivity {

  Query experimentsQuery;
  String searchQuery;
  ListView resultList;
  SearchResultAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);
    resultList = findViewById(R.id.lv_search_results);
    adapter = new SearchResultAdapter(this, new ArrayList<>());
    resultList.setAdapter(adapter);
    resultList.setOnItemClickListener((parent, view, position, id) -> {
      String experimentId = adapter.getItem(position).getExperimentId();
      Intent intent = new Intent(this, ExperimentViewActivity.class);

      intent.putExtra(ExperimentViewActivity.EXPERIMENT_ID, experimentId);
      startActivityForResult(intent, 0);

    });
    handleIntent(getIntent());
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    handleIntent(intent);
  }

  private void handleIntent(@NonNull Intent intent) {
    if (intent.getAction().equals(Intent.ACTION_SEARCH)) {
      experimentsQuery = ExperimentListController.getAllExperiments();
      searchQuery = intent.getStringExtra(SearchManager.QUERY).toLowerCase();

      experimentsQuery.addSnapshotListener(this::showSearchResults);
    }
  }

  private void showSearchResults(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
    adapter.clear();

    if (queryDocumentSnapshots != null) {
      for (QueryDocumentSnapshot experimentDoc : queryDocumentSnapshots) {
        Map<String, Object> fields = experimentDoc.getData();

        if (fields.containsKey("description")) {
          String description = (String) fields.get("description");
          if (description.toLowerCase().contains(searchQuery)) {
            adapter.add(new ExperimentController(experimentDoc));
            continue;
          }
        }

        if (fields.containsKey("region")) {
          String region = (String) fields.get("region");
          if (region.toLowerCase().contains(searchQuery)) {
            adapter.add(new ExperimentController(experimentDoc));
          }
        }
      }
    }
    adapter.notifyDataSetChanged();
  }


  public class SearchResultAdapter extends ArrayAdapter<ExperimentController> {

    private final Context context;
    private final ArrayList<ExperimentController> controllers;

    public SearchResultAdapter(@NonNull Context context, ArrayList<ExperimentController> controllers) {
      super(context, 0, controllers);
      this.context = context;
      this.controllers = controllers;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

      if (convertView == null) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.experiment_list_item, parent, false);
      }

      TextView description = convertView
          .findViewById(R.id.tv_experiment_list_description);
      TextView region = convertView.findViewById(R.id.tv_experiment_list_region);
      TextView counter = convertView.findViewById(R.id.tv_experiment_list_counter);
      TextView type = convertView.findViewById(R.id.tv_experiment_list_type);
      convertView.findViewById(R.id.iv_experiment_list_visible).setVisibility(View.GONE);
      ExperimentController experimentController = controllers.get(position);

      description.setText(experimentController.getExperimentContext().getDescription());
      region.setText(experimentController.getExperimentContext().getRegion());
      counter.setText(experimentController.generateCountText());

      type.setText(experimentController.getType().toString());

      return convertView;
    }
  }
}