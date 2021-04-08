package xyz.kotlout.kotlout.model.experiment.trial;

import androidx.annotation.Nullable;

public class BinomialInfo {

  private String date;
  private float success;
  private float failure;

  public BinomialInfo(String date) {
    this.date = date;
    this.success = 0;
    this.failure = 0;
  }

  public void incrementSuccess() {
    this.success++;
  }

  public void incrementFailure() {
    this.failure++;
  }

  public float successProportion() {
    return (float) (this.success / (this.success + this.failure)) * 100;
  }

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
