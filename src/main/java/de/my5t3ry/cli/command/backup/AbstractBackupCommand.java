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
            String.format(
                "backup interval ['%s'] is no member of ['%s'] ",
                intervalArgument,
                BackupInterval.values.keySet().stream().collect(Collectors.joining(","))));
    return false;
  }

  protected boolean isBackupArgumentValid(String backupArgument) {
    Backup result = getBackupByArgument(backupArgument);
    return Objects.nonNull(result);
  }

  protected Backup getBackupByArgument(String backupArgument) {
    List<Backup> result = getBackupService().findByContainer(backupArgument);
    if (result.size() == 1) {
      return result.get(0);
    }
    if (!StringUtils.isNumeric(backupArgument)) {
      return null;
    }
    return getBackupRepository().findById(Long.valueOf(backupArgument)).orElse(null);
  }
}
