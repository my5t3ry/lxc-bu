package de.my5t3ry.cli.command.backup;

import de.my5t3ry.backup.Backup;
import de.my5t3ry.backup.BackupInterval;
import de.my5t3ry.backup.BackupRepository;
import de.my5t3ry.backup.BackupService;
import de.my5t3ry.cli.command.AbstractCommand;
import de.my5t3ry.cli.ui.print.PrintService;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/** User: my5t3ry Date: 5/26/20 12:07 PM */
public abstract class AbstractBackupCommand extends AbstractCommand {
  protected abstract BackupService getBackupService();
  protected abstract BackupRepository getBackupRepository();
  protected abstract PrintService getPrintService();



  protected boolean isIntervalArgumentValid(List<String> args) {
    if (!BackupInterval.isValide((args.get(1).toUpperCase()))) {
      getPrintService().print(
              "backup interval ['"
                      + args.get(1)
                      + "'] is no member of ['"
                      + BackupInterval.values.keySet().stream().collect(Collectors.joining(","))
                      + "'] ",
              PrintService.red);
      return true;
    }
    return false;
  }

  protected Backup getBackupByArgument(List<String> argumentList) {
    List<Backup> result = getBackupService().findByContainer(argumentList.get(0));
    if (result.size() == 1) {
      return result.get(0);
    }
    if (!StringUtils.isNumeric(argumentList.get(0))) {
      return null;
    }
    return getBackupRepository().findById(Long.valueOf(argumentList.get(0))).orElse(null);
  }
}
