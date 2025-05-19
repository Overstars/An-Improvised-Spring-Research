package com.straycat.repository;

import com.straycat.data.VisitLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * @description:
 * @param null:
 * @return:
 * @author: Overstars
 * @date: 2025/5/19 22:15
 */
@Repository
public interface VisitLogRepository extends JpaRepository<VisitLog, String> {
    // 自定义时间范围查询 (网页10)
    List<VisitLog> findByCreateTimeBetween(Instant start, Instant end);
}
