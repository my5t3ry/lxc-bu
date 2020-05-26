package de.my5t3ry.domain.backup;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Getter
@Entity
public class ContainerInfo {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

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

  @OneToMany(cascade = CascadeType.ALL)
  private List<Snapshot> snapshots = new ArrayList<>();
}
