package xyz.kotlout.kotlout.controller;

import androidx.annotation.NonNull;

public enum ExperimentGroup {
  OPEN("Open Experiments"),
  CLOSED("Closed Experiments");

  private final String text;

  ExperimentGroup(String text) {
    this.text = text;
  }

  public static ExperimentGroup getByOrder(int id) {
    if (id == 0) {
      return OPEN;
    }

    return CLOSED;
  }

  @NonNull
  @Override
  public String toString() {
    return text;
  }
}
