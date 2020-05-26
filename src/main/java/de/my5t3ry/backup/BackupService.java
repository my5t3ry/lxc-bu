package de.my5t3ry.backup;

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
    final List<Backup> activeBackups =
        backupRepository.findAll().stream()
            .filter(curBackup -> curBackup.getScheduled().compareTo(new Date()) <= 0)
            .collect(Collectors.toList());
    if (activeBackups.size() > 0) {
      printService.printInfoWithTimeStamp(
          String.format("processing  ['%d'] scheduled snapshots", activeBackups.size()));
      activeBackups.forEach(
          curBackup -> {
            createSnapshot(curBackup);
            removeLegacySnapshots(curBackup);
            printService.printInfoWithTimeStamp("done");
          });
    } else {
      printService.printInfoWithTimeStamp("nothing to process.");
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

  public void deleteSnapshot(Backup curBackup, Snapshot curSnapshot) {
    try {
      printService.printInfo(
          String.format(
              "deleting snapshot for backup ['%s'] snapshot ['%s']",
              curBackup, curSnapshot.getName()));
      lxcService.executeCmd(
          "delete", curBackup.getContainer().concat("/").concat(curSnapshot.getName()));
      updateBackup(curBackup);
    } catch (IOException | InterruptedException e) {
      throw new IllegalStateException(
          String.format(
              "something went wrong while deleting snapshot for backup ['%s'] snapshot ['%s'] info with msg ['%s']",
              curBackup, curSnapshot.getName(), e.getMessage()),
          e);
    }
  }

  private void createSnapshot(Backup curBackup) {
    try {
      printService.printInfo("creating snapshot for ['" + curBackup.getContainer() + "']");
      lxcService.executeCmd("snapshot", curBackup.getContainer());
      updateBackup(curBackup);
      setNextScheduledDate(curBackup);
    } catch (IOException | InterruptedException e) {
      throw new IllegalStateException(
          String.format(
              "something went wrong while creating snapshot for backup ['%s'] info with msg ['%s']",
              curBackup, e.getMessage()),
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
    printService.printInfo(String.format("added backup ['%s'] ->", backup.getContainer()));
    printService.print(backup);
    return backup;
  }

  private void updateBackup(Backup backup) {
    final List<Snapshot> existingSnapshots;
    try {
      existingSnapshots = getExistingSnapshots(backup);
      backup.setSnapshots(existingSnapshots);
      printService.printInfo(
          "next snapshot for ['"
              + backup.getContainer()
              + "'] scheduled @['"
              + backup.getScheduled()
              + "']");
      backupRepository.save(backup);
    } catch (IOException | InterruptedException e) {
      throw new IllegalStateException(
          String.format(
              "something went wrong while updating container snapshots ['%s'] info with msg ['%s']",
              backup.getContainer(), e.getMessage()),
          e);
    }
  }

  private void setNextScheduledDate(Backup backup) {
    backup.setScheduled(getScheduleDate(backup));
    backupRepository.save(backup);
  }

  private List<Snapshot> getExistingSnapshots(Backup backup)
      throws IOException, InterruptedException {
    final String lxcOutput = lxcService.executeCmd("list", backup.getContainer(), "--format=json");
    final List<ContainerInfo> containerInfos = om.readValue(lxcOutput, new TypeReference<>() {});
    if (containerInfos.size() != 1) {
      throw new VerifyError(
          String.format(
              "something went wrong while receiving container info with msg ['%s']", lxcOutput));
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
    updateBackup(backup);
    final Backup result = backupRepository.save(backup);
    printService.printInfo(String.format("edited backup ['%s'] ->", result.getContainer()));
    printService.print(result);
  }

  public List<Backup> findByContainer(String containerName) {
    return backupRepository.findAll().stream()
        .filter(curBackup -> curBackup.getContainer().equals(containerName))
        .collect(Collectors.toList());
  }
}
