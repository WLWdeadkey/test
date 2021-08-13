package com.example.demo.controller;

import io.minio.MinioClient;
import io.minio.policy.PolicyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController("/minioDemo")
public class MinioController {private static final Logger LOGGER = LoggerFactory.getLogger(MinioController.class);

    @Value("${minio.endpoint}")
    private  String ENDPOINT;
    @Value("${minio.bucketName}")
    private  String BUCKETNAME;
    @Value("${minio.accessKey}")
    private  String ACCESSKEY;
    @Value("${minio.secretKey}")
    private  String SECRETKEY;


    @CrossOrigin

    //文件创建
    @PostMapping()
    public String upload(MultipartFile file) {
        String s=null;
        try {
            System.out.println(1111);
            System.out.println(file);
            MinioClient minioClient = new MinioClient(ENDPOINT, ACCESSKEY, SECRETKEY);
            System.out.println(1111);
            //存入bucket不存在则创建，并设置为只读
            if (!minioClient.bucketExists(BUCKETNAME)) {
                System.out.println(1111);
                minioClient.makeBucket(BUCKETNAME);
                minioClient.setBucketPolicy(BUCKETNAME, "*.*", PolicyType.READ_ONLY);
                System.out.println(1111);
            }
            System.out.println(1111);
            String filename = file.getOriginalFilename();
            System.out.println(filename);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            // 文件存储的目录结构
            String objectName = sdf.format(new Date()) + "/" + filename;
            System.out.println(objectName);
            // 存储文件
            System.out.println(file.getInputStream());
            System.out.println(file.getContentType());
            minioClient.putObject(BUCKETNAME, objectName, file.getInputStream(), file.getContentType());
            LOGGER.info("文件上传成功!");
            s=ENDPOINT + "/" + BUCKETNAME + "/" + objectName;
        } catch (Exception e) {
            LOGGER.info("上传发生错误: {}！", e.getMessage());
        }
        return s;
    }
    //文件删除
    @DeleteMapping
    public String delete(String name) {
        try {
            MinioClient minioClient = new MinioClient(ENDPOINT, ACCESSKEY, SECRETKEY);
            System.out.println(name);
            System.out.println(BUCKETNAME);
            minioClient.removeObject(BUCKETNAME, name);
        } catch (Exception e) {
            return "删除失败"+e.getMessage();
        }
        return "删除成功";
    }

}
