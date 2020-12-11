package cz.cvut.fel.ear.tm.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Entity(name = "Project")
@Table(name = "project")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@Schema(name = "Project", description = "Project", oneOf = Project.class)
@NamedQueries({
        @NamedQuery(name = "Project.findByName", query = "SELECT t from Project t WHERE :project = t.name"),
})
public class Project extends AbstractEntity{

    private String name;

    private LocalDate deadline;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "project_id")
    private List<Task> tasks = new ArrayList<>();

    public Project(String name) {
        this.name = name;
        this.tasks = new ArrayList<Task>();
        this.deadline = LocalDate.now();
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
}
