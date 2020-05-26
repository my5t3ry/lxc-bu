package de.my5t3ry.domain.backup;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Snapshot {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Date created_at;
  private Date last_used_at;
  private String architecture;
  private String name;
  private boolean stateful;
}
