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
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

@SpringBootApplication(proxyBeanMethods = false)
// @EnableJpaRepositories("de.my53ry")
@ImportResource({"classpath*:application.properties", "classpath*:banner.txt"})
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
    try {
      new SpringApplicationBuilder(LcxBu.class).properties(props()).build().run(args);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static Properties props() throws IOException {
    Resource resource = new ClassPathResource("/application.properties");
    return PropertiesLoaderUtils.loadProperties(resource);
  }
}
