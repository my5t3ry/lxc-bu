package de.my5t3ry.cli.command.container;

import de.my5t3ry.cli.command.AbstractCommand;
import de.my5t3ry.cli.ui.print.PrintService;
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
  @Autowired private LxcService lxcService;
  @Autowired private Environment env;

  public void init() {
    setInfo(env.getProperty("command.list"), "list containers");
  }

  @Override
  public void execute(String command) {
    if (!isCommandValid(command)) {
      printService.printInfo(
          "wrong argument count. container lost command requires <= 1 argument: host (local:,some-remote-host:)");
    } else {
      try {
        final String processResultMsg = lxcService.executeCmd("list", stripParentCommand(command));
        if (StringUtils.isBlank(processResultMsg)) {
          printService.printInfo(
              String.format("host ['%s'] empty or not found", stripParentCommand(command)));
        } else {
          printService.printInfo(processResultMsg);
        }
      } catch (IOException | InterruptedException e) {
        printService.printError(
                String.format("container list command failed with msg ['%s']", e.getMessage()));
      }
    }
  }

  private boolean isCommandValid(String command) {
    return stripParentCommand(command).split(" ").length == 1;
  }
}
