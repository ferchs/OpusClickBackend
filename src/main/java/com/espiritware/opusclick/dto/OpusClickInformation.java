package com.espiritware.opusclick.dto;

import lombok.Getter;

@Getter
public final class OpusClickInformation {

	private final OpusCoin opuscoins;
	private final WorkDone workdones;

	public OpusClickInformation(final OpusCoin newOpuscoins, final WorkDone newWorkdones) {
		this.opuscoins = newOpuscoins;
		this.workdones = newWorkdones;
	}

	public static class OpusClickInformationBuilder {

		private OpusCoin nestedOpusCoins;
		private WorkDone nestedWorkdone;

		public OpusClickInformationBuilder(final OpusCoin newOpusCoins, final WorkDone newWorkDone) {
			this.nestedOpusCoins = newOpusCoins;
			this.nestedWorkdone = newWorkDone;
		}

		public OpusClickInformationBuilder identificationNumber(final OpusCoin newOpusCoins) {
			this.nestedOpusCoins = newOpusCoins;
			return this;
		}

		public OpusClickInformationBuilder phoneNumber(final WorkDone newWorkDone) {
			this.nestedWorkdone = newWorkDone;
			return this;
		}

		public OpusClickInformation createPersonalInformation() {
			return new OpusClickInformation(nestedOpusCoins, nestedWorkdone);
		}
	}
}
