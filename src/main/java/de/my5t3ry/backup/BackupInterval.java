package de.my5t3ry.backup;

/** User: my5t3ry Date: 5/19/20 2:16 PM */
public enum BackupInterval {
  DAILY(1),
  WEEKLY(7);

  private final int days;

  BackupInterval(int days) {
    this.days = days;
  }

  public int getDays() {
    return days;
  }
}
