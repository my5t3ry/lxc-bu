package de.my5t3ry.backup;

import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/** User: my5t3ry Date: 5/19/20 2:38 PM */
@Entity
@Builder
@AllArgsConstructor
public class BackupJob {

  @Transient private static final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
  @Transient private static final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Long backupId;

  @Temporal(TemporalType.TIMESTAMP)
  private Date scheduledTime;

  @Temporal(TemporalType.TIMESTAMP)
  private Date finishedTime;

  @Lob private String log;

  public BackupJob() {}

  public String getScheduledTime() {
    return dateFormat.format(scheduledTime);
  }
}
