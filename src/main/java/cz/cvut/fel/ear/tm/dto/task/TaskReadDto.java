package cz.cvut.fel.ear.tm.dto.task;

import cz.cvut.fel.ear.tm.model.State;
import cz.cvut.fel.ear.tm.model.Task;
import cz.cvut.fel.ear.tm.model.User;
import cz.cvut.fel.ear.tm.model.relations.TaskUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskReadDto implements Serializable {
    private Long id;
    private String name;
    private String description;
    private LocalDate deadline;
    private Long projectId;
    private Long parentTaskId;
    private State state;
    private List<TaskReadDto> subtasks;
    private List<Long> users;

    public TaskReadDto(Task task) {
        this.id = task.getId();
        this.name = task.getName();
        this.description = task.getDescription();
        this.deadline = task.getDeadline();
        this.projectId = task.getProject().getId();
        this.subtasks = task.getSubtasks().stream().map(TaskReadDto::new).collect(Collectors.toList());
        this.parentTaskId = task.getParentTaskId();
        this.state = task.getState();
        this.users = task.getTaskUsers().stream().map(TaskUser::getUser).map(User::getId).collect(Collectors.toList());
    }
}
