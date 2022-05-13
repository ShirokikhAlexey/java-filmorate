package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.db.base.FilmCRUD;
import ru.yandex.practicum.filmorate.error.NotFoundError;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

import static ru.yandex.practicum.filmorate.FilmorateApplication.db;

@RestController
@RequestMapping("/film")
public class FilmController {
    @PostMapping
    public Film create(@RequestBody Film film){
        FilmCRUD<Film, Integer> connection = db.getFilmCRUD();
        connection.create(film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film){
        FilmCRUD<Film, Integer> connection = db.getFilmCRUD();
        try{
            connection.update(film);
        } catch (NotFoundError e){
            connection.create(film);
        }
        return film;
    }

    @GetMapping(value = "/films")
    public List<Film> findAll(){
        FilmCRUD<Film, Integer> connection = db.getFilmCRUD();
        return connection.readAll();
    }
}
