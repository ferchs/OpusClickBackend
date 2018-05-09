package com.espiritware.opusclick.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import com.espiritware.opusclick.dto.ProviderRegistrationDto;
import com.espiritware.opusclick.dto.UserRegistrationDto;
import com.espiritware.opusclick.annotations.PasswordMatches;
import com.espiritware.opusclick.dto.PasswordDto;


public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(final PasswordMatches constraintAnnotation) {
        //
    }

    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
//		if (obj instanceof UserRegistrationDto) {
//			UserRegistrationDto user = (UserRegistrationDto) obj;
//			return user.getPassword().equals(user.getMatchingPassword());
//		} else if(obj instanceof ProviderRegistrationDto){
//			ProviderRegistrationDto provider = (ProviderRegistrationDto) obj;
//			return provider.getPassword().equals(provider.getMatchingPassword());
//		}else if(obj instanceof PasswordDto){
//			PasswordDto password = (PasswordDto) obj;
//			return password.getPassword().equals(password.getMatchingPassword());
//		}
//		else {
//			return false;
//		}
    	
    	return true;
    }
}
