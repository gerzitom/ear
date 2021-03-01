package cz.cvut.fel.ear.tm.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class SecurityUser extends User implements Serializable {
    private Long id;
    public SecurityUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public SecurityUser(cz.cvut.fel.ear.tm.model.User user){
        super(user.getUsername(), user.getPassword(), List.of(user.getRole()));
        id = user.getId();
    }

    public String getUsername(){
        return super.getUsername();
    }

    public Long getId() {
        return id;
    }

    public Role getRole(){
        Role role = null;
        for(GrantedAuthority authority : super.getAuthorities()){
            role = (Role) authority;
        }
        return role;
    }
}
