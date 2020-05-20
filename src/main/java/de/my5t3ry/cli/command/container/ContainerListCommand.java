package de.my5t3ry.cli.command.container;

import de.my5t3ry.cli.command.AbstractCommand;
import de.my5t3ry.cli.ui.print.PrintService;
import de.my5t3ry.domain.backup.BackupRepository;
import de.my5t3ry.lxc.LxcService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;

/** User: my5t3ry Date: 5/4/20 9:58 PM */
@Component
public class ContainerListCommand extends AbstractCommand {
  @Autowired private PrintService printService;

  @Autowired private BackupRepository backupRepository;
  @Autowired private LxcService lxcService;
  @Autowired private Environment env;

  public void init() {
    setInfo(env.getProperty("command.list"), "list containers");
  }

  @Override
  public void execute(String command) {
    if (!valid(command)) {
      printService.print(
          "wrong argument count. container lost command requires <= 1 argument: host (local:,some-remote-host:)");
    } else {
      try {
        final String processResultMsg = lxcService.executeCmd("list", stripParentCommand(command));
        if (StringUtils.isBlank(processResultMsg)) {
          printService.getInfoMessage(
              "host ['" + stripParentCommand(command) + "'] empty or not found");
        } else {
          printService.print(processResultMsg);
        }
      } catch (IOException | InterruptedException e) {
        printService.printError(
            "container list command failed with msg ['" + e.getMessage() + "']");
      }
    }
  }

  private boolean valid(String command) {
    return stripParentCommand(command).split(" ").length == 1;
  }
}
