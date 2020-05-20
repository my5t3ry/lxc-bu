package de.my5t3ry.print;

import de.my5t3ry.command.CommandInteface;
import de.my5t3ry.command.TopLevelCommand;
import de.my5t3ry.shell.ShellHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import picocli.CommandLine;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.List;

/** User: my5t3ry Date: 5/4/20 1:57 PM */
@Service
public class PrintService {

  private final PrintStream IOProvider = System.out;
  @Autowired private TopLevelCommand topLevelCommand;

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

  public void printCommands(final List<? extends CommandInteface> commands, final String context) {
    clearScreen();
    String format = "%-15s %s";
    IOProvider.println("['" + context + "'] commands");
    commands.forEach(
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
    clearBannern();
    IOProvider.println("enter ['" + command + "'] for help");
  }

  private void clearBannern() {
    try {
      final String banner =
          StreamUtils.copyToString(
              new ClassPathResource("banner.txt").getInputStream(), Charset.defaultCharset());
      printWithColor(banner, ShellHelper.cyan);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void printTopLevelCommands() {
    this.printCommands(topLevelCommand.getCommands(), "top-level");
  }
}
