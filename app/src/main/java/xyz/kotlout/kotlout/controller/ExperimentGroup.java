package xyz.kotlout.kotlout.controller;

import androidx.annotation.NonNull;

/**
 * Enum to signify open and closed experiments (published/unpublished)
 */
public enum ExperimentGroup {
  OPEN("Open Experiments"),
  CLOSED("Closed Experiments");

  private final String text;

  /**
   * Hidden constructor used to store the string representation as part of the enum.
   *
   * @param text String that the enum should represent.
   */
  ExperimentGroup(String text) {
    this.text = text;
  }

  /**
   * Used to get enum entries by order.
   *
   * @param id order of the enum entry.
   * @return enum entry.
   */
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
