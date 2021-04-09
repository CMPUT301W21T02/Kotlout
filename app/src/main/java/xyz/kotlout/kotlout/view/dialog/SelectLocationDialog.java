package xyz.kotlout.kotlout.view.dialog;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.DelayedMapListener;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.IconOverlay;

import java.util.ArrayList;
import xyz.kotlout.kotlout.BuildConfig;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.controller.LocationHelper;
import xyz.kotlout.kotlout.model.geolocation.Geolocation;

public class SelectLocationDialog extends DialogFragment {
  private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
  private MapView map = null;
  private OnFragmentInteractionListener listener;
  private GeoPoint selection;
  private IconOverlay middleMark;
  private IMapController mapController;
  private GeoPoint oldLocation;

  public interface OnFragmentInteractionListener {
    void onOkPressed(GeoPoint newPoint);
  }

  /* A lot of the map code comes from */

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnFragmentInteractionListener){
      listener = (OnFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString()
          + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    oldLocation = null;
    if (getArguments() != null) {
      oldLocation = LocationHelper.toGeoPoint((Geolocation) getArguments().getSerializable("location"));
    }

    requestPermissionsIfNecessary(new String[] {
        // if you need to show the current location, uncomment the line below
        Manifest.permission.ACCESS_FINE_LOCATION,
        // WRITE_EXTERNAL_STORAGE is required in order to show the map
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    });

    //handle permissions first, before map is created. not depicted here

    //load/initialize the osmdroid configuration, this can be done
    Context ctx = getContext();
    Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_map_view, null);
    Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
    map = (MapView) view.findViewById(R.id.map);
    map.setTileSource(TileSourceFactory.MAPNIK);
    map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.ALWAYS);
    map.setMultiTouchControls(true);

    mapController = map.getController();
    mapController.setZoom(15.0);


    GeoPoint startPoint;
    if (oldLocation == null) {
      startPoint = new GeoPoint(53.52290725708008, -113.5255355834961);
    } else {
      startPoint = oldLocation;
    }
    Drawable icon = getContext().getResources().getDrawable(R.drawable.ic_baseline_exp_location);
    IconOverlay middleMark = new IconOverlay(startPoint, icon);
    middleMark.set(startPoint, icon);
    mapController.setCenter(startPoint);


    map.getOverlays().add(middleMark);

    /* https://stackoverflow.com/a/48839860
     * How to set Drag Start and End listener on my custom osmdroid map?
     * answered Feb 17 '18 at 9:31 by Ali Zain */
    map.setMapListener(new DelayedMapListener(new MapListener() {
      @Override
      public boolean onScroll(ScrollEvent paramScrollEvent) {
        updateSelection();
        return true;
      }

      @Override
      public boolean onZoom(ZoomEvent event) {
        updateSelection();
        return true;
      }
    }));

    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

    return builder
        .setView(view)
        .setTitle("Select Location")
        .setPositiveButton("OK", (dialogInterface, i) ->
            listener.onOkPressed(selection)).create();
  }



  private void updateSelection() {
    IGeoPoint geoPoint = map.getMapCenter();
    selection = new GeoPoint(geoPoint.getLatitude(), geoPoint.getLongitude());
    map.getOverlays().clear();
    Drawable icon = getContext().getResources().getDrawable(R.drawable.ic_baseline_exp_location);
    middleMark = new IconOverlay(selection, icon);
    map.getOverlays().add(middleMark);
    map.invalidate();
  }

  @Override
  public void onResume() {
    super.onResume();
    map.onResume();
  }

  @Override
  public void onPause() {
    super.onPause();
    map.onPause();
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
}
