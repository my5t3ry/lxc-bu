package de.my5t3ry.shell;

/** User: my5t3ry Date: 5/19/20 8:27 AM */
public class ConsoleProgressBar {
  private ProgressBarRotating task;

  public void start() {
    task = new ProgressBarRotating();
    Thread t = new Thread(task);
    t.start();
  }

  public void stop() {
    task.stop();
  }
}
