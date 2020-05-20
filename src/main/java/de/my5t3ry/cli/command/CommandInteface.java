package de.my5t3ry.cli.command;

/** User: my5t3ry Date: 5/20/20 6:10 AM */
public interface CommandInteface {
  void execute(String command);

  boolean executesCommand(final String command);

  String getCommandsAsString();

  String getDescription();

  void init();
}
