package xyz.kotlout.kotlout.model.experiment.trial;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import xyz.kotlout.kotlout.model.experiment.Experiment;
import xyz.kotlout.kotlout.model.geolocation.Geolocation;
import xyz.kotlout.kotlout.model.user.User;

public abstract class Trial implements Serializable {

  private int trialId;
  private String experimenterId;
  private Date timestamp;
  private Geolocation location;

  public Trial() {

  }

  public Trial(String experimenterId) {
    this.experimenterId = experimenterId;
    this.timestamp = Calendar.getInstance().getTime();
  }

  public String getExperimenter() {
    return experimenterId;
  }

}
