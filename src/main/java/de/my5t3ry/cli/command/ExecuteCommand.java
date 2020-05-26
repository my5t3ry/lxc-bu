package de.my5t3ry.cli.command;

import de.my5t3ry.backup.BackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * User: my5t3ry Date: 5/4/20 9:58 PM
 */
@Component
public class ExecuteCommand extends AbstractCommand {
  @Autowired
  private BackupService backupService;
  @Autowired
  private Environment env;

  public void init() {
    setInfo(env.getProperty("command.exec"), "exec scheduled snapshots");
  }

  @Override
  public void execute(String command) {
    backupService.execute();
  }
}
