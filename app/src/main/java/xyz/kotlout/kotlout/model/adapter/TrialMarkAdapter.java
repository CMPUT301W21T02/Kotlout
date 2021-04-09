package xyz.kotlout.kotlout.model.adapter;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.graphics.drawable.Drawable;
import java.util.ArrayList;
import java.util.List;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.IconOverlay;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.controller.ExperimentController;
import xyz.kotlout.kotlout.controller.LocationHelper;
import xyz.kotlout.kotlout.model.experiment.trial.Trial;

/**
 * Adapter for using geolocations as map markers
 */
public class TrialMarkAdapter {

  private final Context context;
  private String experimentId;
  private MapView map;

  private ExperimentController controller;
  private List<? extends Trial> trialList;

  /**
   * Instantiates a new Trial mark adapter.
   *
   * The TrialMarkAdapter can be used for taking experiment
   * trial location and displaying them onto a mapView.
   *
   * @param context      the context of the map
   * @param experimentId the experiment id for the trials
   * @param map          the mapView to place the markers on
   */
  public TrialMarkAdapter(Context context, String experimentId, MapView map) {
    this.context = context;
    this.experimentId = experimentId;
    this.map = map;

    controller = new ExperimentController(experimentId, this::onExperimentLoaded, this::onExperimentLoaded);
    trialList = new ArrayList<>();
  }

  /**
   * Callback for every time the experiment is loaded and updated.
   */
  public void onExperimentLoaded() {
    trialList = controller.getListTrials();
    placeMarkers();
  }

  /**
   * Place markers on all the stored trial locations.
   */
  public void placeMarkers() {
    // Need to clear all the drawn markers since we are placing new ones
    map.getOverlays().clear();
    for (Trial trial: trialList) {
      if (trial.getLocation() != null) {

        // For all the experiments that do have geolocations, convert them to a GeoPoint
        GeoPoint trialLocation = LocationHelper.toGeoPoint(trial.getLocation());
        if (map != null) {

          // Create a marker to place on the map
          Theme theme = context.getTheme();
          Drawable icon = context.getResources().getDrawable(R.drawable.ic_baseline_exp_location, theme);
          IconOverlay mark = new IconOverlay(trialLocation, icon);

          // Place said marker on the map
          mark.set(trialLocation, icon);
          map.getOverlays().add(mark);

          // Redraw the map after our updates
          map.invalidate();
        }
      }
    }
  }
}
