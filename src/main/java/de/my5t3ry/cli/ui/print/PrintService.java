package de.my5t3ry.cli.ui.print;

import de.my5t3ry.cli.command.CommandInteface;
import de.my5t3ry.cli.ui.PromptColor;
import de.my5t3ry.terminal.TerminalService;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import picocli.CommandLine;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/** User: my5t3ry Date: 5/4/20 1:57 PM */
@Service
public class PrintService {
  @Autowired private TerminalService terminalService;
  public static String infoColor = "green";
  public static String successColor = "white";
  public static String warningColor = "yellow";
  public static String errorColor = "red";
  public static String cyan = "cyan";

  @Value("${command.help}")
  private String command;

  private String extendWithColor(String value, String color) {
    return CommandLine.Help.Ansi.AUTO.string("@|" + color + " " + value + "|@");
  }

  public void printWithColor(String value, String color) {
    terminalService.getTerminal().writer().println(extendWithColor(value, color));
  }

  public void printCommands(final List<? extends CommandInteface> commands, final String context) {
    String format = "%-15s %s";
    terminalService.getTerminal().writer().println("['" + context + "'] commands");
    commands.forEach(
        curCommand ->
            terminalService
                .getTerminal()
                .writer()
                .println(
                    String.format(
                        format, curCommand.getCommandsAsString(), curCommand.getDescription())));
  }

  public void clearScreen() {
    terminalService.getTerminal().writer().print("\033[H\033[2J");
    terminalService.getTerminal().writer().flush();
  }

  public void printStartMessage() {
    printBanner();
    terminalService.getTerminal().writer().println("enter ['" + command + "'] for help");
  }

  public String getInfoMessage(String message) {
    return getColored(message, PromptColor.valueOf(infoColor));
  }

  public String getSuccessMessage(String message) {
    return getColored(message, PromptColor.valueOf(successColor));
  }

  public String getWarningMessage(String message) {
    return getColored(message, PromptColor.valueOf(warningColor));
  }

  public String getErrorMessage(String message) {
    return getColored(message, PromptColor.valueOf(errorColor));
  }

  public void print(String message) {
    print(message, null);
  }

  public void printSuccess(String message) {
    print(message, PromptColor.valueOf(successColor));
  }

  public void printInfo(String message) {
    print(message, PromptColor.valueOf(infoColor));
  }

  public void printWarning(String message) {
    print(message, PromptColor.valueOf(warningColor));
  }

  public void printError(String message) {
    print(message, PromptColor.valueOf(errorColor));
  }

  public void print(String message, PromptColor color) {
    String toPrint = message;
    if (color != null) {
      toPrint = getColored(message, color);
    }
    terminalService.getTerminal().writer().println(toPrint);
    terminalService.getTerminal().writer().flush();
  }

  public String getColored(String message, PromptColor color) {
    return (new AttributedStringBuilder())
        .append(message, AttributedStyle.DEFAULT.foreground(color.toJlineAttributedStyle()))
        .toAnsi();
  }

  private void printBanner() {
    try {
      final String banner =
          StreamUtils.copyToString(
              new ClassPathResource("banner.txt").getInputStream(), Charset.defaultCharset());
      printWithColor(banner, PrintService.cyan);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
