package de.my5t3ry.cli.command;

/** User: my5t3ry Date: 5/20/20 6:10 AM */
public interface CommandInteface {
  public void execute(String command);

  public boolean executesCommand(final String command);

  public String getCommandsAsString();

  public String getDescription();

  public void init();
}
