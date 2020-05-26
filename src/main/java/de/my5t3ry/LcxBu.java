package de.my5t3ry;

import de.my5t3ry.cli.command.ExecuteCommand;
import de.my5t3ry.cli.command.HelpCommand;
import de.my5t3ry.cli.command.TopLevelCommand;
import de.my5t3ry.cli.command.backup.AddCommand;
import de.my5t3ry.cli.command.backup.BackupCommand;
import de.my5t3ry.cli.command.backup.BackupListCommand;
import de.my5t3ry.cli.command.container.ContainerCommand;
import de.my5t3ry.cli.command.container.ContainerListCommand;
import de.my5t3ry.cli.ui.print.PrintService;
import de.my5t3ry.config.Config;
import de.my5t3ry.domain.backup.*;
import de.my5t3ry.lxc.LxcService;
import de.my5t3ry.os.ProcessService;
import de.my5t3ry.terminal.TerminalService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

@SpringBootApplication(proxyBeanMethods = false)
@Import({
        BackupService.class,
        BackupRepository.class,
        TopLevelCommand.class,
        AddCommand.class,
        BackupCommand.class,
        ExecuteCommand.class,
        BackupListCommand.class,
        HelpCommand.class,
        ContainerCommand.class,
        ContainerListCommand.class,
        LxcService.class,
        ProcessService.class,
        PrintService.class,
        Config.class,
        Backup.class,
        ContainerInfo.class,
        Snapshot.class,
        TerminalService.class
})
public class LcxBu {
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
