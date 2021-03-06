package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.db.base.UserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

import static ru.yandex.practicum.filmorate.FilmorateApplication.db;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

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

            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public List<User> findAll() {
        UserStorage<User, Integer> connection = db.getUserCRUD();
        return connection.readAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {
        try {
            UserStorage<User, Integer> connection = db.getUserCRUD();
            return connection.read(id);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable int id, @PathVariable int friendId) throws ValidationException {
        try {
            return userService.sendFriendRequest(id, friendId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable int id, @PathVariable int friendId) throws ValidationException {
        try {
            return userService.deleteFriend(id, friendId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        try {
            return userService.getUserFriends(id);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        try {
            return userService.getCommonFriends(id, otherId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}
