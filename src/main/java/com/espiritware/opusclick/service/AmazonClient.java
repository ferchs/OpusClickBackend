package com.espiritware.opusclick.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class AmazonClient {
	
	@Autowired
    private AmazonS3 s3Client;

    @Value("${amazonS3Properties.endpointUrlBase}")
    private String endpointUrl;
    @Value("${amazonS3Properties.bucketName}")
    private String bucketName;
       
	public String uploadFile(String folder, MultipartFile multipartFile) throws AmazonServiceException,AmazonClientException, IOException {
		String fileUrl = "";
		File file = convertMultiPartToFile(multipartFile);
		String fileName = generateFileName(folder,multipartFile);
		fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
		uploadFileTos3bucket(fileName, file);
		file.delete();
		return fileUrl;
	}
	
	public String uploadFile(String folder, File file) throws AmazonServiceException,AmazonClientException, IOException {
		String fileName = generateFileName(folder,file);
		String fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
		uploadFileTos3bucket(fileName, file);
		file.delete();
		return fileUrl;
	}

	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(new Date().getTime() + file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}
	
	private String generateFileName(String folder,File file) {

		return folder + new Date().getTime() + "-" + file.getName();
	}

	private String generateFileName(String folder,MultipartFile multiPart) {
//		Date date= new Date();
//		SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
//		String dateName= dateFormat.format(date);
//		String extension= multiPart.getContentType().split("/")[1];
//		String filename= String.valueOf(idProvider) + "-providerPhoto-" + dateName + "." + extension;
//		provider.setPhoto(PROVIDERS_UPLOADED_FOLDER + filename);
//		byte[] photoBytes= multipartFile.getBytes();
//		Path path= Paths.get(PROVIDERS_UPLOADED_FOLDER + filename);
//		Files.write(path, photoBytes);
		return folder + new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
	}

	public void uploadFileTos3bucket(String fileName, File file) throws AmazonServiceException,AmazonClientException {
		s3Client.putObject(
				new PutObjectRequest(bucketName, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
	}

	public String deleteFileFromS3Bucket(String folder, String fileUrl) throws AmazonServiceException, AmazonClientException {
		String fileName = folder + fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
		s3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
		return "Successfully deleted";
	}
}
