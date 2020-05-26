package de.my5t3ry.domain.backup;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
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

  private int backupInterval;

  private int keepSnaphots;

  @OneToMany
  private List<Snapshot> snapshots = new ArrayList<>();

  public Backup() {
  }

  @Override
  public String toString() {
    return "Backup{"
            + "container='"
            + container
            + '\''
            + ", interval="
        + backupInterval
        + ", keepSnaphots="
        + keepSnaphots
        + '}';
  }
}
