package de.my5t3ry.cli.command.backup;

import de.my5t3ry.backup.BackupRepository;
import de.my5t3ry.backup.BackupService;
import de.my5t3ry.cli.ui.print.PrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/** User: my5t3ry Date: 5/4/20 9:58 PM */
@Component
public class BackupDeleteCommand extends AbstractBackupCommand {
  @Autowired private PrintService printService;
  @Autowired private BackupService backupService;
  @Autowired private BackupRepository backupRepository;

  @Autowired private Environment env;

  public void init() {
    setInfo(env.getProperty("command.delete"), "create snapshot");
  }

  @Override
  public void execute(String command) {
    final List<String> argumentList = Arrays.asList(stripParentCommand(command).split(" "));
    if (argumentList.size() != 1) {
      printService.printInfo("wrong argument count. add command requires 1 argument: id||name");
      printService.printInfo("['create/c id,name]");
    } else {
      if (isBackupArgumentValid(argumentList.get(0))) {
        backupRepository.delete(getBackupByArgument(argumentList.get(0)));
        printService.printInfo(String.format("deleted backup ['%s']", argumentList.get(0)));
      } else {
        printService.printError(
            String.format("can not find backup for [%s] id or name required", argumentList.get(0)));
      }
    }
  }

  @Override
  protected BackupService getBackupService() {
    return this.backupService;
  }

  @Override
  protected BackupRepository getBackupRepository() {
    return this.backupRepository;
  }

  @Override
  protected PrintService getPrintService() {
    return this.printService;
  }
}
