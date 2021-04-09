package xyz.kotlout.kotlout.model.experiment.trial;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import xyz.kotlout.kotlout.model.geolocation.Geolocation;

/**
 * Base trial class extended by other trial types.
 */
public abstract class Trial implements Serializable {

  private String experimenterId;
  private Date timestamp;
  private Geolocation location;

  /**
   * Instantiates a new Trial. (Empty for Firebase)
   */
  public Trial() {

  }

  /**
   * Instantiates a new Trial with an experiment.
   *
   * @param experimenterId the experimenter id
   */
  public Trial(String experimenterId) {
    this.experimenterId = experimenterId;
    this.timestamp = Calendar.getInstance().getTime();
    this.location = new Geolocation(0.0, 0.0);
  }

  /**
   * Instantiates a new Trial with an experiment and geolocation
   *
   * @param experimenterId the experimenter id
   * @param location       the location of the trial
   */
  public Trial(String experimenterId, Geolocation location) {
    this.experimenterId = experimenterId;
    this.timestamp = Calendar.getInstance().getTime();
    this.location = location;
  }

  /**
   * Get experimenter who did this trial.
   *
   * @return Experimenter's ID in Firebase.
   */
  public String getExperimenterId() {
    return experimenterId;
  }

  /**
   * Set experimenter who did this trial. SHOULD ONLY BE USED FOR FIREBASE.
   *
   * @param experimenterId Experimenter's ID.
   */
  public void setExperimenterId(String experimenterId) {
    this.experimenterId = experimenterId;
  }

  /**
   * Get the Date at which this assignment was performed.
   *
   * @return Date object with the recorded time.
   */
  public Date getTimestamp() {
    return timestamp;
  }

  /**
   * Set date when this trial was done. SHOULD ONLY BE USED FOR FIREBASE.
   *
   * @param timestamp Date object with the recorded time.
   */
  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  /**
   * Get location when this trial was done.
   *
   * @return Geolocation object describing the location.
   */
  public Geolocation getLocation() {
    return location;
  }

  /**
   * Set location where this trial was done.
   *
   * @param location Location of trial to be recorded.
   */
  public void setLocation(Geolocation location) {
    this.location = location;
  }
}
