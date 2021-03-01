package cz.cvut.fel.ear.tm.service;

import cz.cvut.fel.ear.tm.dao.TaskUserDao;
import cz.cvut.fel.ear.tm.dao.UserDao;
import cz.cvut.fel.ear.tm.dto.task.TaskReadDto;
import cz.cvut.fel.ear.tm.dto.user.UserDto;
import cz.cvut.fel.ear.tm.dto.user.UserLoginDto;
import cz.cvut.fel.ear.tm.dto.user.UserReadDto;
import cz.cvut.fel.ear.tm.exception.*;
import cz.cvut.fel.ear.tm.model.Image;
import cz.cvut.fel.ear.tm.model.Role;
import cz.cvut.fel.ear.tm.model.User;
import cz.cvut.fel.ear.tm.model.relations.TaskUser;
import cz.cvut.fel.ear.tm.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserDao dao;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private ImageService imageService;

    private TaskUserDao taskUserDao;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    public UserService(UserDao dao, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, ImageService imageService, TaskUserDao taskUserDao) {
        this.dao = dao;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.imageService = imageService;
        this.taskUserDao = taskUserDao;
    }


    @Transactional
    public Long persist(UserDto dto) throws EarException {
        User user = buildFromDto(dto);
        List<User> users = dao.findAll();
        if(dao.findAll().contains(user)){
            throw new AlreadyExistsException("User with username: " + user.getUsername() + " already exists");
        } else {
            dao.persist(user);
        }
        return user.getId();
    }

    @Transactional
    public UserReadDto find(Long id){
        return new UserReadDto(dao.find(id));
    }

    @Transactional
    public User findByUsername(String username){
        try {
            User user = dao.findByUsername(username);
            return user;
        } catch (Exception e){
            throw new NotFoundException("User with username " + username + " was not found.");
        }
    }

    @Transactional
    public Map<Long, UserReadDto> findAll(){
        Map<Long, UserReadDto> users = dao.findAll().stream().map(UserReadDto::new).collect(Collectors.toMap(UserReadDto::getId, Function.identity()));
        return users;
    }

    @Transactional
    public void update(Long userId, UserDto dto){
        User user = dao.find(userId);
        dao.update(dto.update(user));
    }

    @Transactional
    public void remove(Long userId){
        User user = dao.find(userId);
        user.setRemoved(true);
        dao.remove(user);
    }

    /**
     * Checks params and decides if params are correct.
     * @param dto
     * @return user token if successful
     * @throws UnauthorizedException if not successful
     * @throws AuthenticationException if credentials are bad or if user is removed
     */
    @Transactional
    public String login(UserLoginDto dto) throws UnauthorizedException, AuthenticationException {
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (Exception e){
            throw new AuthenticationException(e.getMessage());
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(dto.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);
        return jwt;
    }

    @Transactional
    public UserReadDto getAuthenticatedUser(String token){
        String jwt = token.substring(7);
        String username = jwtUtil.extractUsername(jwt);
        return new UserReadDto(dao.findByUsername(username));
    }

    @Transactional
    public void setUserAvatar(Long userId, MultipartFile file) throws IOException {
        Image avatar = imageService.storeImage(file);
        User user = dao.find(userId);
        if(user == null) throw new NotFoundException("User not found");
        user.setAvatar(avatar);
        dao.update(user);
    }

    public List<TaskReadDto> getUpcommingTasks(Long userId){
        return taskUserDao.getUpcommingTasks(userId).stream().map(TaskUser::getTask).map(TaskReadDto::new).collect(Collectors.toList());
    }


    private User buildFromDto(UserDto dto) {
        User newUser = new User();
        newUser.setUsername(dto.getUsername());
        newUser.setName(dto.getName());
        newUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        if(dto.getRole() == null)
            newUser.setRole(Role.USER);
        else
            newUser.setRole(dto.getRole());
        newUser.setRemoved(false);
        return newUser;
    }
}
