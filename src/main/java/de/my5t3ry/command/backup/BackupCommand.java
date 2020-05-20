package de.my5t3ry.command.backup;

import de.my5t3ry.backup.BackupJobService;
import de.my5t3ry.backup.BackupService;
import de.my5t3ry.command.AbstractCommand;
import de.my5t3ry.lxc.LxcService;
import de.my5t3ry.print.PrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

/** User: my5t3ry Date: 5/4/20 9:58 PM */
@Component
public class BackupCommand extends AbstractCommand {
  @Autowired private PrintService printService;

   private List<AbstractBackupCommand> abstractBackupCommands;

//  @Value("${command.backup}")
//  private String command;

  @Autowired
  private AddCommand addCommand ;
  @Autowired
  private ListCommand listCommand ;
  @Autowired
  private Environment env;
  @PostConstruct
  public void ihit() {
    abstractBackupCommands = Arrays.asList(addCommand, listCommand);
  }

  protected BackupCommand() {
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
