package de.my5t3ry.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** User: my5t3ry Date: 5/4/20 9:53 PM */
public abstract class AbstractCommand implements CommandInteface {
  private List<String> commands = new ArrayList<>();
  private String description;

  public AbstractCommand() {}

  public void setInfo(String commands, String description) {
    this.description = description;
    this.commands.addAll(Arrays.asList(commands.split(",")));
  }

  protected String stripParentCommands(final String cmd) {
    final String[] result = {cmd};
    commands.forEach(curCommand -> result[0] = cmd.replace(curCommand, ""));
    return result[0];
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
