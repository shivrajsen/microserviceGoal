package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.model.User;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class UserInfoDetails implements UserDetails  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private User user;
	
	 public UserInfoDetails(User user) {
	        this.user = user;
	    }
 
	 @Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
		 return Arrays.stream(user.getRole().split(","))
		            .map(role -> new SimpleGrantedAuthority(role))
		            .collect(Collectors.toList());
		}

		@Override
		public String getPassword() {
			return user.getPassword();
		}

		@Override
		public String getUsername() {
			return user.getEmail();
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
