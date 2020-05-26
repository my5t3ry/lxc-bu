package de.my5t3ry.cli.command.backup;

import de.my5t3ry.cli.command.AbstractCommand;
import de.my5t3ry.cli.ui.print.PrintService;
import de.my5t3ry.domain.backup.Backup;
import de.my5t3ry.domain.backup.BackupInterval;
import de.my5t3ry.domain.backup.BackupService;
import de.my5t3ry.lxc.LxcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/** User: my5t3ry Date: 5/4/20 9:58 PM */
@Component
public class AddCommand extends AbstractCommand {
  @Autowired private BackupService backupService;
  @Autowired private PrintService printService;

  @Autowired private LxcService lxcService;

  @Autowired private Environment env;

  public void init() {
    setInfo(env.getProperty("command.add"), "add backup");
  }

  @Override
  public void execute(String command) {
    printService.startSpinner();
    final List<String> argumentList = Arrays.asList(command.split(" "));
    if (argumentList.size() != 4) {
      printService.print(
              "wrong argument count. add command requires 3 arguments: container, interval, keep number of snapshots");
      printService.print(
              "['add [<remote>:]<source>[/<snapshot>] <interval(DAILY,WEEKLY)> <keep-snapshots(int)']");
    } else {
      String[] args =
              argumentList.subList(1, argumentList.size()).toArray(new String[argumentList.size() - 1]);
      if (!valid(args)) {
        return;
      } else {
        try {
          final Backup backup;
          backup = backupService.addBackup(args);
          printService.stopSpinner();
          if (backup.getExistingSnaphots() > backup.getScheduledInterval()) {
            printService.printWarning(
                    "the number of existing snapshots is bigger than the configured amount of snapshots to keep,\n"
                            + "old snapshots will be deleted on the next scheduled backup run ");
          }
          printService.print("added backup ['" + backup.toString() + "']");
        } catch (IOException | InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private boolean valid(String[] args) {
    try {
      BackupInterval.isValide((args[1].toUpperCase()));
      Integer.valueOf(args[2]);
      lxcService.executeCmd("info", args[0]);
    } catch (NumberFormatException e) {
      printService.print(
          "keep snaphots amount must be integer not ['" + args[2] + "'] ", PrintService.red);
      return false;
    } catch (IllegalArgumentException e) {
      printService.print(
          "backup interval ['"
              + args[1]
              + "'] is no member of ['"
              + BackupInterval.values.keySet().stream().collect(Collectors.joining(","))
              + "'] ",
          PrintService.red);
      return false;
    } catch (Exception e) {
      printService.print(e.getMessage(), PrintService.red);
      return false;
    }
    return true;
  }
}
