package de.my5t3ry.cli.command.container;

import de.my5t3ry.cli.command.AbstractCommand;
import de.my5t3ry.cli.ui.print.PrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/** User: my5t3ry Date: 5/4/20 9:58 PM */
@Component
public class ContainerCommand extends AbstractCommand {
  @Autowired private PrintService printService;

  private List<AbstractCommand> commands;

  @Autowired private ContainerListCommand containerListCommand;
  @Autowired private Environment env;

  public void init() {
    setInfo(env.getProperty("command.container"), "container commands");
    commands = Arrays.asList(containerListCommand);
    commands.forEach(curCommand -> curCommand.init());
  }

  @Override
  public void execute(String command) {
    for (AbstractCommand curCommand : commands) {
      if (curCommand.executesCommand(stripParentCommand(command))) {
        curCommand.execute(stripParentCommand(command));
        return;
      }
    }
    printService.printCommands(commands, "backup");
  }
}
