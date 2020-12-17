package com.onejava.auth;

import com.onejava.entity.AuthGroup;
import com.onejava.entity.User;
import com.onejava.repository.AuthGroupRepository;
import com.onejava.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.security.core.userdetails.User.withUsername;

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

        Set<SimpleGrantedAuthority> grantedAuthorities = new HashSet<>();

        if (null != authGroups){
            authGroups.forEach(authGroup -> grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + authGroup.getAuthGroup())));
        }

        return withUsername(username).password(user.getPassword()).authorities(grantedAuthorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
