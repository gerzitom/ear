package cz.cvut.fel.ear.tm.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@Schema(name = "Tracked time", description = "Time tracked to task", oneOf = TrackedTime.class)
public class TrackedTime extends AbstractEntity{

    private LocalDateTime start;

    private LocalDateTime end;
}
