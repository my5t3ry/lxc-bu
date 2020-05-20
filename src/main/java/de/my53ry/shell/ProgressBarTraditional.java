package de.my53ry.shell;

/** User: my5t3ry Date: 5/19/20 8:27 AM */
class ProgressBarTraditional extends Thread {
  boolean showProgress = true;

  public void run() {
    String anim = "=====================";
    int x = 0;
    while (showProgress) {
      System.out.print("\r Processing " + anim.substring(0, x++ % anim.length()) + " ");
      try {
        Thread.sleep(100);
      } catch (Exception e) {
      }
    }
    System.out.print('\u000C');
  }
}
