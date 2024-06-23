package com.jm.dad.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jm.dad.config.auth.CustomUserDetails;
import com.jm.dad.model.dto.PwdUpdateDto;
import com.jm.dad.model.dto.UserUpdateDto;
import com.jm.dad.model.entity.User;
import com.jm.dad.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;
	
    public void save(User user) {
    	userRepository.save(user);
    }
    
    @Transactional
    public void update(String username, UserUpdateDto userUpdateDto) {
    	
    	User user = userRepository.findByUsername(username);
    	
    	if (user != null) {
    		
    		user.updateUser(userUpdateDto.getName(), userUpdateDto.getNickname(), userUpdateDto.getPhone(), userUpdateDto.getBirthday(), userUpdateDto.getEmail());
    	}
    }

    @Transactional
    public void update(String username, PwdUpdateDto pwdUpdateDto) {
    	
    	User user = userRepository.findByUsername(username);
    	
    	if (user != null) {
    		
    		user.setEncryptedPwd(pwdUpdateDto.getNewPwd());
    	}
    }   
    
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User UserEntity = userRepository.findByUsername(username);
		
		if (UserEntity != null) {
			
			return new CustomUserDetails(UserEntity);
		}
		return null;
	}
	
    public boolean isUsernameUnique(String username) {
    	
        return userRepository.findByUsername(username) == null;
    }
    
	public User findUserByUsername(String username) {
		
		return userRepository.findByUsername(username);
	}

}