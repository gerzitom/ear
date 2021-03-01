package cz.cvut.fel.ear.tm.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TaskUserDto {
    private Long taskId;
    private Long userId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime created;
}
