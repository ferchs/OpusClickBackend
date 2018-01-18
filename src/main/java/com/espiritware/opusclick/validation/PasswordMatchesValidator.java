package com.espiritware.opusclick.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import com.espiritware.opusclick.dto.ProviderDto;
import com.espiritware.opusclick.dto.UserDto;
import com.espiritware.opusclick.dto.PasswordDto;;


public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(final PasswordMatches constraintAnnotation) {
        //
    }

    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
		if (obj instanceof UserDto) {
			UserDto user = (UserDto) obj;
			return user.getPassword().equals(user.getMatchingPassword());
		} else if(obj instanceof ProviderDto){
			ProviderDto provider = (ProviderDto) obj;
			return provider.getPassword().equals(provider.getMatchingPassword());
		}else if(obj instanceof PasswordDto){
			PasswordDto password = (PasswordDto) obj;
			return password.getPassword().equals(password.getMatchingPassword());
		}
		else {
			return false;
		}
    }
}
