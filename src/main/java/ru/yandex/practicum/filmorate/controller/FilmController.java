package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.db.base.FilmCRUD;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
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
            if (connection.contains(film.getId())) {
                connection.update(film);
                log.info("Изменен фильм {}", film.toString());
            } else {
                connection.create(film);
                log.info("Добавлен фильм {}", film.toString());
            }
            return film;

        } catch (NotFoundException e) {
            log.info("Попытка обновления несуществующей записи: {}", film.toString());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public List<Film> findAll() {
        FilmCRUD<Film, Integer> connection = db.getFilmCRUD();
        return connection.readAll();
    }
}
