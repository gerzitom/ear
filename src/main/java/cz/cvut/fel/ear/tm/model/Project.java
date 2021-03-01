package cz.cvut.fel.ear.tm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import cz.cvut.fel.ear.tm.model.relations.ProjectUser;
import cz.cvut.fel.ear.tm.response.ProjectStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Entity(name = "Project")
@Table(name = "project")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Schema(name = "Project", description = "Project", oneOf = Project.class)
@NamedQueries({
        @NamedQuery(name = "Project.findByName", query = "SELECT t from Project t WHERE :project = t.name"),
        @NamedQuery(name = "Project.getTasksBy", query = "SELECT t from Task t WHERE :project = t.project.id AND t.state = :state"),
})
public class Project{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id", updatable = false, nullable = false)
    private Long id;

    @Column(unique = true)
    private String name;

    private LocalDate deadline;

    @OneToMany(
            mappedBy = "project",
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private List<Task> tasks = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    private List<Sprint> sprints = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private List<ProjectUser> projectUsers = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private State state = State.IN_PROGRESS;

    public Project(String name) {
        this.name = name;
        this.tasks = new ArrayList<Task>();
        this.deadline = LocalDate.now();
    }

    public Project(String name, LocalDate deadline) {
        this.name = name;
        this.deadline = deadline;
    }

    public void addTask(Task task){
        this.tasks.add(task);
        // TODO can this be solved by some cascade
//        task.setProject(this);
    }

    public boolean taskExists(String taskName){
        AtomicReference<Boolean> taskExists = new AtomicReference<>(false);
        this.tasks.forEach(task -> {
            if(task.getName().equals(taskName)){
                taskExists.set(true);
            }
        });
        return taskExists.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Project)) return false;
        Project project = (Project) o;
        return id.equals(project.id) &&
                name.equals(project.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}