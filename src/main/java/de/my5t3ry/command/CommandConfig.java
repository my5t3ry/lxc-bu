package de.my5t3ry.command;

import de.my5t3ry.command.backup.ListCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/** User: my5t3ry Date: 5/20/20 7:03 PM */
@Configuration(proxyBeanMethods = false)
public class CommandConfig {
  @Autowired public ListCommand listCommand;
}
