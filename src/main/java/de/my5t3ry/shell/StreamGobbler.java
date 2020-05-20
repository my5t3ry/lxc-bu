package de.my5t3ry.shell;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

/** User: my5t3ry Date: 5/19/20 7:10 AM */
public class StreamGobbler implements Runnable {
  private final InputStream inputStream;
  private final Consumer<String> consumer;

  public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
    this.inputStream = inputStream;
    this.consumer = consumer;
  }

  @Override
  public void run() {
    new BufferedReader(new InputStreamReader(inputStream)).lines().forEach(consumer);
  }
}
