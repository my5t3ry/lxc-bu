package de.my53ry.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/** User: my5t3ry Date: 5/4/20 9:52 PM */
@Service
public class CommandService {
  @Autowired private List<AbstractCommand> commands = new ArrayList<>();

  public List<AbstractCommand> getCommands() {
    return commands;
  }
}
