package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.db.base.FilmStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

import static ru.yandex.practicum.filmorate.FilmorateApplication.db;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException {
        FilmStorage<Film, Integer> connection = db.getFilmCRUD();
        connection.create(film);

        log.info("Добавлен фильм {}", film.toString());
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) throws ValidationException {
        FilmStorage<Film, Integer> connection = db.getFilmCRUD();
        try {
            if (film.getId() != 0 && connection.contains(film.getId())) {
                connection.update(film);
                log.info("Изменен фильм {}", film.toString());
            } else if (film.getId() == 0) {
                connection.create(film);
                log.info("Добавлен фильм {}", film.toString());
            } else {
                throw new NotFoundException();
            }
            return film;
        } catch (NotFoundException e) {
            log.info("Попытка обновления несуществующей записи: {}", film.toString());

            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public List<Film> findAll() {
        FilmStorage<Film, Integer> connection = db.getFilmCRUD();
        return connection.readAll();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable int id) {
        try {
            FilmStorage<Film, Integer> connection = db.getFilmCRUD();
            return connection.read(id);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable int id, @PathVariable int userId) throws ValidationException {
        try {
            return filmService.likeMovie(id, userId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable int id, @PathVariable int userId) throws ValidationException {
        try {
            return filmService.deleteLike(id, userId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
        FilmStorage<Film, Integer> connection = db.getFilmCRUD();
        return filmService.getPopular(count);
    }
}
