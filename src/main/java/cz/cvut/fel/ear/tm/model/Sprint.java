package cz.cvut.fel.ear.tm.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@Schema(name = "Sprint", description = "Sprint", oneOf = Sprint.class)
@NamedQueries(
        @NamedQuery(name = "Sprint.getCurrentSprint", query = "SELECT s FROM Sprint s WHERE :projectId = s.project.id AND s.closed = false")
)
public class Sprint extends AbstractEntity{

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    private String name;

    private String description;

    private LocalDate deadline;

    private boolean closed;

    @OneToMany(mappedBy = "sprint")
    private List<Task> tasks;
}
