package com.espiritware.opusclick.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.config.Configuration.AccessLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.espiritware.opusclick.dto.ProviderGetContractReviewDto;
import com.espiritware.opusclick.model.Contract;

@Configuration
public class ModelMapperConfiguration {
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration()
		  .setFieldMatchingEnabled(true)
		  .setFieldAccessLevel(AccessLevel.PRIVATE);
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		modelMapper.addMappings(new PropertyMap<Contract, ProviderGetContractReviewDto>() {
			@Override
			protected void configure() {
				map().setContractName(source.getName());
				map().setDate(source.getStartDate());
				map().setUserName(source.getWork().getUser().getAccount().getName());
				map().setUserLastname(source.getWork().getUser().getAccount().getLastname());
				map().setReviewSatisfactionLevel(source.getWork().getReview().getSatisfactionLevel());
				map().setReviewComment(source.getWork().getReview().getComment());
				map().setReviewImage(source.getWork().getReview().getImage());
				map().setReviewId(source.getWork().getReview().getId());
			}
		});
		return modelMapper;
	}
}
