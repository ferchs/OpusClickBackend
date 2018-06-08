package com.espiritware.opusclick.dto;

import lombok.Getter;

@Getter
public final class OpusCoin {
	
	private final int opusCoinsAmount;

	public OpusCoin(final int newOpusCoins) {
		this.opusCoinsAmount = newOpusCoins;
	}

	@Override
	public String toString() {
		return this.opusCoinsAmount+"";
	}
}
