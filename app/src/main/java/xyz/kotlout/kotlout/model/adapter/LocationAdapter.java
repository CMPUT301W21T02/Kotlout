package xyz.kotlout.kotlout.model.adapter;

import org.osmdroid.util.GeoPoint;
import xyz.kotlout.kotlout.model.geolocation.Geolocation;

public class LocationAdapter {
  public static Geolocation toGeolocation(GeoPoint geoPoint) {
    return new Geolocation((Double) geoPoint.getLatitude(), (Double) geoPoint.getLongitude());
  }

  public static GeoPoint toGeoPoint(Geolocation geolocation) {
    return new GeoPoint((double) geolocation.getLatitude(), (double) geolocation.getLongitude());
  }
}
