package de.my5t3ry;

import de.my5t3ry.backup.Backup;
import de.my5t3ry.backup.BackupJob;
import de.my5t3ry.backup.BackupJobService;
import de.my5t3ry.backup.BackupService;
import de.my5t3ry.command.HelpCommand;
import de.my5t3ry.command.TopLevelCommand;
import de.my5t3ry.command.backup.AddCommand;
import de.my5t3ry.command.backup.BackupCommand;
import de.my5t3ry.command.backup.ListCommand;
import de.my5t3ry.config.Config;
import de.my5t3ry.lxc.LxcService;
import de.my5t3ry.print.PrintService;
import de.my5t3ry.shell.CmdService;
import de.my5t3ry.term.TerminalService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication(proxyBeanMethods = false)
// @EnableJpaRepositories("de.my53ry")
@Import({
  BackupService.class,
  BackupJobService.class,
  TopLevelCommand.class,
  LxcService.class,
  CmdService.class,
  PrintService.class,
  AddCommand.class,
  BackupCommand.class,
  ListCommand.class,
  HelpCommand.class,
  Config.class,
  Backup.class,
  BackupJob.class,
  TerminalService.class
})
public class LcxBu {

  public LcxBu() {}

  public static void main(String[] args) {
    SpringApplication.run(LcxBu.class, args);
  }
}
