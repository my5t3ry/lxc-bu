package de.my5t3ry.command.backup;

import de.my5t3ry.command.AbstractCommand;
import de.my5t3ry.print.PrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/** User: my5t3ry Date: 5/4/20 9:58 PM */
@Component
public class BackupCommand extends AbstractCommand {
  @Autowired private PrintService printService;

  private List<AbstractBackupCommand> abstractBackupCommands;

  @Autowired private AddCommand addCommand;
  @Autowired private ListCommand listCommand;
  @Autowired private Environment env;

  public void init() {
    setInfo(env.getProperty("command.backup"), "backup commands");
    abstractBackupCommands = Arrays.asList(addCommand, listCommand);
    abstractBackupCommands.forEach(curCommand -> curCommand.init());
  }

  @Override
  public void execute(String command) {
    for (AbstractBackupCommand curCommand : abstractBackupCommands) {
      if (curCommand.executesCommand(stripParentCommand(command))) {
        curCommand.execute(stripParentCommand(command));
        return;
      }
    }
    printService.printCommands(abstractBackupCommands, "backup");
  }
}
