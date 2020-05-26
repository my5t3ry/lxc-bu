package de.my5t3ry.cli.command.backup;

import de.my5t3ry.cli.command.AbstractCommand;
import de.my5t3ry.backup.Backup;
import de.my5t3ry.backup.BackupRepository;
import de.my5t3ry.backup.BackupService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/** User: my5t3ry Date: 5/26/20 12:07 PM */
public abstract class AbstractBackupCommand extends AbstractCommand {
  @Autowired protected BackupService backupService;
  @Autowired protected BackupRepository backupRepository;

  protected Backup getBackupByArgument(List<String> argumentList) {
    List<Backup> result = backupService.findByContainer(argumentList.get(0));
    if (result.size() == 1) {
      return result.get(0);
    }
    if (!StringUtils.isNumeric(argumentList.get(0))) {
      return null;
    }
    return backupRepository.findById(Long.valueOf(argumentList.get(0))).orElse(null);
  }
}
