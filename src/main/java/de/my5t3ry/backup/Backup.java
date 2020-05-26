package de.my5t3ry.backup;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

  public boolean hasSnapshot(String snapshotArgument) {
    return getSnapshotsFromArgument(snapshotArgument).size() > 0;
  }

  private List<Snapshot> getSnapshotsFromArgument(String snapshotArgument) {
    return snapshots.stream()
        .filter(
            curSnapshot ->
                curSnapshot.getName().equals(snapshotArgument)
                    || StringUtils.isNumeric(snapshotArgument)
                        && curSnapshot.getId().equals(Long.valueOf(snapshotArgument)))
        .collect(Collectors.toList());
  }

  public Snapshot getSnapShotFromArgument(String s) throws VerifyError {
    final List<Snapshot> snapshots = getSnapshotsFromArgument(s);
    if (snapshots.size() != 1) {
      throw new VerifyError(
          "could not find distinct snapshot for argument ['"
              + s
              + "'] found ['"
              + snapshots.size()
              + "'] matches");
    }

    return snapshots.get(0);
  }
}
