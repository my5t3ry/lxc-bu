package de.my5t3ry.cli.command.backup;

import de.my5t3ry.backup.Backup;
import de.my5t3ry.backup.BackupInterval;
import de.my5t3ry.backup.BackupRepository;
import de.my5t3ry.cli.command.AbstractCommand;
import de.my5t3ry.cli.ui.print.PrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/** User: my5t3ry Date: 5/4/20 9:58 PM */
@Component
public class BackupListCommand extends AbstractCommand {
  @Autowired private PrintService printService;
  @Autowired private BackupRepository backupRepository;
  @Autowired private Environment env;

  public void init() {
    setInfo(env.getProperty("command.list"), "list backups");
  }

  @Override
  public void execute(String command) {
    final List<Backup> backups = backupRepository.findAll();
    if (backups.isEmpty()) {
      printService.printInfo(
              String.format("no backup found. add job with ['backup add [<remote>:]<source>[/<snapshot>] <interval(%s)> <keep-snapshots(int)']", BackupInterval.values.keySet().stream().collect(Collectors.joining(","))));
    } else {
      printService.printTable(backups);
    }
  }
}
