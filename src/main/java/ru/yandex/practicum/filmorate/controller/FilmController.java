package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.db.base.FilmCRUD;
import ru.yandex.practicum.filmorate.error.NotFoundError;
import ru.yandex.practicum.filmorate.error.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

import static ru.yandex.practicum.filmorate.FilmorateApplication.db;

@Slf4j
@RestController
@RequestMapping("/film")
public class FilmController {
    @PostMapping
    public Film create(@RequestBody Film film) {
        FilmCRUD<Film, Integer> connection = db.getFilmCRUD();
        try {
            connection.create(film);
        } catch (ValidationException e) {
            return null;
        }
        log.info("Добавлен фильм {}", film.toString());
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        FilmCRUD<Film, Integer> connection = db.getFilmCRUD();
        try {
            connection.update(film);
        } catch (NotFoundError e) {
            try {
                connection.create(film);
            } catch (ValidationException exception) {
                return null;
            }
            log.info("Добавлен фильм {}", film.toString());
            return film;
        } catch (ValidationException e) {
            return null;
        }
        log.info("Изменен фильм {}", film.toString());
        return film;
    }

    @GetMapping(value = "/films")
    public List<Film> findAll() {
        FilmCRUD<Film, Integer> connection = db.getFilmCRUD();
        try {
            return connection.readAll();
        } catch (NotFoundError e) {
            return null;
        }

    }
}
