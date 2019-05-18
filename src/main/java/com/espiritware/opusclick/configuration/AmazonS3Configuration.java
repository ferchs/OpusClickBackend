package com.espiritware.opusclick.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AmazonS3Configuration {
	
	@Value("${amazonS3Properties.endpointUrlBase}")
	private String endpointUrl;
	@Value("${amazonS3Properties.bucketName}")
	private String bucketName;
	@Value("${amazonS3Properties.region}")
	private String region;
	@Value("${amazonS3Properties.accessKey}")
	private String accessKey;
	@Value("${amazonS3Properties.secretKey}")
	private String secretKey;
	    
	@Bean
	public AmazonS3 initializeAmazonClient() {
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(this.accessKey, this.secretKey);
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
		.withRegion(Regions.fromName(region))
        .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
        .build();
//		AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds))
//				.build();
		return s3Client;
	}
}
