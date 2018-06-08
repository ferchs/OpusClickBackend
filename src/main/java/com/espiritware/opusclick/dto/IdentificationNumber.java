package com.espiritware.opusclick.dto;

import lombok.Getter;

@Getter
public final class IdentificationNumber {
	
	private final String identificationNumber;

	public IdentificationNumber(final String newIdentificationNumber) {
		this.identificationNumber = newIdentificationNumber;
	}

	@Override
	public String toString() {
		return this.identificationNumber;
	}
}
