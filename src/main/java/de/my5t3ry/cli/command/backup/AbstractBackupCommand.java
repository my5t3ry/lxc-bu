package de.my5t3ry.cli.command.backup;

import de.my5t3ry.backup.Backup;
import de.my5t3ry.backup.BackupInterval;
import de.my5t3ry.backup.BackupRepository;
import de.my5t3ry.backup.BackupService;
import de.my5t3ry.cli.command.AbstractCommand;
import de.my5t3ry.cli.ui.print.PrintService;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/** User: my5t3ry Date: 5/26/20 12:07 PM */
public abstract class AbstractBackupCommand extends AbstractCommand {
  protected abstract BackupService getBackupService();

  protected abstract BackupRepository getBackupRepository();

  protected abstract PrintService getPrintService();

  protected boolean isIntervalArgumentValid(String intervalArgument) {
    if (BackupInterval.isValid((intervalArgument.toUpperCase()))) {
      return true;
    }
    getPrintService()
        .printError(
            "backup interval ['"
                + intervalArgument
                + "'] is no member of ['"
                + BackupInterval.values.keySet().stream().collect(Collectors.joining(","))
                + "'] ");
    return false;
  }
 protected boolean isCommandValid(List<String> argumentList) {
    Backup result = getBackupByArgument(argumentList);
    if (Objects.nonNull(result)) {
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
