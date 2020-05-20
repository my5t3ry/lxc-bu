package de.my5t3ry.command.backup;

import de.my5t3ry.backup.*;
import de.my5t3ry.lxc.LxcService;
import de.my5t3ry.print.PrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/** User: my5t3ry Date: 5/4/20 9:58 PM */
@Component
public class AddCommand extends AbstractBackupCommand {
  @Autowired private BackupService backupService;
  @Autowired private BackupJobService backupJobService;
  @Autowired private PrintService printService;

  @Autowired private LxcService lxcService;

  @Autowired private Environment env;

  public void init() {
    setInfo(env.getProperty("command.add"), "add backup");
  }

  @Override
  public void execute(String command) {
    final List<String> argumentList = Arrays.asList(command.split(" "));
    if (argumentList.size() != 4) {
      printService.print(
          "add command requires 3 arguments: container, interval, keep number of snapshots");
      printService.print(
          "['add [<remote>:]<source>[/<snapshot>] <interval(DAILY,WEEKLY)> <keep-snapshots(int)']");
    } else {
      String[] args =
          argumentList.subList(1, argumentList.size()).toArray(new String[argumentList.size() - 1]);
      if (!valid(args)) {
        return;
      } else {
        printService.print(Arrays.asList(args).stream().collect(Collectors.joining(", ")));
        final Backup backup = backupService.addBackup(args);
        final BackupJob backupJob = backupJobService.scheduleBackupJob(backup);
        printService.print("added backup ['" + backup.toString() + "']");
        printService.print("scheduled backup job for  ['" + backupJob.getScheduledTime() + "']");
      }
    }
  }

  private boolean valid(String[] args) {
    try {
      BackupInterval.valueOf(args[1].toUpperCase());
      Integer.valueOf(args[2]);
      lxcService.executeCmd("info", args[0]);
    } catch (NumberFormatException e) {
      printService.printWithColor(
          "keep snaphots amount must be integer not ['" + args[2] + "'] ", "RED");
      return false;
    } catch (IllegalArgumentException e) {
      printService.printWithColor(
          "backup interval ['" + args[1] + "'] is no member of ['DAILY','WEEKLY'] ", "RED");
      return false;
    } catch (Exception e) {
      printService.printWithColor(e.getMessage(), "RED");
      return false;
    }
    return true;
  }
}
