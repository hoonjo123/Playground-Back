package com.swyp.playground.common.S3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import org.springframework.beans.factory.annotation.Value;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName;

    // @Value 어노테이션으로 프로퍼티 주입
    public S3Service(
        @Value("${spring.s3.bucket-name}") String bucketName,
        @Value("${spring.s3.access-key}") String accessKey,
        @Value("${spring.s3.secret-key}") String secretKey,
        @Value("${spring.s3.region}") String region
    ) {
        this.bucketName = bucketName;
        this.s3Client = S3Client.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKey, secretKey)
            ))
            .build();
    }

    public String uploadFile(String key, byte[] content) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(content));
        return String.format("https://%s.%s/%s", bucketName, "s3.amazonaws.com", key);
    }
}

