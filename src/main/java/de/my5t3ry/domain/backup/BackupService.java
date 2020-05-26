package de.my5t3ry.domain.backup;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.my5t3ry.cli.ui.print.PrintService;
import de.my5t3ry.lxc.LxcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * User: my5t3ry Date: 5/19/20 6:17 AM
 */
@Service
public class BackupService {

  @Autowired
  private BackupRepository backupRepository;
  @Autowired
  private PrintService printService;
  @Autowired
  private LxcService lxcService;

  private final ObjectMapper om = new ObjectMapper();

  public BackupService() {
    om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  public void execute() {
    printService.print("exec");
  }

  private Date getScheduleDate(Backup backup) {
    final Date curDate = new Date();
    LocalDateTime localDateTime =
            curDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    localDateTime = localDateTime.plusDays(backup.getBackupInterval());
    Date result = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    return result;
  }

  public Backup addBackup(final String[] args) throws IOException, InterruptedException {
    final Backup backup =
            Backup.builder()
                    .container(args[0])
                    .backupInterval(BackupInterval.values.get(args[1]))
                    .keepSnaphots(Integer.parseInt(args[2]))
                    .build();
    final List<Snapshot> existingSnapshots = getExistingSnapshots(backup);
    backup.setSnapshots(existingSnapshots);
    backup.setScheduled(getScheduleDate(backup));
    backupRepository.save(backup);
    return backup;
  }

  private List<Snapshot> getExistingSnapshots(Backup backup)
          throws IOException, InterruptedException {
    final String lxcOutput = lxcService.executeCmd("list", backup.getContainer(), "--format=json");
    final List<ContainerInfo> containerInfos =
            om.readValue(lxcOutput, new TypeReference<List<ContainerInfo>>() {
            });
    if (containerInfos.size() != 1) {
      throw new VerifyError(
              "something went wrong while receiving container info with msg ['" + lxcOutput + "']");
    }
    return containerInfos.get(0).getSnapshots();
  }

  public List<Backup> findAll() {
    return findAll();
  }
}
