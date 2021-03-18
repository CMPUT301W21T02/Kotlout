package xyz.kotlout.kotlout.controller;

import androidx.annotation.NonNull;

public enum MyExperimentGroup {
  OPEN("Open Experiments"),
  CLOSED("Closed Experiments");

  private final String text;

  MyExperimentGroup(String text) {
    this.text = text;
  }

  public static MyExperimentGroup getByOrder(int id) {
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
