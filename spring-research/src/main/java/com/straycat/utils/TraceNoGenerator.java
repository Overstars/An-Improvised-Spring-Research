package com.straycat.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author 16353 Overstars
 * @Date 2023/12/7 0:10
 * @Version 1.0.0
 * @Description 生成全局唯一、有序的64位整数ID
 * 第一位固定为0
 * 41位时间戳：精确到毫秒级，记录ID生成的时间。
 * 10位机器ID：用于标识不同机器或节点，可自定义分配。5位数据中心ID+5位机器ID
 * 12位序列号：解决同一毫秒内生成多个ID的冲突问题。
 */
public class TraceNoGenerator {
    private final long workerId;          // 机器ID
    private final long datacenterId;      // 数据中心ID
    private long sequence = 0L;           // 序列号
    private long lastTimestamp = -1L;     // 上次生成ID的时间戳
    // 配置参数
    private static final long MAX_WORKER_ID = 31L;
    private static final long MAX_DATACENTER_ID = 31L;
    private static final long WORKER_ID_BITS = 5L;
    private static final long DATACENTER_ID_BITS = 5L;
    private static final long SEQUENCE_BITS = 12L; // 12位序列号
    // 位移偏移量
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS; // 机器ID位移
    private static final long DATACENTER_ID_SHIFT = WORKER_ID_BITS + SEQUENCE_BITS; // 数据中心ID位移
    private static final long TIMESTAMP_SHIFT = DATACENTER_ID_BITS + WORKER_ID_BITS + SEQUENCE_BITS; // 时间戳位移


    protected static final Logger logger = LoggerFactory.getLogger("TraceNoGenerator");
    /**
     * @description: 构造函数
     * @param workerId: 机器ID
     * @param datacenterId: 数据中心ID
     * @return: 
     * @author: Overstars
     * @date: 2023/12/7 0:20
     */
    public TraceNoGenerator (long workerId, long datacenterId) {
        // 检查workerId和datacenterId是否合法
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException("Worker ID must be between 0 and " + MAX_WORKER_ID);
        }
        if (datacenterId > MAX_DATACENTER_ID || datacenterId < 0) {
            throw new IllegalArgumentException("Datacenter ID must be between 0 and " + MAX_DATACENTER_ID);
        }

        // 设置workerId和datacenterId
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }
    /**
     * @description: 生成全局唯一、有序的64位整数ID
     * @param : 无
     * @return: long
     * @author: Overstars
     * @date: 2023/12/7 0:23
     */
    public synchronized long generateUniqueId() {
        long timestamp = getCurrentTimestamp();

        // 检查时钟回退情况
        if (timestamp < this.lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate ID.");
        }

        // 同一毫秒内自增序列号
        if (timestamp == this.lastTimestamp) {
            sequence = (sequence + 1) & ((1 << SEQUENCE_BITS) - 1);  // 自增并掩码
            if (sequence == 0) {
                // 序列号用完，等待下一毫秒
                timestamp = getNextTimestamp();
            }
        } else {
            sequence = 0L;  // 不同毫秒重置序列号
        }

        this.lastTimestamp = timestamp;

        // 组合生成唯一ID
        return ((timestamp << TIMESTAMP_SHIFT) |
                (datacenterId << DATACENTER_ID_SHIFT)) |
                ((workerId << WORKER_ID_SHIFT) |
                sequence);
    }
    private long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }
    private long getNextTimestamp() {
        long timestamp = getCurrentTimestamp();
        while (timestamp <= this.lastTimestamp) {
            timestamp = getCurrentTimestamp();
        }
        return timestamp;
    }
    /**
     * @description: 传入一个String，若长度＜length，则会在左侧用0填充
     * @param inputString
     * @param length:
     * @return: java.lang.String
     * @author: Overstars
     * @date: 2023/12/7 1:00
     */
    public static String padLeftZeros(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append('0');
        }
        sb.append(inputString);

        return sb.toString();
    }

    // ==============================测试==============================
    public static void main(String[] args) {
        // 创建一个雪花算法生成器，传入机器ID和数据中心ID
        TraceNoGenerator idGenerator = new TraceNoGenerator(0, 0);
        // 生成10个唯一ID并打印
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                long uniqueId = idGenerator.generateUniqueId();
                logger.info("Generated Unique ID : {}, {}", uniqueId, padLeftZeros(Long.toBinaryString(uniqueId), 64));
            }).start();
        }
    }
}
