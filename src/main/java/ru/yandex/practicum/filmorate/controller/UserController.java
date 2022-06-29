package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.db.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @Autowired
    private UserDbStorage userCRUD;

    @PostMapping
    public User create(@RequestBody User user) throws ValidationException {
        userCRUD.create(user);
        log.info("Добавлен пользователь {}", user.toString());
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) throws ValidationException {
        try {
            if (user.getId() != 0 && userCRUD.contains(user.getId())) {
                userCRUD.update(user);
                log.info("Изменен пользователь {}", user.toString());
            } else if (user.getId() == 0) {
                userCRUD.create(user);
                log.info("Добавлен пользователь {}", user.toString());
            } else {
                throw new NotFoundException();
            }
            return user;
        } catch (NotFoundException | EmptyResultDataAccessException e) {
            log.info("Попытка обновления несуществующего пользователя: {}", user.toString());

            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public List<User> findAll() {
        return userCRUD.readAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {
        try {
            return userCRUD.read(id);
        } catch (NotFoundException | EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable int id, @PathVariable int friendId) throws ValidationException {
        try {
            return userService.sendFriendRequest(id, friendId);
        } catch (NotFoundException | EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable int id, @PathVariable int friendId) throws ValidationException {
        try {
            return userService.deleteFriend(id, friendId);
        } catch (NotFoundException | EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        try {
            return userService.getUserFriends(id);
        } catch (NotFoundException | EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        try {
            return userService.getCommonFriends(id, otherId);
        } catch (NotFoundException | EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}
