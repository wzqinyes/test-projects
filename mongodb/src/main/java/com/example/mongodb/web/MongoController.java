package com.example.mongodb.web;

import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Controller
@RequestMapping("/mongo")
public class MongoController {


    @Autowired
    @Qualifier("myGridFsTemplate")
    private GridFsTemplate gridFsTemplate;

    @RequestMapping(value = "/mp3/{fileName}", method = RequestMethod.GET)
    public void getMp3(@PathVariable String fileName, HttpServletResponse response) {
        GridFSFile fsFile = gridFsTemplate.findOne(new Query(Criteria.where("filename").is(fileName)));
        if (fsFile == null) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
            response.setContentType("application/json;charset=UTF-8");
            try {
                response.getWriter().write("{\"code\":0, \"msg\":\"error\"}");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        GridFsResource resource = gridFsTemplate.getResource(fsFile);
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("audio/mpeg;charset=utf-8");
        response.setContentLengthLong(fsFile.getLength());
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = resource.getInputStream();
            outputStream = response.getOutputStream();
            byte[] b = new byte[8 * 1024];  //8k
            int c;
            while ( (c = inputStream.read(b)) > 0) {
                outputStream.write(b, 0, c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStream(inputStream);
            closeStream(outputStream);
        }
    }

    private void closeStream(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
