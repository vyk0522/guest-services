package com.onejava.security;

import com.onejava.entity.AuthGroup;
import com.onejava.entity.User;
import com.onejava.repository.AuthGroupRepository;
import com.onejava.repository.UserRepository;
import com.onejava.service.GuestServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyHotelUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthGroupRepository authGroupRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(null == user){
            throw new UsernameNotFoundException("Can not find username: " + username);
        }
        List<AuthGroup> authGroups = authGroupRepository.findByUsername(username);

        return new MyHotelUserDetails(user, authGroups);
    }

}
