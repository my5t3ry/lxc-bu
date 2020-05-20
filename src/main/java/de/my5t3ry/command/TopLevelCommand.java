package de.my5t3ry.command;

import de.my5t3ry.command.backup.BackupCommand;
import de.my5t3ry.print.PrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** User: my5t3ry Date: 5/4/20 9:52 PM */
@Controller
public class TopLevelCommand {
  @Autowired public BackupCommand backupCommand;
  @Autowired public HelpCommand helpCommand;
  @Autowired private PrintService printService;
  private List<AbstractCommand> commands = new ArrayList<>();

  public void init() {
    printService.print("test");
    helpCommand.execute("h");
    commands = Arrays.asList(helpCommand, backupCommand);
    commands.forEach(curCommand -> curCommand.init());
  }

  public void execute(String command) {
    for (AbstractCommand curCommand : commands) {
      if (curCommand.executesCommand(command)) {
        curCommand.execute(command);
        return;
      }
    }
    printService.printCommands(commands, "top-level");
  }

  public List<AbstractCommand> getCommands() {
    return commands;
  }
}
