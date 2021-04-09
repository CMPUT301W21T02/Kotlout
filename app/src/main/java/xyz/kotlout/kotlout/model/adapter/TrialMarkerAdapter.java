package xyz.kotlout.kotlout.model.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.IconOverlay;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.controller.ExperimentController;
import xyz.kotlout.kotlout.controller.LocationHelper;
import xyz.kotlout.kotlout.model.ExperimentType;
import xyz.kotlout.kotlout.model.experiment.trial.Trial;

public class TrialMarkerAdapter {
  private List<? extends Trial> trialList;
  Map<String, ? extends List<? extends Trial>> ByExperimenter;

  private final Context context;

  ExperimentType type;
  String experimentId;
  ExperimentController controller;
  MapView map;
  private int trialsMarked = 0;
  List<String> Experimenters;
  private boolean isActive = false;

  public TrialMarkerAdapter(Context context, String experimentId, ExperimentType type, MapView map) {
    this.context = context;
    this.experimentId = experimentId;
    this.type = type;
    this.map = map;

    controller = new ExperimentController(experimentId, this::onExperimentLoaded, this::onExperimentLoaded);
    Experimenters = new ArrayList<>();
    trialList = new ArrayList<>();
    isActive = true;

  }

  public void onExperimentLoaded() {
    trialList = controller.getListTrials();
    placeMarkers();
  }

  public void placeMarkers() {
    trialsMarked = 0;
    map.getOverlays().clear();
    for (Trial trial: trialList) {
      if (trial.getLocation() != null) {
        trialsMarked++;
        GeoPoint trialLocation = LocationHelper.toGeoPoint(trial.getLocation());
        if (map != null) {
          Drawable icon = context.getResources().getDrawable(R.drawable.ic_baseline_exp_location);
          IconOverlay mark = new IconOverlay(trialLocation, icon);
          mark.set(trialLocation, icon);
          map.getOverlays().add(mark);
        }
      }
    }
  }

  public void onPause() {
    isActive = false;
  }

  public void onResume() {
    isActive = true;
  }

  public int getTrialsMarked() {
    return trialsMarked;
  }
}
