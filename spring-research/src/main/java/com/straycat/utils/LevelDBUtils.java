package com.straycat.utils;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBFactory;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;

import java.io.File;
import java.io.IOException;
/**
 * @projectName: spring-research
 * @package: com.straycat.utils
 * @className: LevelDBUtils
 * @author: Overstars
 * @description: TODO
 * @date: 2025/2/11 15:51
 * @version: 1.0
 */
public class LevelDBUtils {
    public static void main(String[] args) {
        // 指定 LevelDB 数据库的存储路径
        File dbPath = new File("leveldb-demo");


        // 配置 LevelDB 选项，如创建缺失的数据库
        Options options = new Options();
        options.createIfMissing(true);


        DBFactory factory = new Iq80DBFactory();
        // 用于存储数据库引用的变量
        DB db = null;


        try {
            // 打开数据库
            db = factory.open(dbPath, options);


            // 存储数据
            String key = "greeting";
            String value = "Hello, LevelDB!";
            db.put(key.getBytes(), value.getBytes());


            // 检索数据
            byte[] retrievedData = db.get(key.getBytes());
            if (retrievedData != null) {
                System.out.println("Retrieved value: " + new String(retrievedData));
            } else {
                System.out.println("Value not found for key: " + key);
            }


        } catch (IOException e) {
            // 处理异常
            e.printStackTrace();
        } finally {
            // 关闭数据库
            if (db != null) {
                try {
                    db.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}