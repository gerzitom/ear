package cz.cvut.fel.ear.tm.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@Schema(name = "Project manager", description = "Project manager", oneOf = ProjectManager.class)
public class ProjectManager extends User{
}

