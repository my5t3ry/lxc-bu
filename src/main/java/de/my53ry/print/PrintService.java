package de.my53ry.print;

import de.my53ry.command.CommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import picocli.CommandLine;

import java.io.PrintStream;

/** User: my5t3ry Date: 5/4/20 1:57 PM */
@Service
public class PrintService {

  private final PrintStream IOProvider = System.out;
  @Autowired private CommandService commandService;

  @Value("${command.help}")
  private String command;

  public void print(final String msg) {
    IOProvider.println(msg);
  }

  private String extendWithColor(String value, String color) {
    return CommandLine.Help.Ansi.AUTO.string("@|" + color + " " + value + "|@");
  }

  public void printWithColor(String value, String color) {
    IOProvider.println(extendWithColor(value, color));
  }

  public void printHelp() {
    clearScreen();
    String format = "%-15s %s";
    IOProvider.println("commands");
    commandService
        .getCommands()
        .forEach(
            curCommand ->
                IOProvider.println(
                    String.format(
                        format, curCommand.getCommandsAsString(), curCommand.getDescription())));
  }

  public void clearScreen() {
    IOProvider.print("\033[H\033[2J");
    IOProvider.flush();
  }

  public void printStartMessage() {
    clearScreen();
    IOProvider.println(
        "enter ['" + command + "'] for help");
  }
}
