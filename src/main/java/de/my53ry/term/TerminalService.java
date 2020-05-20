package de.my53ry.term;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;

/** User: my5t3ry Date: 5/5/20 5:07 AM */
@Service
public class TerminalService {

  public Terminal terminal;

  public void init() throws IOException {
    terminal = TerminalBuilder.builder().system(true).nativeSignals(true).build();
  }

  public Terminal getTerminal() {
    return terminal;
  }
}
