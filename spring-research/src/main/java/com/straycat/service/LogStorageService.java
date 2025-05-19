package com.straycat.service;

import com.straycat.data.VisitLog;
import com.straycat.repository.VisitLogRepository;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;

@Service
public class LogStorageService {
    private final VisitLogRepository repository;

    public LogStorageService(VisitLogRepository repository) {
        this.repository = repository;
    }

    public void storeLog(VisitLog log) {
        repository.save(log);
    }

    public List<VisitLog> queryLogs(long start, long end) {
        return repository.findByCreateTimeBetween(
                Instant.ofEpochMilli(start),
                Instant.ofEpochMilli(end)
        );
    }
}