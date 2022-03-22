package com.elfLab.bbgg.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.elfLab.bbgg.model.User;
import com.elfLab.bbgg.mapper.UserMapper;

@Service
public class PrincipalDetailService implements UserDetailsService{

	@Autowired
	private UserMapper userMapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User principal = userMapper.findByUsername(username).get();

		return new PrincipalDetail(principal);
	}
		
	
}
