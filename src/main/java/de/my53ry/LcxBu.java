package de.my53ry;

import de.my53ry.backup.BackupJobService;
import de.my53ry.backup.BackupService;
import de.my53ry.command.CommandService;
import de.my53ry.command.HelpCommand;
import de.my53ry.command.ListCommand;
import de.my53ry.command.backup.AddCommand;
import de.my53ry.command.backup.BackupCommand;
import de.my53ry.config.Config;
import de.my53ry.lxc.LxcService;
import de.my53ry.print.PrintService;
import de.my53ry.shell.CmdService;
import de.my53ry.term.TerminalService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication(proxyBeanMethods = false)
@Import({
  BackupService.class,
  BackupJobService.class,
  CommandService.class,
  LxcService.class,
  CmdService.class,
  PrintService.class,
  AddCommand.class,
  BackupCommand.class,
  ListCommand.class,
  HelpCommand.class,
  Config.class,
  TerminalService.class
})
public class LcxBu {

  public LcxBu() {}

  public static void main(String[] args) {
    SpringApplication.run(LcxBu.class, args);
  }
}
