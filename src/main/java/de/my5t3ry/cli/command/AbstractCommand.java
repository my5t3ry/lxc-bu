package de.my5t3ry.cli.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/** User: my5t3ry Date: 5/4/20 9:53 PM */
public abstract class AbstractCommand implements CommandInteface {
  private final List<String> commands = new ArrayList<>();
  private String description;

  public AbstractCommand() {}

  public void setInfo(String commands, String description) {
    this.description = description;
    this.commands.addAll(Arrays.asList(commands.split(",")));
  }

  protected String stripParentCommand(final String cmd) {
    final List<String>[] result = new List[] {Arrays.asList(cmd.split(" "))};
    if (result[0].size() == 1) {
      return "";
    }
    commands.forEach(
        curCommand -> {
          if (result[0].get(0).equals(curCommand)) {
            result[0] = result[0].subList(1, result[0].size());
          }
        });
    return result[0].stream().collect(Collectors.joining(" "));
  }

  public boolean executesCommand(final String command) {
    return commands.contains(command.split(" ")[0]);
  }

  public abstract void execute(String command);

  public List<String> getCommands() {
    return commands;
  }

  public String getCommandsAsString() {
    return String.join(", ", commands);
  }

  public String getDescription() {
    return description;
  }
}
