package de.my5t3ry.domain.backup;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@Getter
public class ContainerInfo {
  private String architecture;
  private boolean ephemeral;
  private boolean stateful;
  private String description;
  private String created_at;
  private String name;
  private String status;
  private float status_code;
  private String last_used_at;
  private String location;
  private String backups = null;
  private String state = null;
  ArrayList<Snapshot> snapshots = new ArrayList<>();
}
