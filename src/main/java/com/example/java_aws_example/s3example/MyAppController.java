package com.example.java_aws_example.s3example;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/java-aws-example/s3")
@RequiredArgsConstructor
@Slf4j
public class MyAppController {

    public final S3Service s3Service;

    @PostMapping
    public ResponseEntity<String> uploadFile() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:employees.json");
        s3Service.putFile("employees/employees" + System.currentTimeMillis() + ".json", file);

        return ResponseEntity.ok("File uploaded successfully");
    }

    @GetMapping("/list")
    public ResponseEntity<List<String>> getFileList(@RequestParam("prefix") String prefix) throws IOException {
        List<String> data = s3Service.listObjects(prefix);
        return ResponseEntity.ok(data);
    }

    @GetMapping
    public ResponseEntity<String> getFile(@RequestParam("fileName") String fileName) throws IOException {
        if (StringUtils.hasText(fileName)) {
            try {
                String data = s3Service.getFile(fileName);
                return ResponseEntity.ok(data);
            } catch (S3Exception ex) {
                log.error("Error occurred while fetching file from S3: {}", ex.awsErrorDetails().errorMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch the file from S3: " + fileName);
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file name provided");
        }

    }

}
