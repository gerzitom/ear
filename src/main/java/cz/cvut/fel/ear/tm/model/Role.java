package cz.cvut.fel.ear.tm.model;


import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public enum Role implements GrantedAuthority {
    USER("USER"),
    PROJECT_MANAGER("PROJECT_MANAGER"),
    ADMIN("ADMIN");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }


    @Override
    public String getAuthority() {
        return name;
    }
}
