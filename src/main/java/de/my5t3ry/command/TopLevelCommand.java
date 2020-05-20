package de.my5t3ry.command;

import de.my5t3ry.command.backup.BackupCommand;
import de.my5t3ry.print.PrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** User: my5t3ry Date: 5/4/20 9:52 PM */
@Component
public class TopLevelCommand {
  @Autowired(required = true) public BackupCommand backupCommand;
  @Autowired public HelpCommand helpCommand;
  @Autowired private PrintService printService;
  private List<AbstractCommand> commands = new ArrayList<>();

  @PostConstruct
  public void init() {
    helpCommand.execute("h");
    commands = Arrays.asList(helpCommand, backupCommand);
  }

  public void execute(String command) {
    for (AbstractCommand curCommand : commands) {
      if (curCommand.executesCommand(command)) {
        curCommand.execute(command);
        break;
      }
    }
    printService.printCommands(commands, "top-level");
  }

  public List<AbstractCommand> getCommands() {
    return commands;
  }
}
