package cz.cvut.fel.ear.tm.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ErrorInfo {
    private String message;
}
