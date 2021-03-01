package cz.cvut.fel.ear.tm.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import cz.cvut.fel.ear.tm.dto.Dto;
import cz.cvut.fel.ear.tm.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto implements Serializable, Dto<Comment> {
    private String text;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
    private Long userId;

    @Override
    public Comment buildFromDto() {
        return null;
    }

    @Override
    public Comment update(Comment comment) {
        if(text != null) comment.setText(text);
        return comment;
    }

    @Override
    public String toString() {
        return "User " + userId + " commented " + text + " at " + created + ".";
    }
}
