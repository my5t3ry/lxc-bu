package de.my5t3ry.command;

import de.my5t3ry.command.backup.AbstractBackupCommand;
import de.my5t3ry.command.backup.BackupCommand;
import de.my5t3ry.command.backup.ListCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/** User: my5t3ry Date: 5/20/20 7:03 PM */
@Configuration(proxyBeanMethods = false)
public class CommandConfig {
  @Autowired public ListCommand listCommand;
  @Autowired public BackupCommand backupCommand;
  @Autowired public HelpCommand helpCommand;

  @Bean
  public List<AbstractBackupCommand> abstractBackupCommands() {
    return Arrays.asList(listCommand);
  }

  @Bean
  public List<AbstractCommand> abstractCommands() {
    return Arrays.asList(helpCommand);
  }
}
