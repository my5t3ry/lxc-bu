package de.my5t3ry.history;

import de.my5t3ry.command.TopLevelCommand;
import org.apache.commons.lang3.StringUtils;
import org.jline.reader.History;
import org.jline.reader.impl.history.DefaultHistory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/** User: my5t3ry Date: 5/4/20 5:24 PM */
public class LxcBuHistory extends DefaultHistory implements History {
  private final List<String> controlCommands = new ArrayList<>();

  @Autowired private TopLevelCommand topLevelCommand;

  public LxcBuHistory(TopLevelCommand topLevelCommand) {
    this.topLevelCommand = topLevelCommand;
    this.topLevelCommand
        .getCommands()
        .forEach(
            curCommand ->
                curCommand
                    .getCommands()
                    .forEach(curCommandString -> controlCommands.add(curCommandString)));
  }

  @Override
  public void add(Instant time, String line) {
    if (!controlCommands.contains(line) && !StringUtils.isNumeric(line)) {
      super.add(time, line);
    }
  }
}
