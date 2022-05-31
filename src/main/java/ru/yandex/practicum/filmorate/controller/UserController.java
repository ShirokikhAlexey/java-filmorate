package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.db.base.UserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

import static ru.yandex.practicum.filmorate.FilmorateApplication.db;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    @PostMapping
    public User create(@RequestBody User user) throws ValidationException {
        UserStorage<User, Integer> connection = db.getUserCRUD();

        connection.create(user);
        log.info("Добавлен пользователь {}", user.toString());
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) throws ValidationException {
        UserStorage<User, Integer> connection = db.getUserCRUD();
        try {
            if (user.getId() != 0 && connection.contains(user.getId())) {
                connection.update(user);
                log.info("Изменен пользователь {}", user.toString());
            } else if (user.getId() == 0) {
                connection.create(user);
                log.info("Добавлен пользователь {}", user.toString());
            } else {
                throw new NotFoundException();
            }
            return user;
        } catch (NotFoundException e) {
            log.info("Попытка обновления несуществующего пользователя: {}", user.toString());

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public List<User> findAll() {
        UserStorage<User, Integer> connection = db.getUserCRUD();
        return connection.readAll();
    }
}
