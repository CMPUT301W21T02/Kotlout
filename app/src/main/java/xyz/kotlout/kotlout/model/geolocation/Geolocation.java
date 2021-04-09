package xyz.kotlout.kotlout.model.geolocation;

import java.io.Serializable;

/**
 * A class representing a location on earth
 */
public class Geolocation implements Serializable {

  private Double latitude;
  private Double longitude;

  public Geolocation() {
    this.longitude = null;
    this.latitude = null;
  }

  public Geolocation(Double latitude, Double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  public void setGeolocation(Double latitude, Double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }
}
