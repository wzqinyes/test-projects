package com.example.mongodb;

import com.example.mongodb.domain.UserTTT;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MongodbApplication.class)
public class MongodbApplicationTests {

    @Autowired
    @Qualifier("myMongoTemplate")
    private MongoTemplate mongoTemplate;

    @Autowired
    @Qualifier("myGridFsTemplate")
    private GridFsTemplate gridFsTemplate;

    @Test
    public void insert() {

        UserTTT user = new UserTTT();
        user.setId(1);
        user.setName("凌康");
        user.setSex("男");
        UserTTT u1 = mongoTemplate.insert(user);
        System.out.println(u1.toString());

        user = new UserTTT();
        user.setId(2);
        user.setName("李白");
        user.setSex("男");
        UserTTT u2 = mongoTemplate.insert(user);
        System.out.println(u2.toString());
    }

    @Test
    public void find() throws Exception {
        UserTTT user = new UserTTT();
        List<UserTTT> users = mongoTemplate.findAll(UserTTT.class);
        mongoTemplate.find(new Query().with(Sort.by(Sort.Order.asc("name"), Sort.Order.desc("sex"))), UserTTT.class, "UserTTT");
        users.forEach(System.out::println);
    }

    @Test
    public void findUserByUserName() {
        String userName = "李白";
//        Query query = new Query(Criteria.where("name").is(userName));
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(userName));
        UserTTT user = mongoTemplate.findOne(query, UserTTT.class);
//        List<UserTTT> list = mongoTemplate.find(query, UserTTT.class);
        System.out.println(user);

        query = new Query()
            .addCriteria(Criteria.where("deviceStatus").is("ONLINE"))
            .addCriteria(Criteria.where("entityStatus").is("NOMAL"));
        List<Map> device = mongoTemplate.find(query, Map.class, "Device");
        device.forEach(map -> {
            map.forEach((k, v) -> {
                System.out.println(k + " : " + v);
            });
        });
    }

    @Test
    public void update() throws Exception {
        Query query = new Query(Criteria.where("id").is(1));
        Update update = new Update().set("name", "张三").set("sex", "女");
        UpdateResult result = mongoTemplate.updateMulti(query, update, UserTTT.class);
        if (result != null) {
            System.out.println(result.getMatchedCount());
        }
    }

    @Test
    public void delete() throws Exception {
        //删除找到的记录
        //DeleteResult result = mongoTemplate.remove(new Query(Criteria.where("id").is(1)), UserTTT.class);
        //System.out.println(result.getDeletedCount());

        //删除全部
        DeleteResult all = mongoTemplate.remove(UserTTT.class).all();
        System.out.println(all.getDeletedCount());

    }

    @Test
    public void storeFile() throws Exception {
        Resource resource = new ClassPathResource("static/music.mp3");
        InputStream inputStream = resource.getInputStream();
        ObjectId objectId = gridFsTemplate.store(inputStream, "audio-of-12377", "mp3");
        inputStream.close();
        System.out.println(objectId);
    }

    @Test
    public void deleteFile() throws Exception {
        gridFsTemplate.delete(new Query(Criteria.where("filename").is("123.mp3")));
    }


    @Test
    public void findFilesInGridFs() throws IOException {
        GridFSFile fsFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is("5e9d0c4c6aa94a0740d25629")));
        if (fsFile != null) {
            System.out.println(fsFile.getFilename());
            System.out.println(fsFile.getObjectId());
        }

        fsFile = gridFsTemplate.findOne(new Query(Criteria.where("filename").is("12377.mp31")));
        if (fsFile != null) {
            System.out.println(fsFile.getFilename());
            GridFsResource resource = gridFsTemplate.getResource(fsFile);
            InputStream inputStream = resource.getInputStream();

            File f = new File("E:/", fsFile.getFilename());
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(f);
            byte[] b = new byte[8 * 1024];  //8k
            int c;
            while ((c = inputStream.read(b)) > 0) {
                out.write(b, 0, c);
            }
            out.close();
            inputStream.close();
        }

        GridFSFindIterable fsFiles = gridFsTemplate.find(new Query(Criteria.where("filename").is("123.mp3")));
        for (GridFSFile file : fsFiles) {
            System.out.println(file.getObjectId());
        }
        System.out.println(fsFile.getFilename());
    }

    @Test
    public void resources() throws Exception {
        GridFsResource[] resources = gridFsTemplate.getResources("*");
        for (GridFsResource resource : resources) {
            System.out.println(resource.contentLength());
        }
    }
}
