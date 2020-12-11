package cz.cvut.fel.ear.tm.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(name = "Task", description = "Task", oneOf = Task.class)
@NamedQueries({
        @NamedQuery(name = "Task.findByProject", query = "SELECT t from Task t WHERE :project = t.project.id"),
        @NamedQuery(name = "Task.findByUser", query = "SELECT t from Task t WHERE :user MEMBER OF t.users")
})
public class Task extends AbstractEntity{

    private String name;

    private String description;

    private Date deadline;

    @ManyToOne(
            cascade = CascadeType.PERSIST
    )
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToOne
    @ToString.Exclude
    private User responsibleUser;

    @OneToMany(cascade = CascadeType.PERSIST)
    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<User> users = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Task> subtasks;

    @ManyToOne
    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    private Task parentTask;

    @OneToMany(cascade = CascadeType.ALL)
    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Comment> comments;

    public Task(String name, String description, Date deadline, User responsibleUser) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.responsibleUser = responsibleUser;
    }

    public void addComment(Comment comment){
        this.comments.add(comment);
    }

    public void addUser(User user){
        this.users.add(user);

    }
    
    public void addSubtask(Task subtask){
        this.subtasks.add(subtask);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
