package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.db.base.FilmCRUD;
import ru.yandex.practicum.filmorate.db.base.UserCRUD;
import ru.yandex.practicum.filmorate.error.NotFoundError;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

import static ru.yandex.practicum.filmorate.FilmorateApplication.db;

@RestController
@RequestMapping("/user")
public class UserController {
    @PostMapping
    public User create(@RequestBody User user){
        UserCRUD<User, Integer> connection = db.getUserCRUD();
        connection.create(user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user){
        UserCRUD<User, Integer> connection = db.getUserCRUD();
        try{
            connection.update(user);
        } catch (NotFoundError e){
            connection.create(user);
        }
        return user;
    }

    @GetMapping(value = "/users")
    public List<User> findAll(){
        UserCRUD<User, Integer> connection = db.getUserCRUD();
        return connection.readAll();
    }
}
