package com.example.java_aws_example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;

@Service
public class S3Service {
    private final S3Client s3Client;

    @Value("spring.application.s3.bucket-name")
    private String bucketName;

    public S3Service() {
        // The client is initialized once and reused for all operations
        this.s3Client = S3Client.builder()
                .region(Region.US_EAST_1)
                .build();
    }

    public void putFile(String key, File file) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));
    }

    public void getFile(String key, File destinationFile) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.getObject(getObjectRequest, destinationFile.toPath());
    }

    public void close() {
        s3Client.close();
    }
}
