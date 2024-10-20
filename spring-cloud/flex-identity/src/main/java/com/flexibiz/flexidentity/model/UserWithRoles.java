package com.flexibiz.flexidentity.model;

import com.flexibiz.flexidentity.entity.User;
import com.flexibiz.flexidentity.entity.UserAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

public class UserWithRoles extends User implements UserDetails {

    private final List<UserAuthority> authorities;

    public UserWithRoles(User user, List<UserAuthority> authorities) {
        super(user.getId(), user.getUsername(), user.getPassword(), user.isExpired(), user.isLocked());
        this.authorities = authorities;
    }

    @Override
    public Collection<UserAuthority> getAuthorities() {
        return authorities;
    }
}
