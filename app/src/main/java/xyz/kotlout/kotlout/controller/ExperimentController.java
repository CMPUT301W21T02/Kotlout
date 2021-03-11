package xyz.kotlout.kotlout.controller;

import xyz.kotlout.kotlout.model.ExperimentType;
import xyz.kotlout.kotlout.model.experiment.BinomialExperiment;
import xyz.kotlout.kotlout.model.experiment.CountExperiment;
import xyz.kotlout.kotlout.model.experiment.Experiment;
import xyz.kotlout.kotlout.model.experiment.MeasurementExperiment;
import xyz.kotlout.kotlout.model.experiment.NonNegativeExperiment;

/**
 * Controller for handling interactions with Experiment objects.
 */
public class ExperimentController {

  Experiment experiment;

  /**
   * Default constructor.
   */
  public ExperimentController() {
  }

  /**
   * Initializes the controller and sets the context to an existing Experiment object.
   *
   * @param experiment An instance of Experiment.
   */
  public ExperimentController(Experiment experiment) {
    this.experiment = experiment;
  }

  /**
   * Sets the experiment context with the basic fields needed for a new Experiment.
   *
   * @param description The experiment description.
   * @param region      The region where the experiment is conducted.
   * @param minTrials   The minimum number of trials required for the experiment.
   * @param type        The type of experiment.
   */
  public void setExperimentContext(String description, String region, int minTrials,
      ExperimentType type) {
    switch (type) {

      case binomial:
        experiment = new BinomialExperiment(description, region, minTrials);
        break;
      case nonNegativeInteger:
        experiment = new NonNegativeExperiment(description, region, minTrials);
        break;
      case count:
        experiment = new CountExperiment(description, region, minTrials);
        break;
      case measurement:
        experiment = new MeasurementExperiment(description, region, minTrials);
        break;
    }
  }

  /**
   * Gets the current Experiment object owned by the controller.
   *
   * @return An Experiment object.
   */
  public Experiment getExperimentContext() {
    return experiment;
  }
}

