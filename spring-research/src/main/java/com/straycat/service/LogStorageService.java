package com.straycat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.straycat.data.VisitLog;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBIterator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class LogStorageService {

    private final DB db;
    private final ObjectMapper mapper = new ObjectMapper();

    public LogStorageService(@Qualifier("visitLogDB") DB db) {
        this.db = db;
    }

    public void storeLog(VisitLog log) throws JsonProcessingException {
        byte[] key = log.getId().getBytes(StandardCharsets.UTF_8);
        byte[] value = mapper.writeValueAsBytes(log);
        db.put(key, value);
    }

    public List<VisitLog> queryLogs(long startTime, long endTime) {
        List<VisitLog> logs = new ArrayList<>();
        try (DBIterator iterator = db.iterator()) {
            for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
                VisitLog log = mapper.readValue(iterator.peekNext().getValue(), VisitLog.class);
                if (log.getTimestamp() >= startTime && log.getTimestamp() <= endTime) {
                    logs.add(log);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Query failed", e);
        }
        return logs;
    }
}