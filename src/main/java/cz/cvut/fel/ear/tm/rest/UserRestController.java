package cz.cvut.fel.ear.tm.rest;

import cz.cvut.fel.ear.tm.exception.EarException;
import cz.cvut.fel.ear.tm.exception.NotFoundException;
import cz.cvut.fel.ear.tm.model.User;
import cz.cvut.fel.ear.tm.model.User;
import cz.cvut.fel.ear.tm.service.UserService;
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
@RequestMapping(value = "/rest/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User", description = "Users API")
public class UserRestController {
    private final UserService userService;

    @Operation(summary = "Add a new User", description = "endpoint for creating an entity", tags = {"User"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "User already exists")})
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Long> saveUser(
            @Parameter(description = "User", required = true) @NotNull @RequestBody User user) {
        log.info("saveUser() - start: User = {}", user);
        try{
            userService.persist(user);
        } catch (EarException exception){
            return new ResponseEntity<Long>(HttpStatus.FOUND);
        }
        log.info("saveUser() - end: savedAutomobile = {}", user.getId());
        return new ResponseEntity<Long>(HttpStatus.CREATED);
    }

    @Operation(summary = "Find all Users", description = " ", tags = {"User"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = User.class))))})
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("getAllUsers() - start");
        List<User> collection = userService.findAll();
        log.info("getAllUsers() - end");
        return new ResponseEntity<>(collection, HttpStatus.OK);
    }

    @Operation(summary = "Find User", description = " ", tags = {"User"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = User.class))))})
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserById(
            @Parameter(description = "Id of the User to be obtained. Cannot be empty.", required = true)
            @PathVariable Long id
    ) {
        log.info("getUserById() - start");
        User User = userService.find(id);
        if(User == null){
            log.info("getUserById() - end, User not found");
            throw new NotFoundException("User not found");
        }
        log.info("getUserById() - end");
        return User;
    }

    @Operation(summary = "Update an existing Users", description = "need to fill", tags = {"User"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
            @ApiResponse(responseCode = "404", description = "Automobile not found"),
            @ApiResponse(responseCode = "405", description = "Validation exception")})
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User refreshUser(
            @Parameter(description = "Id of the User to be update. Cannot be empty.", required = true)
            @PathVariable Long id,
            @Parameter(description = "User to update.", required = true)
            @RequestBody User User) {
        log.info("refreshAutomobile() - start: id = {}, automobile = {}", id, User);
        userService.update(User);
        log.info("refreshAutomobile() - end: updatedAutomobile = {}", User);
        return User;
    }
}
