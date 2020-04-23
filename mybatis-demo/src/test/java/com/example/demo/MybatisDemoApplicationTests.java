package com.example.demo;

import com.example.demo.entity.TRqdjScan;
import com.example.demo.service.TRqdjScanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = MybatisDemoApplication.class)
class MybatisDemoApplicationTests {

    @Autowired
    private TRqdjScanService service;

    @Test
    void contextLoads() {
        TRqdjScan obj = new TRqdjScan();
        obj.setWorker("张三");
        TRqdjScan entity = service.insert(obj);
        System.out.println(entity);
    }

}
