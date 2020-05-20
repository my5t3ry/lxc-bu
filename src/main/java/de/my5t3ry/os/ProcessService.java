package de.my5t3ry.os;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/** User: my5t3ry Date: 5/19/20 7:09 AM */
@Service
public class ProcessService {
  public void runCmdSilent(final String... cmd) {
    try {
      ProcessBuilder builder = new ProcessBuilder();
      builder.command(cmd);
      Process process = builder.start();
      int exitCode = process.waitFor();
      assert exitCode == 0;
    } catch (IOException | InterruptedException e) {
      throw new IllegalStateException("command failed with message ['" + e.getMessage() + "']");
    }
  }

  public String runCmd(final String... cmd) throws IOException, InterruptedException {
    final StringBuilder stringBuilder = new StringBuilder();
    ProcessBuilder builder = new ProcessBuilder();
    builder.command(cmd);
    Process process = builder.start();
    StreamGobbler streamGobbler =
        new StreamGobbler(process.getInputStream(), s -> stringBuilder.append(s).append("\n"));
    Executors.newSingleThreadExecutor().submit(streamGobbler);
    int exitCode = process.waitFor();
    if (exitCode != 0) {
      final StringBuilder sb = new StringBuilder();
      InputStream errorStream = process.getErrorStream();
      int c = 0;
      while ((c = errorStream.read()) != -1) {
        sb.append((char) c);
      }
      throw new IllegalStateException(
          "failed to run cmd ['"
              + Arrays.asList(cmd).stream().collect(Collectors.joining(" "))
              + "'] with msg ['"
              + sb.toString().replaceAll("\n", "")
              + "']");
    } else {
      return stringBuilder.toString();
    }
  }
}