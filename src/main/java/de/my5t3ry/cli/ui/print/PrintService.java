package de.my5t3ry.cli.ui.print;

import com.jakewharton.fliptables.FlipTable;
import de.my5t3ry.cli.command.CommandInteface;
import de.my5t3ry.cli.ui.ConsoleProgressBar;
import de.my5t3ry.domain.backup.Backup;
import de.my5t3ry.domain.backup.BackupInterval;
import de.my5t3ry.terminal.TerminalService;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** User: my5t3ry Date: 5/4/20 1:57 PM */
@Service
public class PrintService {
  public static int green = 2;
  public static int white = 7;
  public static int yellow = 3;
  public static int red = 1;
  public static int bright = 0;
  public static int cyan = 6;
  public static int magenta = 5;
  public static int blue = 4;
  public static int black = 0;

  private static final String DATE_FORMAT = "yyyy/MM/dd HH:mm";
  private static final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

  @Autowired private TerminalService terminalService;

  @Value("${command.help}")
  private String command;

  private ConsoleProgressBar consoleProgressBar;

  public void startSpinner() {
    consoleProgressBar = new ConsoleProgressBar();
    consoleProgressBar.start();
  }

  public void stopSpinner() {
    consoleProgressBar.stop();
    print("\n");
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
    return getColored(message, green);
  }

  public String getSuccessMessage(String message) {
    return getColored(message, white);
  }

  public String getWarningMessage(String message) {
    return getColored(message, yellow);
  }

  public String getErrorMessage(String message) {
    return getColored(message, red);
  }

  public void print(String message) {
    print(message, black);
  }

  public void printSuccess(String message) {
    print(message, white);
  }

  public void printInfo(String message) {
    print(message, green);
  }

  public void printWarning(String message) {
    print("warning: ".concat(message), yellow);
  }

  public void printError(String message) {
    print(message, red);
  }

  public void print(String message, int color) {
    String toPrint = message;
    toPrint = getColored(message, color);
    terminalService.getTerminal().writer().println(toPrint);
    terminalService.getTerminal().writer().flush();
  }

  public String getColored(String message, int color) {
    return (new AttributedStringBuilder())
        .append(message, AttributedStyle.DEFAULT.foreground(color))
        .toAnsi();
  }

  private void printBanner() {
    try {
      final String banner =
          StreamUtils.copyToString(
              new ClassPathResource("banner.txt").getInputStream(), Charset.defaultCharset());
      print(banner, PrintService.cyan);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void printTable(List<Backup> backups) {
    String[] headers = {
      "id", "container", "interval", "scheduled", "snaps", "cur/max",
    };
    List<List<String>> dataList = new ArrayList<>();
    backups.forEach(
        curBackup -> {
          dataList.add(getPrintData(curBackup));
        });
    String[][] data = toDoubleIndexArray(dataList);
    print(FlipTable.of(headers, data), PrintService.white);
  }

  private List<String> getPrintData(Backup curBackup) {
    return Arrays.asList(
        curBackup.getId().toString(),
        curBackup.getContainer(),
        BackupInterval.getKey(curBackup.getScheduledInterval()),
        dateFormat.format(curBackup.getScheduled()),
        curBackup.getSnapshotsAsString(),
        String.format("%d/%d", curBackup.getExistingSnaphots(), curBackup.getKeepSnapshots()));
  }

  private String[][] toDoubleIndexArray(List<List<String>> mergedList) {
    String[][] result = new String[mergedList.size()][];
    for (int i = 0; i < mergedList.size(); i++) {
      List<String> currentList = mergedList.get(i);
      result[i] = currentList.toArray(new String[currentList.size()]);
    }
    return result;
  }

  public void print(Backup backup) {
    String[] headers = {
      "id", "container", "interval", "scheduled", "snaps", "cur/max",
    };
    List<List<String>> dataList = new ArrayList<>();
    dataList.add(getPrintData(backup));
    String[][] data = toDoubleIndexArray(dataList);
    print(FlipTable.of(headers, data), PrintService.white);
  }
}
