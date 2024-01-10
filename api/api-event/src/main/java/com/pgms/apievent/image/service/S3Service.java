package com.pgms.apievent.image.service;

import static com.pgms.apievent.exception.EventErrorCode.*;

import java.io.IOException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.pgms.apievent.exception.EventException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3Service {

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;
	private final AmazonS3Client amazonS3Client;

	public URL upload(MultipartFile file, String filename) {
		try {
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType(file.getContentType());
			metadata.setContentLength(file.getSize());
			amazonS3Client.putObject(bucket, filename, file.getInputStream(), metadata);
			return amazonS3Client.getUrl(bucket, filename);
		} catch (IOException e) {
			throw new EventException(S3_UPLOAD_FAILED_EXCEPTION);
		}
	}

	public void delete(String fileName) {
		DeleteObjectRequest request = new DeleteObjectRequest(bucket, fileName);
		amazonS3Client.deleteObject(request);
	}
}
