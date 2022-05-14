package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.db.base.UserCRUD;
import ru.yandex.practicum.filmorate.error.NotFoundError;
import ru.yandex.practicum.filmorate.error.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

import static ru.yandex.practicum.filmorate.FilmorateApplication.db;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    @PostMapping
    public User create(@RequestBody User user) throws ValidationException {
        UserCRUD<User, Integer> connection = db.getUserCRUD();

        connection.create(user);
        log.info("Добавлен пользователь {}", user.toString());
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) throws ValidationException {
        UserCRUD<User, Integer> connection = db.getUserCRUD();
        try {
            connection.update(user);
        } catch (NotFoundError e) {
            connection.create(user);
            log.info("Добавлен пользователь {}", user.toString());
            return user;
        }
        log.info("Изменен пользователь {}", user.toString());
        return user;
    }

    @GetMapping
    public List<User> findAll() {
        UserCRUD<User, Integer> connection = db.getUserCRUD();
        try {
            return connection.readAll();
        } catch (NotFoundError e) {
            return null;
        }

    }
}
