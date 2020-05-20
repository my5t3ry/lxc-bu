package de.my53ry.shell;

import org.jline.terminal.Terminal;

/** User: my5t3ry Date: 5/19/20 8:13 AM */
public class ProgressCounter {
  private static final String CUU = "\u001B[A";

  private final Terminal terminal;
  private char[] spinner = {'|', '/', '-', '\\'};

  private int spinCounter = 0;
  private boolean started;

  public ProgressCounter(Terminal terminal) {
    this(terminal, null);
  }

  public ProgressCounter(Terminal terminal, char[] spinner) {
    this.terminal = terminal;

    if (spinner != null) {
      this.spinner = spinner;
    }
  }

  public void display() {
    if (!started) {
      terminal.writer().println();
      started = true;
    }
    terminal.writer().println(CUU + "\r" + getSpinnerChar());
  }

  public void reset() {
    spinCounter = 0;
    started = false;
  }

  private char getSpinnerChar() {
    char spinChar = spinner[spinCounter];
    spinCounter++;
    if (spinCounter == spinner.length) {
      spinCounter = 0;
    }
    return spinChar;
  }

  // --- set / get methods ---------------------------------------------------

  public char[] getSpinner() {
    return spinner;
  }

  public void setSpinner(char[] spinner) {
    this.spinner = spinner;
  }
}
