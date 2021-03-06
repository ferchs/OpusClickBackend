package com.espiritware.opusclick.controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.amazonaws.AmazonClientException;
import com.espiritware.opusclick.annotations.DTO;
import com.espiritware.opusclick.dto.ProviderUpdateDto;
import com.espiritware.opusclick.dto.ReviewDto;
import com.espiritware.opusclick.dto.ReviewImageUpdateDto;
import com.espiritware.opusclick.error.CustomErrorType;
import com.espiritware.opusclick.event.Publisher;
import com.espiritware.opusclick.model.Milestone;
import com.espiritware.opusclick.model.Provider;
import com.espiritware.opusclick.model.Review;
import com.espiritware.opusclick.model.State;
import com.espiritware.opusclick.model.Work;
import com.espiritware.opusclick.service.AmazonClient;
import com.espiritware.opusclick.service.ProviderService;
import com.espiritware.opusclick.service.ReviewService;
import com.espiritware.opusclick.service.WorkService;

@Controller
@RequestMapping("/v1")
public class ReviewController {
	
	private static final String REVIEW_IMAGES_FOLDER="review-images/";
	
	private static final String TMP_FOLDER="tmp/";

	private static final String PROVIDER_IMAGES_FOLDER="provider-profile-images/";


	@Autowired
	private WorkService workService;
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private ProviderService providerService;
	
	@Autowired
	private AmazonClient amazonClient;
	
	@Autowired
	private Publisher publisher;
	
	@RequestMapping(value = "/reviews", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> makeReview(@Valid @DTO(ReviewDto.class) Review review,
			@RequestParam(value = "work", required = true) String workId, UriComponentsBuilder uriComponentsBuilder,
			final HttpServletRequest request) {
		updateImage(review);
		reviewService.createReview(review);
		Work work = workService.findWorkById(Integer.parseInt(workId));
		work.setState(State.QUALIFIED);
		work.setHistoryStateChanges(work.getHistoryStateChanges() + work.getState().state() + ",");
		work.getContract().setState(State.QUALIFIED);
		work.getContract().setHistoryStateChanges(work.getContract().getHistoryStateChanges() + work.getContract().getState()+ ",");
		updateMilestonesState(work.getContract().getMilestones(),State.QUALIFIED);
		updateProviderGlobalRating(work.getProvider(), review);
		work.setReview(review);
		workService.updateWork(work);
		publisher.publishReviewEvent(work);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	private void updateImage(Review review) {
		if(review.getImage()!=null) {
			try {
				String url = amazonClient.uploadFile(REVIEW_IMAGES_FOLDER, createTmpFile(review.getImage()));
				review.setImage(url);
			} catch (AmazonClientException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	private File createTmpFile(String base64) throws IOException {
		createTmpFolder();
		String base64Image = base64.split(",")[1];
		String metadata= base64.split(",")[0];
		String extension = "."+metadata.substring(metadata.indexOf("/")+1, metadata.indexOf(";"));
		byte[] decodedFile = Base64.getDecoder().decode(base64Image.getBytes(StandardCharsets.UTF_8));
		File file = File.createTempFile("tmp",extension, new File(TMP_FOLDER));
		file.deleteOnExit();
		Files.write(file.toPath(), decodedFile);
		return file;
	}
	
	private void createTmpFolder() {
		File file = new File("tmp");
        if (!file.exists()) {
        	file.mkdir();
        }
	}
	
	
	private void updateMilestonesState(Set<Milestone> milestones,State state) {
		for (Milestone milestone : milestones) {
			milestone.setState(state);
			milestone.setHistoryStateChanges(milestone.getHistoryStateChanges()+milestone.getState().state()+",");
		}
	}

	private void updateProviderGlobalRating(Provider provider, Review review) {
		if (provider.getGlobalRating().getWorksDone() == 0) {
			provider.getGlobalRating().setGlobalSatisfactionLevel(review.getSatisfactionLevel());
			if (review.isRecommend()) {
				provider.getGlobalRating().setGlobalRecommend(100);
			} else {
				provider.getGlobalRating().setGlobalRecommend(0);
			}
			provider.getGlobalRating().setWorksDone(1);
			providerService.updateProvider(provider);
		} else {
			provider.getGlobalRating().setWorksDone(provider.getGlobalRating().getWorksDone() + 1);
			updateGlobalRecommend(provider, review);
			updateGlobalSatisfactionLevel(provider, review);
			providerService.updateProvider(provider);
		}
	}

	private void updateGlobalSatisfactionLevel(Provider provider, Review review) {
		provider.getGlobalRating().setGlobalSatisfactionLevel(
				calculateGlobalSatisfactionLevel(provider.getGlobalRating().getGlobalSatisfactionLevel(),
						provider.getGlobalRating().getWorksDone(), review.getSatisfactionLevel()));
	}

	private double calculateGlobalSatisfactionLevel(double globalSatisfactionLevel, int worksNumber,
			double newSatisfactionLevel) {
		return (globalSatisfactionLevel * (worksNumber - 1) + newSatisfactionLevel) / worksNumber;
	}

	private void updateGlobalRecommend(Provider provider, Review review) {
		if (review.isRecommend()) {
			provider.getGlobalRating().setGlobalRecommend(calculatePositiveGlobalRecommend(
					provider.getGlobalRating().getGlobalRecommend(), provider.getGlobalRating().getWorksDone(), 1));
		} else {
			provider.getGlobalRating().setGlobalRecommend(calculatePositiveGlobalRecommend(
					provider.getGlobalRating().getGlobalRecommend(), provider.getGlobalRating().getWorksDone(), 0));
		}
	}
	
	private double calculatePositiveGlobalRecommend(double globalRecommend, int worksNumber, int recommend) {
		double previousYesAmount=((globalRecommend/100)*(worksNumber-1));
		int previousYsAmount=(int) Math.round(previousYesAmount);
		return ((float)(previousYsAmount+recommend)/worksNumber)*100;
	}
	
	
	@RequestMapping(value = "/reviews/{id}/image", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<?> updateReviewImage(@PathVariable("id") String reviewId, @RequestParam("file") MultipartFile multipartFile,
			UriComponentsBuilder uriComponentsBuilder, final HttpServletRequest request) {
		if (reviewId == null || reviewId.isEmpty()) {
			return new ResponseEntity<>(new CustomErrorType("Please set id_review"), HttpStatus.NO_CONTENT);
		}
		if (multipartFile.isEmpty()) {
			return new ResponseEntity<>(new CustomErrorType("Please select a file to upload"), HttpStatus.NO_CONTENT);
		}
		Review review = reviewService.findReviewById(Integer.parseInt(reviewId));
		if (review == null) {
			return new ResponseEntity<>(new CustomErrorType("Review with id: " + reviewId + " not found"),
					HttpStatus.NOT_FOUND);
		}
		try {
			String fileUrl = amazonClient.uploadFile(PROVIDER_IMAGES_FOLDER,multipartFile);
			review.setImage(fileUrl);
			reviewService.updateReview(review);
			return new ResponseEntity<String>(fileUrl, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(
					new CustomErrorType("Image review with id: " + reviewId + " can't be upload"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
		
}
