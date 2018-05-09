package com.espiritware.opusclick.dto;

import lombok.Getter;

@Getter
public final class Phone {
	
	private final String phone;

	public Phone(final String newPhone) {
		this.phone = newPhone;
	}

	@Override
	public String toString() {
		return this.phone;
	}
}
