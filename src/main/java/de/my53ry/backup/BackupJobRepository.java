package de.my53ry.backup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** User: my5t3ry Date: 5/19/20 2:40 PM */
@Repository
public interface BackupJobRepository extends JpaRepository<BackupJob, Long> {
  BackupJob findByBackupId(Long backupId);
}
