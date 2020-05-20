package de.my5t3ry.cli;

import de.my5t3ry.cli.command.TopLevelCommand;
import de.my5t3ry.cli.history.LxcBuHistory;
import de.my5t3ry.cli.ui.ConsoleProgressBar;
import de.my5t3ry.cli.ui.print.PrintService;
import de.my5t3ry.domain.backup.BackupService;
import de.my5t3ry.lxc.LxcService;
import de.my5t3ry.terminal.TerminalService;
import org.apache.commons.lang3.StringUtils;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Arrays;

/** User: my5t3ry Date: 5/19/20 10:49 PM */
@Controller
public class CliController implements CommandLineRunner {

  private static ConsoleProgressBar consoleProgressBar;
  @Autowired private BackupService backupService;
  @Autowired private TopLevelCommand topLevelCommand;
  @Autowired private TerminalService terminalService;
  @Autowired private LxcService lxcService;
  @Autowired private PrintService printService;
  @Autowired private ConfigurableApplicationContext context;

  @Value("${command.exit}")
  private String exitCommand;

  @Override
  public void run(String... args) {
    try {
      terminalService.init();
      lxcService.validateLxc();
      initContainers();
      if (args.length == 1 && args[0].equals("e")) {
        backupService.execute();
      } else {
        startCli();
        System.exit(SpringApplication.exit(context));
      }
    } catch (Exception e) {
      printService.printError("lxc not reachable ");
      e.printStackTrace();
      System.exit(SpringApplication.exit(context));
    }
  }

  private void initContainers() {
    topLevelCommand.init();
  }

  private void startCli() {
    try {
      consoleProgressBar = new ConsoleProgressBar();
      consoleProgressBar.start();
      LineReader lineReader =
          LineReaderBuilder.builder()
              .terminal(terminalService.getTerminal())
              .history(new LxcBuHistory(topLevelCommand))
              .build();
      consoleProgressBar.stop();
      printService.printStartMessage();
      while (true) {
        String line = null;
        try {
          line = lineReader.readLine("> ");
          if (Arrays.asList(exitCommand.split(",")).contains(line)) {
            return;
          }
          if (StringUtils.isNotBlank(line)) {
            topLevelCommand.execute(line);
          }
        } catch (UserInterruptException | EndOfFileException e) {
          return;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }
  }
}
