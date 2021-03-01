package cz.cvut.fel.ear.tm.dto.sprint;

import com.fasterxml.jackson.annotation.JsonFormat;
import cz.cvut.fel.ear.tm.dto.Dto;
import cz.cvut.fel.ear.tm.model.Sprint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class SprintDto implements Serializable {
    private Long projectId;
    private String name;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline;

    public Sprint update(Sprint sprint) {
        if(sprint.getName() != null) name = sprint.getName();
        if(sprint.getDescription() != null) description = sprint.getDescription();
        if(sprint.getDeadline() != null) deadline = sprint.getDeadline();
        return null;
    }
}
