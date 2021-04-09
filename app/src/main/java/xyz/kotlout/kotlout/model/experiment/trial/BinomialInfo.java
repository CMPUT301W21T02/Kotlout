package xyz.kotlout.kotlout.model.experiment.trial;

/**
 * A class that stores the information stored in binomial trials so that they can be more easily displayed
 */
public class BinomialInfo {

  private final String date;
  private float success;
  private float failure;

  /**
   * The constructor is created with the Trial's date
   *
   * @param date The date of the trial
   */
  public BinomialInfo(String date) {
    this.date = date;
    this.success = 0;
    this.failure = 0;
  }

  /**
   * Increases the successes by 1
   */
  public void incrementSuccess() {
    this.success++;
  }

  /**
   * Increases the failures by 1
   */
  public void incrementFailure() {
    this.failure++;
  }

  /**
   * Calculates the percentage of successes
   *
   * @return a float representing the success percentage
   */
  public float successProportion() {
    return (this.success / (this.success + this.failure)) * 100;
  }

  /**
   * Gets the date of the trial
   *
   * @return A string of the date
   */
  public String getDate() {
    return date;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof BinomialInfo) {
      BinomialInfo b = (BinomialInfo) obj;
      return this.date.equals(b.getDate());
    } else {
      return false;
    }
  }
}
