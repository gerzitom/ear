package cz.cvut.fel.ear.tm.service;

import cz.cvut.fel.ear.tm.dto.user.UserDto;
import cz.cvut.fel.ear.tm.dto.user.UserReadDto;
import cz.cvut.fel.ear.tm.exception.AuthenticationException;
import cz.cvut.fel.ear.tm.model.SecurityUser;
import cz.cvut.fel.ear.tm.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User userDto = userService.findByUsername(s);
        if(userDto.isRemoved()) throw new AuthenticationException("User was removed");
        return new SecurityUser(userDto);
    }
}
