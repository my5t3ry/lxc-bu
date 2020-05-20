package de.my53ry;

import de.my53ry.backup.BackupJobService;
import de.my53ry.backup.BackupService;
import de.my53ry.command.AddCommand;
import de.my53ry.command.CommandService;
import de.my53ry.command.HelpCommand;
import de.my53ry.command.ListCommand;
import de.my53ry.lxc.LxcService;
import de.my53ry.print.PrintService;
import de.my53ry.shell.CmdService;
import de.my53ry.term.TerminalService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(proxyBeanMethods = false)
@Import({
  BackupService.class,
  BackupJobService.class,
  CommandService.class,
  LxcService.class,
  CmdService.class,
  PrintService.class,
  AddCommand.class,
  ListCommand.class,
  HelpCommand.class,
  TerminalService.class
})
@PropertySource("classpath:application.properties")
@PropertySource("classpath:banner.txt")
public class LcxBu {

  public LcxBu() {}

  public static void main(String[] args) {
    SpringApplication.run(LcxBu.class, args);
  }
}
