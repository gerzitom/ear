package cz.cvut.fel.ear.tm.environment;

import cz.cvut.fel.ear.tm.model.Project;
import cz.cvut.fel.ear.tm.model.Task;
import cz.cvut.fel.ear.tm.model.User;

import java.util.Random;

public class Generator {

    private static final Random RAND = new Random();

    public static int randomInt() {
        return RAND.nextInt();
    }

    public static boolean randomBoolean() {
        return RAND.nextBoolean();
    }

    public static User generateUser() {
        final User user = new User();
        user.setName("Test name " + randomInt());
        user.setUsername("username" + randomInt() + "@fel.cvut.cz");
        user.setPassword(Integer.toString(randomInt()));
        return user;
    }

    public static Task generateTask(){
        Task task = new Task();
        task.setName("Testing task " + randomInt());
        task.setDescription("Testing task description");
        return task;
    }

    public static Project generateProject(){
        Project project = new Project();
        project.setName("Project " + randomInt());
        return project;
    }
}
