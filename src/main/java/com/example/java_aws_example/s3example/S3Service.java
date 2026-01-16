package com.example.java_aws_example.s3example;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Client s3Client;

    @Value("${spring.application.s3.bucket-name}")
    private String bucketName;

    public List<String> listObjects(String bucketName) {
        List<String> fileNames = new ArrayList<>();
        ListObjectsV2Iterable objectsV2Iterable = s3Client.listObjectsV2Paginator(
                ListObjectsV2Request.builder().bucket(bucketName).build()
        );
        objectsV2Iterable.stream().iterator().forEachRemaining(s3ObjectsV2ResponsePage ->
            s3ObjectsV2ResponsePage.contents().forEach(s3Object -> fileNames.add(s3Object.key()))
        );

        return fileNames;
    }

    public void putFile(String key, File file) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));
    }

    public String getFile(String key) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        ResponseInputStream<GetObjectResponse> ris = s3Client.getObject(getObjectRequest);
        return new String(ris.readAllBytes());
    }
}
