package de.my5t3ry.backup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/** User: my5t3ry Date: 5/19/20 3:25 PM */
@Service
public class BackupJobService {
  @Autowired private BackupJobRepository backupJobRepository;

  public BackupJob scheduleBackupJob(final Backup backup) {
    final BackupJob result =
        BackupJob.builder().backupId(backup.getId()).scheduledTime(getScheduleDate(backup)).build();
    return backupJobRepository.save(result);
  }

  private Date getScheduleDate(Backup backup) {
    final Date curDate = new Date();
    LocalDateTime localDateTime =
        curDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    localDateTime = localDateTime.plusDays(backup.getBackupInterval().getDays());
    Date result = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    return result;
  }
}
