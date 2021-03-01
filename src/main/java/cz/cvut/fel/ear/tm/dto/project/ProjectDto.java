package cz.cvut.fel.ear.tm.dto.project;

import cz.cvut.fel.ear.tm.model.Project;
import cz.cvut.fel.ear.tm.model.Task;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto implements Serializable {
     private String name;
     private LocalDate deadline;

    public Project updateProject(Project project){
        if(this.name != null) project.setName(this.name);
        if(this.deadline != null) project.setDeadline(this.deadline);
        return project;
    }

    public Project buildNewProject() {
        Project project = new Project();
        project.setName(this.name);
        project.setDeadline(this.deadline);
        return project;
    }
}
