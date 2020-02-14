package cn.wu;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Arrays;

public class MongoDBTestRun {

    public static void main(String[] args){

        //连接服务
        MongoClient mongoClient = new MongoClient("192.168.153.130", 27017);

        //连接数据库
        MongoDatabase db = mongoClient.getDatabase("runoob");

        MongoCollection<Document> col = db.getCollection("col");

        Document d1 = new Document().append("title", "java").append("name", "wangwu").append("likes", "30");
        Document d2 = new Document().append("title", "c++").append("name", "zhaoliu").append("likes", "20");
        col.insertMany(Arrays.asList(d1, d2));

        mongoClient.close();
    }
}
