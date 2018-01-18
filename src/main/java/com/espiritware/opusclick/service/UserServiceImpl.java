package com.espiritware.opusclick.service;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.espiritware.opusclick.dao.GenericDao;
import com.espiritware.opusclick.dto.UserDto;
import com.espiritware.opusclick.error.UserAlreadyExistException;
import com.espiritware.opusclick.model.City;
import com.espiritware.opusclick.model.State;
import com.espiritware.opusclick.model.User;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService{

	GenericDao< User > userDao;
	
	@Autowired
	public void setDao( GenericDao< User > daoToSet ){
		userDao = daoToSet;
		userDao.setEntityClass( User.class );
	}
	
	@Override
	public User registerUser(UserDto userDto) {
		if (userExist(userDto.getEmail())) {
            throw new UserAlreadyExistException("Ya existe una cuenta registrada con esta dirección de email: " + userDto.getEmail());
        }
		else {
			User user = new User();
	        user.setEmail(userDto.getEmail());
	        user.setCity(City.valueOf(userDto.getCity()));
	        user.setOpusCoins(10);
	        user.setState(State.WAITING_EMAIL_CONFIRMATION);
	        userDao.create(user);
			return user;
		}
	}

	
	public boolean userExist(String email) {
        return userDao.findById(email) != null;
    }

//	@Override
//	public void deleteUserById(String email) {
//		_userDao.deleteById(email);
//	}

	@Override
	public void updateUser(User user) {
		userDao.update(user);
	}

//	@Override
//	public List<User> findAllUsers() {
//		return _userDao.findAll();
//	}

	@Override
	public User findUserById(String email) {
		return userDao.findById(email);
	}
	
	
	@Override
	public State getUserState(String email) {
		return userDao.findById(email).getState();
	}
	
	@Override
	public void setUserState(String email, State state) {
		User user= userDao.findById(email);
		user.setState(state);
		userDao.update(user);
	}
}
