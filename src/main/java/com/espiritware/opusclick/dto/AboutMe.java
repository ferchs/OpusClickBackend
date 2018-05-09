package com.espiritware.opusclick.dto;

import lombok.Getter;

@Getter
public final class AboutMe {
	
	private final String aboutMe;

	public AboutMe(final String newAboutMe) {
		this.aboutMe = newAboutMe;
	}

	@Override
	public String toString() {
		return this.aboutMe;
	}
}
