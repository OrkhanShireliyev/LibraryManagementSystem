package com.company.librarymanagementsystem.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfig {
        @Value("${cloud.aws.credentials.access-key}")
        private String awsAccessKey;

        @Value("${cloud.aws.credentials.secret-key}")
        private String awsSecretKey;

        @Value("${cloud.aws.region.static}")
        private String region;

        @Bean
        public AmazonS3 amazonS3() {
            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
            return AmazonS3ClientBuilder.standard()
                    .withRegion(Regions.fromName(region))
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .build();
        }
}
