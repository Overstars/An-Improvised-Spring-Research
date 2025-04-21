package com.straycat.config;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.File;
import java.io.IOException;

@Configuration
public class LevelDBConfig {

    @Value("${leveldb.path}")
    private String levelDbPath;

    @Value("${leveldb.path.visitLog}")
    private String visitLogDbPath;

//    @Bean
//    public DB levelDB() throws IOException {
//        Options options = new Options();
//        options.createIfMissing(true);
//        return Iq80DBFactory.factory.open(new File(levelDbPath), options);
//    }

    // 文件元数据库
    @Primary
    @Bean(name = "fileLogDB")
    public DB fileDB() throws IOException {
        Options options = new Options().createIfMissing(true);
        return Iq80DBFactory.factory.open(new File(levelDbPath), options);
    }
    // 访问日志数据库
    @Bean(name = "visitLogDB")
    public DB logDB() throws IOException {
        Options options = new Options().createIfMissing(true);
        return Iq80DBFactory.factory.open(new File(visitLogDbPath), options);
    }


}
