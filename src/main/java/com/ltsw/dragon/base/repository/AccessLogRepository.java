package com.ltsw.dragon.base.repository;

import com.ltsw.dragon.base.entity.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author heshaobing
 */
@Repository
public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {
}
