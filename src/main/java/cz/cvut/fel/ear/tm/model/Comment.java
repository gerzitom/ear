package cz.cvut.fel.ear.tm.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@Schema(name = "Comment", description = "Comment assigned to task by user", oneOf = Comment.class)
@NamedQueries({
        @NamedQuery(name = "Comment.findByTask", query = "SELECT c from Comment c WHERE :task = c.task.id")
})
public class Comment extends AbstractEntity{

    private String text;

    @ManyToOne
    private User user;

    private LocalDateTime created;

    @ManyToOne
    private Task task;
}
