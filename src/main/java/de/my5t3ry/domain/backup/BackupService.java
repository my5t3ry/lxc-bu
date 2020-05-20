package de.my5t3ry.domain.backup;

import de.my5t3ry.cli.ui.print.PrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/** User: my5t3ry Date: 5/19/20 6:17 AM */
@Service
public class BackupService {

  @Autowired private BackupRepository backupRepository;
  @Autowired private PrintService printService;

  public void execute() {
    printService.print("exec");
  }

  public Backup addBackup(final String[] args) {
    final Backup backup =
        Backup.builder()
            .container(args[0])
            .backupInterval(Integer.parseInt(args[1]))
            .keepSnaphots(Integer.parseInt(args[2]))
            .build();
    backupRepository.save(backup);
    return backup;
  }

  public List<Backup> findAll() {
    return findAll();
  }
}
