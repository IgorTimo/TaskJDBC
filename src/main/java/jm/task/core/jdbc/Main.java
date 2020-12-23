package jm.task.core.jdbc;



import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.Arrays;


public class Main {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();
        userService.createUsersTable();
        userService.saveUser("lida", "bukina", (byte) 44);
        userService.saveUser("ivan", "lobutin", (byte) 56);
        userService.saveUser("petr", "tirchin", (byte) 23);
        userService.saveUser("bork", "genin", (byte) 79);
        System.out.println(userService.getAllUsers());

        User realUser = userService.saveUser("First", "Real user", (byte) 33, 0); //todo создаём настоящего юзера.
        System.out.println(realUser.toString() + "user id = " + realUser.getId());
        System.out.println("After adding real user:");
        System.out.println(userService.getAllUsers());
        userService.removeUserById(realUser.getId());//todo теперь мы может запроссить его id чтобы его же и удалить
        System.out.println("After removing:");
        System.out.println(userService.getAllUsers());



        System.out.println(userService.getAllUsers());
        userService.cleanUsersTable();
        System.out.println(userService.getAllUsers());
        userService.dropUsersTable();
        userService.getAllUsers();
    }
}
