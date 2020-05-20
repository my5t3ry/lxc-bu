package de.my53ry.command;

import de.my53ry.backup.BackupService;
import de.my53ry.history.LxcBuHistory;
import de.my53ry.lxc.LxcService;
import de.my53ry.print.PrintService;
import de.my53ry.shell.ConsoleProgressBar;
import de.my53ry.term.TerminalService;
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

/** User: my5t3ry Date: 5/19/20 10:49 PM */
@Controller
public class CliController implements CommandLineRunner {

  private static ConsoleProgressBar consoleProgressBar;
  @Autowired private BackupService backupService;
  @Autowired private CommandService commandService;
  @Autowired private TerminalService terminalService;
  @Autowired private LxcService lxcService;
  @Autowired private PrintService printService;
  @Autowired private ConfigurableApplicationContext context;

  @Value("${command.exit}")
  private String exitCommand;

  @Value("${command.help}")
  private String helpCommand;

  @Override
  public void run(String... args) throws Exception {
    if (args.length == 1 && args[0].equals("e")) {
      backupService.execute();
    } else {
      startCli();
      System.exit(SpringApplication.exit(context));
    }
  }

  private void startCli() {
    try {
      consoleProgressBar = new ConsoleProgressBar();
      consoleProgressBar.start();

      terminalService.init();
      lxcService.validateLxc();
      LineReader lineReader =
          LineReaderBuilder.builder()
              .terminal(terminalService.getTerminal())
              .history(new LxcBuHistory(commandService))
              .build();
      consoleProgressBar.stop();
      printService.printStartMessage();
      while (true) {
        String line = null;
        try {
          line = lineReader.readLine("> ");
          if (line.equals(exitCommand)) {
            return;
          }
          for (AbstractCommand curCommand : commandService.getCommands()) {
            if (curCommand.executesCommand(line)) {
              curCommand.execute(line);
              break;
            }
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
