package de.my5t3ry.cli.command.backup;

import com.jakewharton.fliptables.FlipTableConverters;
import de.my5t3ry.cli.command.AbstractCommand;
import de.my5t3ry.domain.backup.Backup;
import de.my5t3ry.domain.backup.BackupRepository;
import de.my5t3ry.cli.ui.print.PrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;

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
      printService.print(
          "no backup found. add job with ['backup add [<remote>:]<source>[/<snapshot>] <interval(DAILY,WEEKLY)> <keep-snapshots(int)']");
    } else {
      System.out.println(FlipTableConverters.fromIterable(backups, Backup.class));
    }
  }
}
