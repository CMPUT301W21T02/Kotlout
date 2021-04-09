package xyz.kotlout.kotlout.view.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.util.SimpleInvalidationHandler;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import xyz.kotlout.kotlout.BuildConfig;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.model.ExperimentType;
import xyz.kotlout.kotlout.model.adapter.TrialMarkAdapter;

/**
 * The ExperimentMapFragment, displays a map with markers on where trials have been recorded.
 */
public class ExperimentMapFragment extends Fragment {

  private static final String ARG_EXPERIMENT = "EXPERIMENT";
  private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
  private final int trialsMarked = 0;
  private MapView map = null;
  private String experimentId;
  private View view;
  private ExperimentType type;
  private TrialMarkAdapter trialMarker;


  /**
   * Creates an experiment map fragment.
   *
   * @param experimentId the experiment id to get data from
   * @return the experiment map fragment
   */
  public static ExperimentMapFragment newInstance(String experimentId) {
    ExperimentMapFragment fragment = new ExperimentMapFragment();
    Bundle args = new Bundle();
    args.putString(ARG_EXPERIMENT, experimentId);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      experimentId = getArguments().getString(ARG_EXPERIMENT);
    }
    setRetainInstance(true);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    setRetainInstance(true);
    // Inflate the layout for this fragment
    view = inflater.inflate(R.layout.fragment_experiment_map, container, false);
    Log.d("This msg was called", "onCreateView: ");
    configureMap(view);

    trialMarker = new TrialMarkAdapter(getContext(), experimentId, map);
    return view;
  }

  private void configureMap(View mView) {
    requestPermissionsIfNecessary(new String[]{
        // if you need to show the current location, uncomment the line below
        Manifest.permission.ACCESS_FINE_LOCATION,
        // WRITE_EXTERNAL_STORAGE is required in order to show the map
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    });

    Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

    map = mView.findViewById(R.id.map);
    map.setTileSource(TileSourceFactory.MAPNIK);
    map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.ALWAYS);
    map.setMultiTouchControls(true);
    IMapController mapController = map.getController();
    mapController.setZoom(15.0);
    GeoPoint startPoint = new GeoPoint(53.52290725708008, -113.5255355834961);
    mapController.setCenter(startPoint);
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

  }

  /**
   * Handles the result of the permissions
   * <p>
   * https://github.com/osmdroid/osmdroid/wiki/How-to-use-the-osmdroid-library-(Java) This function is from the open street maps
   * wiki
   */
  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    ArrayList<String> permissionsToRequest = new ArrayList<>();
    for (int i = 0; i < grantResults.length; i++) {
      permissionsToRequest.add(permissions[i]);
    }
    if (permissionsToRequest.size() > 0) {
      ActivityCompat.requestPermissions(
          getActivity(),
          permissionsToRequest.toArray(new String[0]),
          REQUEST_PERMISSIONS_REQUEST_CODE);
    }
  }

  /**
   * Request permissions from the user if they have not yet been provided.
   * <p>
   * https://github.com/osmdroid/osmdroid/wiki/How-to-use-the-osmdroid-library-(Java) This function is from the open street maps
   * wiki
   *
   * @param permissions A list of required permissions to request
   */
  private void requestPermissionsIfNecessary(String[] permissions) {
    ArrayList<String> permissionsToRequest = new ArrayList<>();
    for (String permission : permissions) {
      if (ContextCompat.checkSelfPermission(getContext(), permission)
          != PackageManager.PERMISSION_GRANTED) {
        // Permission is not granted
        permissionsToRequest.add(permission);
      }
    }
    if (permissionsToRequest.size() > 0) {
      ActivityCompat.requestPermissions(
          getActivity(),
          permissionsToRequest.toArray(new String[0]),
          REQUEST_PERMISSIONS_REQUEST_CODE);
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    map.onPause();
  }

  @Override
  public void onResume() {
    super.onResume();
    map.onResume();

    /* https://github.com/osmdroid/osmdroid/issues/277#issuecomment-412099853 */
    MapTileProviderBasic tileProvider = new MapTileProviderBasic(getContext().getApplicationContext(),
        TileSourceFactory.MAPNIK);
    SimpleInvalidationHandler mTileRequestCompleteHandler = new SimpleInvalidationHandler(map);
    tileProvider.getTileRequestCompleteHandlers().add(mTileRequestCompleteHandler);
    map.setTileProvider(tileProvider);

    // Replace the markers since they get removed each time the map is paused.
    trialMarker.placeMarkers();
  }
}
