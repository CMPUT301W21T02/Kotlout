package xyz.kotlout.kotlout.model.experiment;

import java.util.Objects;

/**
 * Base class for data points which are needed to create histograms with the MPGraph library
 */
public class HistogramData {
  String result;
  float count;

  /**
   * Creates a new data point
   * @param result The result of the trial (e.g. Success, Failure, some measurement)
   * @param count The amount of that specific result
   */
  public HistogramData(String result, float count) {
    this.result = result;
    this.count = count;
  }

  /**
   * Gets the result of the trial
   * @return A string containing the result of the trial
   */
  public String getResult() {
    return result;
  }

  /**
   * Gets the count of the trial
   * @return an integer representing the count of the trial
   */
  public float getCount() {
    return count;
  }

  public void setCount(float count) {
    this.count = count;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    HistogramData that = (HistogramData) obj;
    return Objects.equals(this.result, that.result);
  }

  @Override
  public int hashCode() {
    return Objects.hash(result);
  }

  /**
   * Merges two histogram data points by combining their count
   * @param other another instance of HistogramData
   * @return A new HistogramData object with the counts of the two HistogramData objects combined, and the same result as both objects
   */
  public HistogramData merge(HistogramData other) {
    assert(this.equals(other));
    return new HistogramData(this.result, this.count + other.count);
  }

}
