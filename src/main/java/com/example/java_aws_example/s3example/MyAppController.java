package com.example.java_aws_example.s3example;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/java-aws-example/s3")
@RequiredArgsConstructor
public class MyAppController {

    public final S3Service s3Service;

    @PostMapping
    public ResponseEntity<String> uploadFile() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:employees.json");
        s3Service.putFile("employees/employees.json", file);

        return ResponseEntity.ok("File uploaded successfully");
    }

    @GetMapping("/list")
    public ResponseEntity<List<String>> getFileList() throws IOException {
        List<String> data = s3Service.listObjects("employees");
        return ResponseEntity.ok(data);
    }

    @GetMapping
    public ResponseEntity<String> getFile() throws IOException {
        String data = s3Service.getFile("employees/employees.json");
        return ResponseEntity.ok(data);
    }

}
