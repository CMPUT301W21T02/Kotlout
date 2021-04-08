package xyz.kotlout.kotlout.model.experiment;

import java.util.ArrayList;
import java.util.List;
import xyz.kotlout.kotlout.model.ExperimentType;
import xyz.kotlout.kotlout.model.experiment.trial.MeasurementTrial;
import xyz.kotlout.kotlout.model.experiment.trial.Trial;

/**
 * A specialization of the Experiment class for experiments involving floating point measurements.
 */
public class MeasurementExperiment extends Experiment {

  /**
   * Type of experiment, can't be static due to Firestore model generation
   */
  public ExperimentType type = ExperimentType.MEASUREMENT;

  private List<MeasurementTrial> trials = new ArrayList<>();

  public MeasurementExperiment() {
  }

  /**
   * Creates a new MeasurementExperiment with basic fields passed on to the base Experiment constructor.
   */
  public MeasurementExperiment(String description, String region, int minimumTrials, boolean geolocationRequired) {
    super(description, region, minimumTrials, geolocationRequired);
  }

  @Override
  ExperimentType getExperimentType() {
    return type;
  }

  @Override
  public void addTrial(Trial trial) {
    trials.add((MeasurementTrial) trial);
  }

  /**
   * Get list of trials done for this experiment
   *
   * @return ArrayList of Trials for this experiment
   */
  public List<MeasurementTrial> getTrials() {
    return trials;
  }
}
