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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import com.amazonaws.AmazonClientException;
import com.espiritware.opusclick.annotations.DTO;
import com.espiritware.opusclick.dto.ReviewDto;
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
		String base64Image = base64.split(",")[1];
		String metadata= base64.split(",")[0];
		String extension = "."+metadata.substring(metadata.indexOf("/")+1, metadata.indexOf(";"));
		byte[] decodedFile = Base64.getDecoder().decode(base64Image.getBytes(StandardCharsets.UTF_8));
		File file = File.createTempFile("tmp",extension, new File(TMP_FOLDER));
		file.deleteOnExit();
		Files.write(file.toPath(), decodedFile);
		return file;
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
			provider.getGlobalRating().setGlobalRecommend(calculateGlobalRecommend(
					provider.getGlobalRating().getGlobalRecommend(), provider.getGlobalRating().getWorksDone(), 1));
		} else {
			provider.getGlobalRating().setGlobalRecommend(calculateGlobalRecommend(
					provider.getGlobalRating().getGlobalRecommend(), provider.getGlobalRating().getWorksDone(), 0));
		}
	}

	private double calculateGlobalRecommend(double globalRecommend, int worksNumber, int recommend) {
		return ((worksNumber - 1) * globalRecommend + recommend) / worksNumber;
	}
}
