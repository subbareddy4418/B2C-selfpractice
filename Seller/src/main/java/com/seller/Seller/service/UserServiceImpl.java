package com.seller.Seller.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seller.Seller.model.User;
import com.seller.Seller.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService{
	
	private final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserRepository userrepo;

	@Override
	public User addUser(User user) {
	
		return userrepo.save(user);
	}

	@Override
	public User getUserByEmailAndStatus(String emailId, String status) {
		try {
	        return userrepo.findByEmailIdAndStatus(emailId, status);
	    } catch (Exception e) {
	        LOG.error("An error occurred while retrieving user by email and status: emailId={}, status={}", emailId, status, e);
	        throw new RuntimeException("Error occurred while fetching user by email and status", e);
	    }
	}
	

	@Override
	public User getUserByEmailid(String emailId) {
		return userrepo.findByEmailId(emailId);
	}

	@Override
	public List<User> getUserByRole(String role) {
		return userrepo.findByRole(role);
	}

	@Override
	public User getUserById(int userId) {

		Optional<User> optionalUser = this.userrepo.findById(userId);

		if (optionalUser.isPresent()) {
			return optionalUser.get();
		} else {
			return null;
		}
	}
	

	@Override
	public User getUserByEmailIdAndRoleAndStatus(String emailId, String role, String status) {
		return this.userrepo.findByEmailIdAndStatus(emailId, status);
	}
	

	@Override
	public List<User> getUserByRoleAndStatus(String role, String status) {
		return this.userrepo.findByRoleAndStatus(role, status);
	}


}
