package de.my5t3ry.shell;

import org.jline.terminal.Terminal;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import javax.annotation.PostConstruct;

/** User: my5t3ry Date: 5/19/20 6:31 AM */
public class ShellHelper {
  private final Terminal terminal;
  public static String infoColor = "green";
  public static String successColor = "white";
  public static String warningColor = "yellow";
  public static String errorColor = "red";
  public static String cyan = "cyan";

  public ShellHelper(Terminal terminal) {
    this.terminal = terminal;
  }


  public String getColored(String message, PromptColor color) {
    return (new AttributedStringBuilder())
        .append(message, AttributedStyle.DEFAULT.foreground(color.toJlineAttributedStyle()))
        .toAnsi();
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
    terminal.writer().println(toPrint);
    terminal.flush();
  }
}
