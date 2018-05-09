package com.espiritware.opusclick.dto;

import lombok.Getter;

@Getter
public final class PersonalInformation {
	private final IdentificationNumber identificationNumber;
	private final Phone phoneNumber;
	private final Experience experience;
	private final AboutMe aboutMe;

	public PersonalInformation(final IdentificationNumber identificationNumber, final Phone phoneNumber,
			final Experience experience, final AboutMe aboutMe) {
		this.identificationNumber = identificationNumber;
		this.phoneNumber = phoneNumber;
		this.experience = experience;
		this.aboutMe = aboutMe;
	}
	
	public static class PersonalInformationBuilder {
		private IdentificationNumber nestedIdentificationNumber;
		private Phone nestedPhoneNumber;
		private Experience nestedExperience;
		private AboutMe nestedAboutMe;

		public PersonalInformationBuilder identificationNumber(final IdentificationNumber newIdentificationNumber) {
			this.nestedIdentificationNumber = newIdentificationNumber;
			return this;
		}

		public PersonalInformationBuilder phoneNumber(final Phone newPhoneNumber) {
			this.nestedPhoneNumber = newPhoneNumber;
			return this;
		}
		
		public PersonalInformationBuilder experience(final Experience newExperience) {
			this.nestedExperience = newExperience;
			return this;
		}
		
		public PersonalInformationBuilder aboutMe(final AboutMe newAboutMe) {
			this.nestedAboutMe = newAboutMe;
			return this;
		}
		
		public PersonalInformation createPersonalInformation() {
			return new PersonalInformation(nestedIdentificationNumber, nestedPhoneNumber, nestedExperience,
					nestedAboutMe);
		}
	}
}
