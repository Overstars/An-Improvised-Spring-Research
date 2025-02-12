package com.straycat.config;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.File;
import java.io.IOException;

@Configuration
public class LevelDBConfig {

    @Value("${leveldb.path}")
    private String levelDbPath;

    @Bean
    public DB levelDB() throws IOException {
        Options options = new Options();
        options.createIfMissing(true);
        return Iq80DBFactory.factory.open(new File(levelDbPath), options);
    }
}
