package de.my5t3ry.cli.command.backup;

import de.my5t3ry.cli.command.AbstractCommand;
import de.my5t3ry.cli.ui.print.PrintService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/** User: my5t3ry Date: 5/4/20 9:58 PM */
@Component
public class BackupSnapshotCommand extends AbstractCommand {
  @Autowired private PrintService printService;

  private List<AbstractCommand> abstractBackupCommands;

  @Autowired private BackupSnapshotDeleteCommand backupSnapshotDeleteCommand;
  @Autowired private Environment env;

  public void init() {
    setInfo(env.getProperty("command.snapshot"), "snapshot commands");
    abstractBackupCommands = Arrays.asList(backupSnapshotDeleteCommand);
    abstractBackupCommands.forEach(curCommand -> curCommand.init());
  }

  @Override
  public void execute(String command) {
    final String nestedCommand = stripParentCommand(command);
    if (StringUtils.isBlank(nestedCommand)) {
      printService.printCommands(abstractBackupCommands, "snapshot");
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
