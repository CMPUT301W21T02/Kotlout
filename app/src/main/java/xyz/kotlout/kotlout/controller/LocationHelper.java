package xyz.kotlout.kotlout.controller;

import org.osmdroid.util.GeoPoint;
import xyz.kotlout.kotlout.model.geolocation.Geolocation;

/**
 * Function to help with location stuff
 */
public class LocationHelper {

  /**
   * Converts a GeoPoint to a Geolocation
   *
   * @param geoPoint to convert
   * @return geolocation location type
   */
  public static Geolocation toGeolocation(GeoPoint geoPoint) {
    return new Geolocation((Double) geoPoint.getLatitude(), (Double) geoPoint.getLongitude());
  }

  /**
   * Converts a Geolocation to a GeoPoint
   *
   * @param geolocation to convert
   * @return geoPoint location type
   */
  public static GeoPoint toGeoPoint(Geolocation geolocation) {
    return new GeoPoint((double) geolocation.getLatitude(), (double) geolocation.getLongitude());
  }
}
