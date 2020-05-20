package de.my5t3ry.command.backup;

import com.jakewharton.fliptables.FlipTableConverters;
import de.my5t3ry.backup.Backup;
import de.my5t3ry.backup.BackupRepository;
import de.my5t3ry.print.PrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/** User: my5t3ry Date: 5/4/20 9:58 PM */
@Component
public class ListCommand extends AbstractBackupCommand {
  @Autowired private PrintService printService;

  @Autowired private BackupRepository backupRepository;

  @Value("${command.list}")
  private String command;

  protected ListCommand() {
    super();
  }

  @PostConstruct
  public void init() {
    setInfo(command, "list backups");
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
