package com.espiritware.opusclick.service;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.espiritware.opusclick.dao.GenericDao;
import com.espiritware.opusclick.error.UserAlreadyExistException;
import com.espiritware.opusclick.model.State;
import com.espiritware.opusclick.model.User;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService{

	GenericDao< User,String > userDao;
	
	@Autowired
	public void setDao( GenericDao< User,String > daoToSet ){
		userDao = daoToSet;
		userDao.setEntityClass( User.class );
	}
	
	@Override
	public void createUser(User user) {
		userDao.create(user);
	}
	
	@Override
	public User createUser(String id, String identificationNumber, String phoneNumber, int opusCoins, State state) {
		if (userExist(id)) {
            throw new UserAlreadyExistException("Ya existe una cuenta registrada con esta dirección de email: " + id);
        }
		else {
			User user = new User();
			user.setUserId(id);
			user.setIdentificationNumber(identificationNumber);
			user.setPhone(phoneNumber);
	        user.setOpusCoins(opusCoins);
	        user.setState(state);
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
