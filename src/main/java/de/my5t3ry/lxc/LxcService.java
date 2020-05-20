package de.my5t3ry.lxc;

import de.my5t3ry.cli.ui.print.PrintService;
import de.my5t3ry.os.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** User: my5t3ry Date: 5/19/20 7:14 AM */
@Service
public class LxcService {
  @Autowired private ProcessService processService;
  @Autowired private PrintService printService;

  public void validateLxc() {
    try {
      final String result = processService.runCmd("lxc", "info");
      if (result.toLowerCase().contains("error")) {
        throw new IllegalStateException("lxc validation failed with message \\n['" + result + "']");
      }
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  public String executeCmd(String... cmd) throws IOException, InterruptedException {
    final List<String> lxcCmd = new ArrayList<>();
    lxcCmd.add("lxc");
    lxcCmd.addAll(Arrays.asList(cmd));
    return processService.runCmd(lxcCmd.toArray(new String[lxcCmd.size()]));
  }
}
