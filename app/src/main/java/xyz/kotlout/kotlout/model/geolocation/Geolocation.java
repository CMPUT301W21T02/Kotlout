package xyz.kotlout.kotlout.model.geolocation;

import java.io.Serializable;

/**
 * A class representing a location on earth.
 */
public class Geolocation implements Serializable {

  private Double latitude;
  private Double longitude;

  /**
   * Instantiates a new empty Geolocation. (Empty for Firebase)
   */
  public Geolocation() {
    this.longitude = null;
    this.latitude = null;
  }

  /**
   * Instantiates a new Geolocation.
   *
   * @param latitude  the latitude
   * @param longitude the longitude
   */
  public Geolocation(Double latitude, Double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  /**
   * Get the latitude of a geolocation.
   *
   * @return the latitude
   */
  public double getLatitude() {
    return latitude;
  }

  /**
   * Set the latitude of a geolocation.
   *
   * @param latitude the latitude
   */
  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  /**
   * Get the longitude of a geolocation..
   *
   * @return the longitude
   */
  public double getLongitude() {
    return longitude;
  }

  /**
   * Set the longitude of a geolocation.
   *
   * @param longitude the longitude
   */
  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  /**
   * Set both the latitude and longitude of a geolocation.
   *
   * @param latitude  the latitude
   * @param longitude the longitude
   */
  public void setGeolocation(Double latitude, Double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }
}
