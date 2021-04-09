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
import org.osmdroid.views.MapViewRepository;
import xyz.kotlout.kotlout.BuildConfig;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.model.ExperimentType;
import xyz.kotlout.kotlout.model.adapter.TrialMarkerAdapter;

public class ExperimentMapFragment extends Fragment {

  private static final String ARG_EXPERIMENT = "EXPERIMENT";
  private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
  private MapView map = null;
  private int trialsMarked = 0;
  String experimentId;
  View view;
  ExperimentType type;
  TrialMarkerAdapter markIt;


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


    markIt = new TrialMarkerAdapter(getContext(), experimentId, type, map);
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

    map = (MapView) mView.findViewById(R.id.map);
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
    MapTileProviderBasic tileProvider = new MapTileProviderBasic(getContext().getApplicationContext(), TileSourceFactory.MAPNIK);
    SimpleInvalidationHandler mTileRequestCompleteHandler = new SimpleInvalidationHandler(map);
    tileProvider.getTileRequestCompleteHandlers().add(mTileRequestCompleteHandler);
    map.setTileProvider(tileProvider);
    MapViewRepository mvr = map.getRepository();
    markIt.onResume();
    markIt.placeMarkers();
  }
}
