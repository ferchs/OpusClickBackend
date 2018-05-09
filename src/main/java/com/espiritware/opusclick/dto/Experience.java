package com.espiritware.opusclick.dto;

import lombok.Getter;

@Getter
public final class Experience {
	
	private final int experienceData;

	public Experience(final int newExperience) {
		this.experienceData = newExperience;
	}

	@Override
	public String toString() {
		return this.experienceData+"";
	}
}
