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
import java.util.stream.Collectors;

/** User: my5t3ry Date: 5/19/20 6:17 AM */
@Service
public class BackupService {

  @Autowired private BackupRepository backupRepository;
  @Autowired private PrintService printService;
  @Autowired private LxcService lxcService;

  private final ObjectMapper om = new ObjectMapper();

  public BackupService() {
    om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  public void execute() {
    //    final Date curDate = new Date();
    //    LocalDateTime localDateTime =
    //            curDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    //    localDateTime = localDateTime.plusDays(20);
    //    Date result = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    //    final List<Backup> acb tiveBackups = backupRepository.findBackupByScheduledBefore(result);
    final List<Backup> activeBackups =
        backupRepository.findAll().stream()
            .filter(curBackup -> curBackup.getScheduled().compareTo(new Date()) <= 0)
            .collect(Collectors.toList());
    if (activeBackups.size() > 0) {
      printService.printInfo("processing  ['" + activeBackups.size() + "'] scheduled snapshots");
      activeBackups.forEach(
          curBackup -> {
            createSnapshot(curBackup);
            removeLegacySnapshots(curBackup);
            printService.printInfo("done");
          });
    } else {
      printService.printInfo("nothing to process.");
    }
  }

  private void removeLegacySnapshots(Backup curBackup) {
    if (curBackup.getKeepSnapshots() < curBackup.getSnapshots().size()) {
      final List<Snapshot> legacySnapshots =
          curBackup
              .getSnapshots()
              .subList(0, curBackup.getSnapshots().size() - curBackup.getKeepSnapshots());
      legacySnapshots.forEach(
          curSnapshot -> {
            deleteSnapshot(curBackup, curSnapshot);
          });
      updateBackup(curBackup);
    }
  }

  private void deleteSnapshot(Backup curBackup, Snapshot curSnapshot) {
    try {
      printService.printInfo(
          "deleting legacy snapshot for backup ['"
              + curBackup
              + "'] snapshot ['"
              + curSnapshot.getName()
              + "']");
      lxcService.executeCmd(
          "delete", curBackup.getContainer().concat("/").concat(curSnapshot.getName()));
    } catch (IOException | InterruptedException e) {
      throw new IllegalStateException(
          "something went wrong while deleting snapshot for backup ['"
              + curBackup
              + "'] snapshot ['"
              + curSnapshot.getName()
              + "'] info with msg ['"
              + e.getMessage()
              + "']",
          e);
    }
  }

  private void createSnapshot(Backup curBackup) {
    try {
      printService.printInfo("creating snapshot for ['" + curBackup.getContainer() + "']");
      lxcService.executeCmd("snapshot", curBackup.getContainer());
      updateBackup(curBackup);
    } catch (IOException | InterruptedException e) {
      throw new IllegalStateException(
          "something went wrong while creating snapshot for backup ['"
              + curBackup
              + "'] info with msg ['"
              + e.getMessage()
              + "']",
          e);
    }
  }

  private Date getScheduleDate(Backup backup) {
    LocalDateTime localDateTime =
        backup.getScheduled().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    localDateTime = localDateTime.plusHours(backup.getScheduledInterval());
    return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
  }

  public Backup addBackup(final List<String> args) {
    final Backup backup =
        Backup.builder()
            .container(args.get(0))
            .scheduledInterval(BackupInterval.values.get(args.get(1)))
            .keepSnapshots(Integer.parseInt(args.get(2)))
            .build();
    updateBackup(backup);
    printService.printInfo("added backup ['" + backup.getContainer() + "'] ->");
    printService.print(backup);
    return backup;
  }

  private void updateBackup(Backup backup) {
    final List<Snapshot> existingSnapshots;
    try {
      existingSnapshots = getExistingSnapshots(backup);
      backup.setSnapshots(existingSnapshots);
      backup.setScheduled(getScheduleDate(backup));
      printService.printInfo(
          "next snapshot for ['"
              + backup.getContainer()
              + "'] scheduled @['"
              + backup.getScheduled()
              + "']");
      backupRepository.save(backup);
    } catch (IOException | InterruptedException e) {
      throw new IllegalStateException(
          "something went wrong while updating container snapshots ['"
              + backup.getContainer()
              + "'] info with msg ['"
              + e.getMessage()
              + "']",
          e);
    }
  }

  private List<Snapshot> getExistingSnapshots(Backup backup)
      throws IOException, InterruptedException {
    final String lxcOutput = lxcService.executeCmd("list", backup.getContainer(), "--format=json");
    final List<ContainerInfo> containerInfos = om.readValue(lxcOutput, new TypeReference<>() {});
    if (containerInfos.size() != 1) {
      throw new VerifyError(
          "something went wrong while receiving container info with msg ['" + lxcOutput + "']");
    }
    return containerInfos.get(0).getSnapshots();
  }

  public void createBackup(Backup backup) {
    createSnapshot(backup);
    removeLegacySnapshots(backup);
  }

  public void editBackup(Backup backup, List<String> arguments) {
    backup.setContainer(arguments.get(1));
    backup.setScheduledInterval(BackupInterval.values.get(arguments.get(2)));
    backup.setKeepSnapshots(Integer.parseInt(arguments.get(3)));
    backupRepository.save(backup);
    printService.printInfo("edited backup ['" + backup.getContainer() + "'] ->");
    printService.print(backup);
  }
}
