package cz.cvut.fel.ear.tm.rest;

import cz.cvut.fel.ear.tm.dto.project.ProjectDto;
import cz.cvut.fel.ear.tm.dto.project.ProjectReadDto;
import cz.cvut.fel.ear.tm.dto.project.ProjectUserDto;
import cz.cvut.fel.ear.tm.dto.task.TaskDto;
import cz.cvut.fel.ear.tm.dto.task.TaskReadDto;
import cz.cvut.fel.ear.tm.dto.user.UserDto;
import cz.cvut.fel.ear.tm.model.Project;
import cz.cvut.fel.ear.tm.model.SecurityUser;
import cz.cvut.fel.ear.tm.model.Task;
import cz.cvut.fel.ear.tm.service.ProjectService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/rest/projects", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
@Tag(name = "Project", description = "Project API")
public class ProjectRestController {

    private final ProjectService service;

    private final TaskService taskService;

    @Operation(summary = "Find all Projects", description = "Gets all project, where currently logged user is enlisted. If user has admin or project manager rules, all projects are displayed.", tags = {"Project"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Project.class)))),
            @ApiResponse(responseCode = "403", description = "JWT expired or other authentication error")
    })
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<ProjectReadDto> getAllProjects(){
        log.info("getAllProjects() - start");
        SecurityUser user = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<ProjectReadDto> allProjects = service.getAll(user);
        log.info("getAllProjects() - end");
        return allProjects;
    }

    @Operation(summary = "Add a new Project", description = "Creates project and adds user, who is logged in", tags = {"Project"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Project created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Project already exists")})
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @RolesAllowed({ "PROJECT_MANAGER", "ADMIN" })
    public Long saveProject(
            @Parameter(description = "Project", required = true) @NotNull @RequestBody ProjectDto project
    ) {
        log.info("saveProject() - start: project = {}", project);
        SecurityUser user = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
        Long newProjectId = service.persist(project, username);
        log.info("saveProject() - end: saved project = {}", project);
        return newProjectId;
    }

    @Operation(summary = "Get project by ID", description = " ", tags = {"Project"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Project.class))))})
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectReadDto getProject(
            @Parameter(description = "Id of the Project to be obtained. Cannot be empty.", required = true)
            @PathVariable Long id
    ){
        log.info("getProject() - start");
        ProjectReadDto project = service.find(id);
        log.info("getProject() - end");
        return project;
    }

    @Operation(summary = "Update an existing Project", description = "need to fill", tags = {"Project"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "405", description = "Validation exception")})
    @PutMapping("{id}")
    @PreAuthorize("hasRole('PROJECT_MANAGER') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public void updateProject(
            @Parameter(description = "Id of the Task to be update. Cannot be empty.", required = true)
            @PathVariable Long id,
            @Parameter(description = "Project to update.", required = true)
            @RequestBody ProjectDto projectDto) {
        log.info("updateProject() - start: id = {}", id);
        service.update(id, projectDto);
        log.info("updateProject() - end:");
    }

    @Operation(summary = "Update an existing Project", description = "need to fill", tags = {"Project"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "405", description = "Validation exception")})
    @DeleteMapping("/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('PROJECT_MANAGER') or hasRole('ADMIN')")
    public void deleteProject(
            @Parameter(description = "Id of the Task to be update. Cannot be empty.", required = true)
            @PathVariable Long projectId
    ) {
        log.info("deleteProject() - start: id = {}", projectId);
        service.deleteProject(projectId);
        log.info("deleteProject() - end:");
    }

    @Operation(summary = "Find project task", description = " ", tags = {"Task"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Task.class))))})
    @GetMapping("/{id}/tasks")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskReadDto> getTasksOfProject(
            @Parameter(description = "Id of the Project to be obtained. Cannot be empty.", required = true)
            @PathVariable Long id
    ) {
        log.info("getTasksOfProject() - start");
        List<TaskReadDto> result = taskService.findByProject(id).stream()
                .map(TaskReadDto::new)
                .filter(task -> task.getParentTaskId() == null)
                .collect(Collectors.toList());
        log.info("getTasksOfProject() - end");
        return result;
    }

    @Operation(summary = "Add a User to project", description = "endpoint for creating an entity", tags = {"Project"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User added"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "User already exists")})
    @PostMapping("/users")
    @PreAuthorize("hasRole('PROJECT_MANAGER') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public void addUser(
            @Parameter(description = "Project", required = true) @NotNull @RequestBody ProjectUserDto dto) {
        log.info("addUser() - start: project = {}", dto);
        service.addUser(dto);
        log.info("addUser() - end: saved project = {}", dto);
    }

    @Operation(summary = "Add a User to project", description = "endpoint for creating an entity", tags = {"Project"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User added"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "User already exists")})
    @DeleteMapping("/{projectId}/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(
            @Parameter(description = "Id of the Task to be obtained. Cannot be empty.", required = true)
            @PathVariable Long projectId,
            @Parameter(description = "Id of the Task to be obtained. Cannot be empty.", required = true)
            @PathVariable Long userId
    ) {
        log.info("removeUser() - start");
        service.removeUser(projectId, userId);
        log.info("removeUser() - end: ");
    }
}
