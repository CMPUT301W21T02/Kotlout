package xyz.kotlout.kotlout.model.experiment.trial;

import java.util.Date;
import xyz.kotlout.kotlout.model.experiment.Experiment;
import xyz.kotlout.kotlout.model.geolocation.Geolocation;
import xyz.kotlout.kotlout.model.user.User;

public abstract class Trial {

  private int trialId;
  private User experimenter;
  private Experiment experiment;
  private Date timestamp;
  private Geolocation location;
  private boolean isUploaded;
}
