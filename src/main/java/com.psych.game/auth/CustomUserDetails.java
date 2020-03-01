package com.psych.game.auth;

import com.psych.game.models.Role;
import com.psych.game.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


/*
Notes:
LazyIntializationException : -  The collection is loaded in a lazy manner as a placeholder (kind of like a
base address of array in c) . If you try to access more of this placeholder and session of DB is no longer there
we get this exception
 */
//UserDetails asks you to implement UserDetails functions
public class CustomUserDetails extends User implements UserDetails {

    //Basic delegation to User abstract class

    public CustomUserDetails(User user) {
        super(user);
    }

    //GrantedAuthority -> a sort of string
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles = super.getRoles();
        /*
        This provides a sort of groupt control thing
         */
        List<GrantedAuthority> authorities = new ArrayList<>();
        for(Role role: roles)
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName())); //Tells which role is granted in format "ROLE_"+role_name
        return authorities;

    }

    @Override
    public String getPassword() {
        return super.getSaltedHashedPassword();
        //BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //return encoder.encode(super.getSaltedHashedPassword());
    }

    @Override
    public String getUsername() {
        return super.getEmail();
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