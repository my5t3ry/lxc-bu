package de.my5t3ry.domain.backup;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/** User: my5t3ry Date: 5/19/20 6:17 AM */
@Entity
@Data
@Builder
@AllArgsConstructor
public class Backup {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String container;
  private int scheduledInterval;
  private int keepSnapshots;

  @OneToMany(cascade = CascadeType.ALL)
  private List<Snapshot> snapshots = new ArrayList<>();

  public Date getScheduled() {
    if (scheduled == null) {
      return new Date();
    }
    return scheduled;
  }

  private Date scheduled;

  public Backup() {}

  public int getExistingSnaphots() {
    return snapshots.size();
  }

  @Override
  public String toString() {
    return "Backup{"
        + "container='"
        + container
        + '\''
        + ", interval="
        + scheduledInterval
        + ", keepSnaphots="
        + keepSnapshots
        + '}';
  }

  public String getSnapshotsAsString() {
    final StringBuilder sb = new StringBuilder();
    snapshots.forEach(curSnapshot -> sb.append(curSnapshot.toString()));
    return sb.toString();
  }
}
