package de.my5t3ry.cli.command.backup;

import de.my5t3ry.cli.ui.print.PrintService;
import de.my5t3ry.backup.Backup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/** User: my5t3ry Date: 5/4/20 9:58 PM */
@Component
public class BackupCreateCommand extends AbstractBackupCommand {
  @Autowired private PrintService printService;
  @Autowired private Environment env;

  public void init() {
    setInfo(env.getProperty("command.create"), "create snapshot");
  }

  @Override
  public void execute(String command) {
    final List<String> argumentList = Arrays.asList(stripParentCommand(command).split(" "));
    if (argumentList.size() != 1) {
      printService.printInfo("wrong argument count. add command requires 1 argument: id||name");
      printService.printInfo("['create/c id,name]");
    } else {
      if (!valid(argumentList)) {
        printService.printError(
            "can not find backup for [" + argumentList.get(0) + "] id or name required");
      } else {
        backupService.createBackup(getBackupByArgument(argumentList));
      }
    }
  }

  private boolean valid(List<String> argumentList) {
    Backup result = getBackupByArgument(argumentList);
    if (Objects.nonNull(result)) {
      return true;
    }
    return false;
  }
}
