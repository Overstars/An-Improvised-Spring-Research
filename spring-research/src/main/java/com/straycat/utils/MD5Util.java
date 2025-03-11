package com.straycat.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    private static Logger logger = LoggerFactory.getLogger(MD5Util.class);
    /**
     * @description: 使用MessageDigest类计算32位小写摘要
     * @param source:
     * @return java.lang.String
     * @author: Overstars
     * @date: 2023/7/24 5:16
     */
    public static String encrypt(String source) {
        return encodeMd5(source.getBytes());
    }
    /**
     * @description: 
     * @param source: 
     * @return java.lang.String
     * @author: Overstars
     * @date: 2023/7/24 5:16
     */
    private static String encodeMd5(byte[] source) {
        try {
            return encodeHex(MessageDigest.getInstance("MD5").digest(source));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
    /**
     * @description: byte串转16进制String
     * @param bytes: 
     * @return java.lang.String
     * @author: Overstars
     * @date: 2023/7/24 5:16
     */
    private static String encodeHex(byte[] bytes) {
        StringBuffer buffer = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            // 不够大，转出来少一位，用0占位
            if ((bytes[i] & 0xff) < 0x10) {
                buffer.append("0");
            }
            buffer.append(Long.toString(bytes[i] & 0xff, 16));
        }
        return buffer.toString();
    }

    public static void main(String[] args) {
        logger.debug(encrypt("20210417000785945apple1435660288UVUMbcvaGHQL0PdWiVHs"));
    }
}
