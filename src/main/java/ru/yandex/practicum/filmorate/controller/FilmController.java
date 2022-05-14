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
@RequestMapping("/films")
public class FilmController {
    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException {
        FilmCRUD<Film, Integer> connection = db.getFilmCRUD();
        connection.create(film);

        log.info("Добавлен фильм {}", film.toString());
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) throws ValidationException {
        FilmCRUD<Film, Integer> connection = db.getFilmCRUD();
        try {
            connection.update(film);
        } catch (NotFoundError e) {
            connection.create(film);
            log.info("Добавлен фильм {}", film.toString());
            return film;
        }
        log.info("Изменен фильм {}", film.toString());
        return film;
    }

    @GetMapping
    public List<Film> findAll() {
        FilmCRUD<Film, Integer> connection = db.getFilmCRUD();
        try {
            return connection.readAll();
        } catch (NotFoundError e) {
            return null;
        }

    }
}
