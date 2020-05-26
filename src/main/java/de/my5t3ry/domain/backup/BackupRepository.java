package de.my5t3ry.domain.backup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * User: my5t3ry Date: 5/19/20 6:26 AM
 */
public interface BackupRepository extends JpaRepository<Backup, Long> {

  @Query("select a from Backup a where a.scheduled <= :current")
  List<Backup> findAllWithScheduledBefore(@Param("current") Date current);
}
