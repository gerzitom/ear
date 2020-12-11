package cz.cvut.fel.ear.tm.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@Schema(name = "Sprint", description = "Sprint", oneOf = Sprint.class)
public class Sprint extends AbstractEntity{

    private String name;

    private String description;

    private LocalDate deadline;
}
