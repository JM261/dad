package com.jm.dad.config.auth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.jm.dad.model.entity.User;

public class CustomUserDetails implements UserDetails, Serializable {
	
    private static final long serialVersionUID = 1L;

	private User user;
	
	public CustomUserDetails(User user) {
		this.user = user;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		
		authorities.add(new GrantedAuthority() {
			
			@Override
			public String getAuthority() {
				return "ROLE_" + user.getRole();
			}
			
		});
		
		return authorities;
	}

	@Override
	public String getPassword() {
		return user.getPwd();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
