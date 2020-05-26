package de.my5t3ry.cli.command.backup;

import de.my5t3ry.cli.command.AbstractCommand;
import de.my5t3ry.cli.ui.print.PrintService;
import de.my5t3ry.domain.backup.Backup;
import de.my5t3ry.domain.backup.BackupRepository;
import de.my5t3ry.domain.backup.BackupService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * User: my5t3ry Date: 5/4/20 9:58 PM
 */
@Component
public class BackupDeleteCommand extends AbstractCommand {
  @Autowired
  private PrintService printService;

  @Autowired
  private BackupRepository backupRepository;
  @Autowired
  private BackupService backupService;
  @Autowired
  private Environment env;

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
      if (!valid(argumentList)) {
        printService.printError(
                "can not find backup for [" + argumentList.get(0) + "] id or name required");
      } else {
        backupRepository.delete(getBackupByArgument(argumentList));
        printService.print("deleted backup ['" + argumentList.get(0) + "']");
      }
    }
    return;
  }

  private boolean valid(List<String> argumentList) {
    Backup result = getBackupByArgument(argumentList);
    if (Objects.nonNull(result)) {
      return true;
    }
    return false;
  }

  private Backup getBackupByArgument(List<String> argumentList) {
    Backup result = backupRepository.findByContainer(argumentList.get(0));
    if (Objects.nonNull(result)) {
      return result;
    }
    if (!StringUtils.isNumeric(argumentList.get(0))) {
      return null;
    }
    return backupRepository.findById(Long.valueOf(argumentList.get(0))).get();
  }
}
