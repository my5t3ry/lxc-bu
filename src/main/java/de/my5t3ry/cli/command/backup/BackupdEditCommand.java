package de.my5t3ry.cli.command.backup;

import de.my5t3ry.backup.Backup;
import de.my5t3ry.backup.BackupInterval;
import de.my5t3ry.backup.BackupRepository;
import de.my5t3ry.backup.BackupService;
import de.my5t3ry.cli.ui.print.PrintService;
import de.my5t3ry.lxc.LxcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/** User: my5t3ry Date: 5/4/20 9:58 PM */
@Component
public class BackupdEditCommand extends AbstractBackupCommand {
  @Autowired private PrintService printService;
  @Autowired private LxcService lxcService;
  @Autowired private BackupService backupService;
  @Autowired private BackupRepository backupRepository;
  @Autowired private Environment env;

  public void init() {
    setInfo(env.getProperty("command.edit"), "edit backup");
  }

  @Override
  public void execute(String command) {
    printService.startSpinner();
    final List<String> argumentList = Arrays.asList(stripParentCommand(command).split(" "));
    if (argumentList.size() != 4) {
      printService.printInfo(
          "wrong argument count. add command requires 4 arguments: container, interval, keep number of snapshots");
      printService.printInfo(
          "['edit id|name [<remote>:]<source>[/<snapshot>] <interval(DAILY,WEEKLY)> <keep-snapshots(int)']");
    } else {
      if (valid(argumentList)) {
        Backup backup = getBackupByArgument(argumentList);
        backupService.editBackup(backup, argumentList);
        if (backup.getExistingSnaphots() > backup.getKeepSnapshots()) {
          printService.printWarning(
              "the number of existing snapshots is bigger than the configured amount of snapshots to keep,\n"
                  + "old snapshots will be deleted on the next scheduled backup run.");
        }
      }
      printService.stopSpinner();
    }
  }

  private boolean valid(List<String> args) {
    try {
      final Backup backupByArgument = getBackupByArgument(args);
      if (Objects.isNull(backupByArgument)) {
        printService.printError(
            "can not find backup for [" + args.get(0) + "] id or name required");
        return false;
      }
      if (!isIntervalArgumentValid(args)) {
        return false;
      }
      Integer.valueOf(args.get(3));
      lxcService.executeCmd("info", args.get(1));
    } catch (NumberFormatException e) {
      printService.printError("keep snapshots amount must be integer not ['" + args.get(3) + "'] ");
      return false;
    } catch (IllegalArgumentException e) {
      printService.printError(
          "backup interval ['"
              + args.get(2)
              + "'] is no member of ['"
              + BackupInterval.values.keySet().stream().collect(Collectors.joining(","))
              + "'] ");
      return false;
    } catch (Exception e) {
      printService.print(e.getMessage(), PrintService.red);
      return false;
    }
    return true;
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
