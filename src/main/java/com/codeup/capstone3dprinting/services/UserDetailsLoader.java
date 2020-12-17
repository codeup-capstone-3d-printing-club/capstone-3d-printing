package com.codeup.capstone3dprinting.services;

import com.codeup.capstone3dprinting.models.User;
import com.codeup.capstone3dprinting.models.UserWithRoles;
import com.codeup.capstone3dprinting.repos.Users;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsLoader implements UserDetailsService {
    private final Users users;

    public UserDetailsLoader(Users users) {
        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = users.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("No user found for " + username);
        }else if(user.isActive() == false){
            throw new  NullPointerException("This user is not active: " + username);
        }
        return new UserWithRoles(user);
    }
}
