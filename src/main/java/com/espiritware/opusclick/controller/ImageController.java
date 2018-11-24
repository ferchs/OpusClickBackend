package com.espiritware.opusclick.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.espiritware.opusclick.service.AmazonClient;

@Controller
@RequestMapping("/v1")
public class ImageController {
	
	private static final String TEST_IMAGES_FOLDER="test-images/";

	@Autowired
	private AmazonClient amazonClient;

	@RequestMapping(value="/images", method = RequestMethod.POST, headers ="content-type=multipart/form-data")
	@Transactional
	public ResponseEntity<?> uploadProviderImage(@RequestParam("file") MultipartFile multipartFile) {
		try {
			String fileUrl = amazonClient.uploadFile(TEST_IMAGES_FOLDER,multipartFile);
			return new ResponseEntity<String>(fileUrl, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
