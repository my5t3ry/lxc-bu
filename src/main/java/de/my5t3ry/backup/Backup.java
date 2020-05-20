package de.my5t3ry.backup;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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

  public Backup() {}

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
