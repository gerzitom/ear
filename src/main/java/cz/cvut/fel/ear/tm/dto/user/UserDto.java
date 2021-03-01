package cz.cvut.fel.ear.tm.dto.user;


import cz.cvut.fel.ear.tm.dto.Dto;
import cz.cvut.fel.ear.tm.model.AbstractEntity;
import cz.cvut.fel.ear.tm.model.Role;
import cz.cvut.fel.ear.tm.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Dto<User> {
    private String username;
    private String name;
    private String password;
    private Role role;
    private boolean removed;

    public UserDto(User user) {
        this.name = user.getName();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.role = user.getRole();
        this.removed = user.isRemoved();
    }

    @Override
    public User update(User user) {
        if(this.name != null) user.setName(this.name);
        if(this.username != null) user.setUsername(this.username);
        if(this.password != null) user.setPassword(this.password);
        if(this.role != null) user.setRole(this.role);
        return user;
    }

    @Override
    public User buildFromDto() {
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setName(name);
        newUser.setPassword(password);
        newUser.setRole(role);
        return newUser;
    }
}
