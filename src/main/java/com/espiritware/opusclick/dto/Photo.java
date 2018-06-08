package com.espiritware.opusclick.dto;

import lombok.Getter;

@Getter
public final class Photo {
	
	private final String photo;

	public Photo(final String newPhoto) {
		this.photo = newPhoto;
	}

	@Override
	public String toString() {
		return this.photo;
	}
}
