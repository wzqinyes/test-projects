package com.example.mongodb.config;


import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

@Configuration
public class MongoConfigure {

    private String host = "192.168.1.136";
    private String database = "device";
    private int port = 27017;

    @Bean(name = "myMongoTemplate")
    @Primary
    public MongoTemplate getMongoTemplate() throws Exception {
        return new MongoTemplate(mongoDbFactory());
    }

    private MongoDbFactory mongoDbFactory() throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(host, port), database);
    }

    @Bean(name = "myGridFsTemplate")
    @Primary
    public GridFsTemplate getGridFsTemplate(MongoTemplate mongoTemplate) throws Exception {
        return new GridFsTemplate(mongoDbFactory(), mongoTemplate.getConverter());
    }

}
