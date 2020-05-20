package de.my5t3ry.cli.command;

import de.my5t3ry.cli.ui.print.PrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/** User: my5t3ry Date: 5/4/20 9:58 PM */
@Component
public class HelpCommand extends AbstractCommand {
  @Autowired private PrintService printService;
  @Autowired private Environment env;


  public void init() {
    setInfo(env.getProperty("command.help"), "show help");
  }

  @Override
  public void execute(String command) {
    return;
  }
}
