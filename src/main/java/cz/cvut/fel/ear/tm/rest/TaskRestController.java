package cz.cvut.fel.ear.tm.rest;

import cz.cvut.fel.ear.tm.exception.NotFoundException;
import cz.cvut.fel.ear.tm.model.Comment;
import cz.cvut.fel.ear.tm.model.Task;
import cz.cvut.fel.ear.tm.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(value = "/rest/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Task", description = "Tasks API")
public class TaskRestController {

    private final TaskService taskService;

    @Operation(summary = "Add a new Task", description = "endpoint for creating an entity", tags = {"Task"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Task already exists")})
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveTask(
            @Parameter(description = "Task", required = true) @NotNull @RequestBody Task task) {
        log.info("saveTask() - start: task = {}", task);
        taskService.persist(task);
        log.info("saveTask() - end: savedAutomobile = {}", task.getId());
    }

    @Operation(summary = "Find all Tasks", description = " ", tags = {"Task"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Task.class))))})
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Task>> getAllTasks() {
        log.info("getAllTasks() - start");
        List<Task> collection = taskService.findAll();
        log.info("getAllTasks() - end");
        return new ResponseEntity<>(collection, HttpStatus.OK);
    }

    @Operation(summary = "Find task", description = " ", tags = {"Task"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Task.class))))})
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Task getTaskById(
            @Parameter(description = "Id of the Task to be obtained. Cannot be empty.", required = true)
            @PathVariable Long id
    ) {
        log.info("getTaskById() - start");
        Task task = taskService.find(id);
        if(task == null){
            log.info("getTaskById() - end, Task not found");
            throw new NotFoundException("Task not found");
        }
        log.info("getTaskById() - end");
        return task;
    }

    @Operation(summary = "Update an existing Tasks", description = "need to fill", tags = {"Task"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
            @ApiResponse(responseCode = "404", description = "Automobile not found"),
            @ApiResponse(responseCode = "405", description = "Validation exception")})
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Task refreshTask(
            @Parameter(description = "Id of the Task to be update. Cannot be empty.", required = true)
            @PathVariable Long id,
            @Parameter(description = "Task to update.", required = true)
            @RequestBody Task task) {
        log.info("refreshAutomobile() - start: id = {}, automobile = {}", id, task);
        taskService.update(task);
        log.info("refreshAutomobile() - end: updatedAutomobile = {}", task);
        return task;
    }

    @Operation(summary = "Add comment to task", description = "endpoint for creating an entity", tags = {"Task"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Task already exists")})
    @PostMapping("/{id}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public void addComment(
            @Parameter(description = "Id of the Task to be update. Cannot be empty.", required = true)
            @PathVariable Long id,
            @Parameter(description = "Comment", required = true) @NotNull @RequestBody Comment comment) {
        log.info("addComment() - start: task = {}", comment);
        Task foundTask = taskService.find(id);
        taskService.addComment(foundTask, comment);
        log.info("addComment() - end: updated Task = {}", foundTask.getId());
    }


}
