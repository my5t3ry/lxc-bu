package de.my53ry.backup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** User: my5t3ry Date: 5/19/20 6:26 AM */
@Repository
public interface BackupRepository extends JpaRepository<Backup, Long> {}
