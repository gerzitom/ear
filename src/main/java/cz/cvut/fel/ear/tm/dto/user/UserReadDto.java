package cz.cvut.fel.ear.tm.dto.user;

import cz.cvut.fel.ear.tm.dto.ImageReadDto;
import cz.cvut.fel.ear.tm.model.Image;
import cz.cvut.fel.ear.tm.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

import static cz.cvut.fel.ear.tm.config.ConfigConstants.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserReadDto implements Serializable {
    private Long id;
    private String username;
    private String name;
    private ImageReadDto avatar;

    public UserReadDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        Image avatar = user.getAvatar();
        if(avatar != null)
            this.avatar = new ImageReadDto(avatar, AVATARS_PUBLIC_PATH);
    }
}
