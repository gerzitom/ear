package cz.cvut.fel.ear.tm.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@NamedQueries({
})

@Schema(name = "Team", description = "Team", oneOf = Team.class)
public class Team extends AbstractEntity{

    private String name;

    @ManyToMany
    private List<User> users;

    /**
     * Gets User by its username
     * @param username
     * @return
     */
    public User findUser(String username) throws NoSuchElementException {
        Optional<User> foundUser = this.users.stream().filter(user -> user.getUsername().equals(username)).reduce((a, b) -> null);
        return foundUser.get();
    }
}
