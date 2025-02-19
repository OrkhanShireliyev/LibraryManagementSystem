package com.company.librarymanagementsystem.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String uploadFile(String bucketName, String fileName, InputStream inputStream, long fileSize) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(fileSize);
            amazonS3.putObject(bucketName, fileName, inputStream, metadata);

            return amazonS3.getUrl(bucketName, fileName).toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }
}
