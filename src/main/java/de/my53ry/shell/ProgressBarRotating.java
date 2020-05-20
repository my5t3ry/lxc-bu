package de.my53ry.shell;

/** User: my5t3ry Date: 5/19/20 8:27 AM */
class ProgressBarRotating implements Runnable {
  private boolean showProgress = true;

  public void run() {
    String anim = "|/-\\";
    int x = 0;
    while (this.showProgress) {
      System.out.print("\r" + anim.charAt(x++ % anim.length()));
      try {
        Thread.sleep(100);
      } catch (Exception e) {
      }
    }
  }

  public void stop() {
    this.showProgress = false;
  }
}
