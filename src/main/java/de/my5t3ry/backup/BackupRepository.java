package de.my5t3ry.backup;

import org.springframework.data.jpa.repository.JpaRepository;

/** User: my5t3ry Date: 5/19/20 6:26 AM */
public interface BackupRepository extends JpaRepository<Backup, Long> {}
