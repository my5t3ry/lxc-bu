package de.my5t3ry.cli.command.backup;

import de.my5t3ry.backup.Backup;
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
public class BackupSnapshotRestoreCommand extends AbstractBackupCommand {
  @Autowired private PrintService printService;
  @Autowired private BackupService backupService;
  @Autowired private BackupRepository backupRepository;

  @Autowired private Environment env;

  public void init() {
    setInfo(env.getProperty("command.restore"), "restore snapshot");
  }

  @Override
  public void execute(String command) {
    final List<String> argumentList = Arrays.asList(stripParentCommand(command).split(" "));
    if (argumentList.size() != 2) {
      printService.printInfo(
          "wrong argument count. add command requires 2 arguments: backup(id,name)] snapshot(id,name)");
      printService.printInfo("['delete/d backup(id,name)] snapshot(id,name )");
    } else {
      if (isBackupArgumentValid(argumentList.get(0))) {
        final Backup backup = getBackupByArgument(argumentList.get(0));
        if (backup.hasSnapshot(argumentList.get(1))) {
          try {
            backupService.restoreSnapshot(
                backup, backup.getSnapShotFromArgument(argumentList.get(1)));
            printService.printInfo(String.format("restored backup ['%s']", argumentList.get(0)));
          } catch (VerifyError error) {
            printService.printError(error.getMessage());
          }
        } else {
          printService.printInfo(
              String.format(
                  " backup ['%s'] has no snapshot ['%s']",
                  argumentList.get(0), argumentList.get(1)));
        }
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
