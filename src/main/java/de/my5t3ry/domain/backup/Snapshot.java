package de.my5t3ry.domain.backup;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

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

  private static final ObjectMapper mapper = new ObjectMapper();

  private Date created_at;

  @SneakyThrows
  @Override
  public String toString() {
    return "snapshot ['"
            + this.name
            + "']: "
            + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
  }

  private Date last_used_at;
  private String architecture;
  private String name;
  private boolean stateful;
}
