package de.my5t3ry.command.backup;

import de.my5t3ry.backup.BackupJobService;
import de.my5t3ry.backup.BackupService;
import de.my5t3ry.command.AbstractCommand;
import de.my5t3ry.lxc.LxcService;
import de.my5t3ry.print.PrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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

  @Autowired private Environment env;

  public BackupCommand() {
    super();
  }

  @PostConstruct
  public void init() {
    setInfo(env.getProperty("command.backup"), "backup commands");
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
