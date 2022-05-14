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
@RequestMapping("/user")
public class UserController {
    @PostMapping
    public User create(@RequestBody User user){
        UserCRUD<User, Integer> connection = db.getUserCRUD();
        try{
            connection.create(user);
        } catch (ValidationException e){
            return null;
        }
        log.info("Добавлен пользователь {}", user.toString());
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user){
        UserCRUD<User, Integer> connection = db.getUserCRUD();
        try{
            connection.update(user);
        } catch (NotFoundError e){
            try{
                connection.create(user);
            } catch (ValidationException exception) {
                return null;
            }
            log.info("Добавлен пользователь {}", user.toString());
            return user;
        } catch (ValidationException exception){
            return null;
        }
        log.info("Изменен пользователь {}", user.toString());
        return user;
    }

    @GetMapping(value = "/users")
    public List<User> findAll(){
        UserCRUD<User, Integer> connection = db.getUserCRUD();
        try{
            return connection.readAll();
        } catch (NotFoundError e){
            return null;
        }

    }
}
