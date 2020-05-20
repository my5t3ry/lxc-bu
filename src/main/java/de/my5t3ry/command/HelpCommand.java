package de.my5t3ry.command;

import de.my5t3ry.print.PrintService;
import de.my5t3ry.shell.ShellHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/** User: my5t3ry Date: 5/4/20 9:58 PM */
@Component
public class HelpCommand extends AbstractCommand {
  @Autowired private PrintService printService;

  @Value("${command.help}")
  private String command;

  protected HelpCommand() {
    super();
  }

  @PostConstruct
  public void init() {
    setInfo(command, "show help");
  }

  @Override
  public void execute(String command) {
    printService.printWithColor("test", ShellHelper.errorColor);
  }
}
