package de.my5t3ry.domain.backup;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * User: my5t3ry Date: 5/19/20 6:26 AM
 */
public interface BackupRepository extends JpaRepository<Backup, Long> {
  List<Backup> findBackupByScheduledBefore(final Date current);
}
