package de.my5t3ry.cli.command.backup;

import de.my5t3ry.cli.command.AbstractCommand;
import de.my5t3ry.cli.ui.print.PrintService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * User: my5t3ry Date: 5/4/20 9:58 PM
 */
@Component
public class BackupCommand extends AbstractCommand {
  @Autowired
  private PrintService printService;

  private List<AbstractCommand> abstractBackupCommands;

  @Autowired
  private AddCommand addCommand;
  @Autowired
  private BackupDeleteCommand deleteCommand;
  @Autowired
  private BackupListCommand backupListCommand;
  @Autowired
  private BackupCreateCommand backupCreateCommand;
  @Autowired
  private Environment env;

  public void init() {
    setInfo(env.getProperty("command.backup"), "backup commands");
    abstractBackupCommands =
            Arrays.asList(addCommand, backupListCommand, backupCreateCommand, deleteCommand);
    abstractBackupCommands.forEach(curCommand -> curCommand.init());
  }

  @Override
  public void execute(String command) {
    final String nestedCommand = stripParentCommand(command);
    if (StringUtils.isBlank(nestedCommand)) {
      printService.printCommands(abstractBackupCommands, "backup");
      return;
    }
    for (AbstractCommand curCommand : abstractBackupCommands) {
      if (curCommand.executesCommand(nestedCommand)) {
        curCommand.execute(nestedCommand);
        return;
      }
    }
    printService.printCommands(abstractBackupCommands, "backup");
  }
}
