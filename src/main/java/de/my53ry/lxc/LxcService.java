package de.my53ry.lxc;

import de.my53ry.shell.CmdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** User: my5t3ry Date: 5/19/20 7:14 AM */
@Service
public class LxcService {
  @Autowired private CmdService cmdService;

  public void validateLxc() {
    cmdService.runCmdSilent("lxc", "info");
  }

  public void executeCmd(String... cmd) throws IOException, InterruptedException {
    final List<String> lxcCmd = new ArrayList<>();
    lxcCmd.add("lxc");
    lxcCmd.addAll(Arrays.asList(cmd));
    cmdService.runCmd(lxcCmd.toArray(new String[lxcCmd.size()]));
  }
}
