package de.my53ry.command.backup;

import de.my53ry.backup.BackupJobService;
import de.my53ry.backup.BackupService;
import de.my53ry.command.AbstractCommand;
import de.my53ry.lxc.LxcService;
import de.my53ry.print.PrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/** User: my5t3ry Date: 5/4/20 9:58 PM */
@Component
public class BackupCommand extends AbstractCommand {
  @Autowired private BackupService backupService;
  @Autowired private BackupJobService backupJobService;
  @Autowired private PrintService printService;

  @Autowired private LxcService lxcService;
  @Autowired private List<AbstractBackupCommand> abstractBackupCommands;

  @Value("${command.backup}")
  private String command;

  protected BackupCommand() {
    super();
  }

  @PostConstruct
  public void init() {
    setInfo(command, "backup commands");
  }

  @Override
  public void execute(String command) {
    for (AbstractBackupCommand curCommand : abstractBackupCommands) {
      if (curCommand.executesCommand(command)) {
        curCommand.execute(command);
        break;
      }
    }
    printService.printCommands(abstractBackupCommands, "backup");
  }
}
