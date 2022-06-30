package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.db.base.FilmStorage;
import ru.yandex.practicum.filmorate.db.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.db.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    private FilmDbStorage filmCRUD;

    @Autowired
    private GenreDbStorage genreCRUD;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Film create(@RequestBody Film film) throws ValidationException {
        Film created = filmCRUD.create(film);
        if (created.getGenres() != null) {
            for (Genre genre : created.getGenres()) {
                genreCRUD.addFilmGenre(created.getId(), genre.getId());
            }
        }
        log.info("Добавлен фильм {}", film.toString());
        return film;
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Film update(@RequestBody Film film) throws ValidationException {
        try {
            if (film.getId() != 0 && filmCRUD.contains(film.getId())) {
                filmCRUD.update(film);
                genreCRUD.updateFilmGenres(film);
                log.info("Изменен фильм {}", film.toString());
            } else if (film.getId() == 0) {
                filmCRUD.create(film);
                log.info("Добавлен фильм {}", film.toString());
            } else {
                throw new NotFoundException();
            }
            return film;
        } catch (NotFoundException | EmptyResultDataAccessException e) {
            log.info("Попытка обновления несуществующей записи: {}", film.toString());

            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public List<Film> findAll() {
        return filmCRUD.readAll();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable int id) {
        try {
            Film film = filmCRUD.read(id);
            List<Genre> genres = genreCRUD.getFilmGenres(id);
            if (genres.size() == 0){
                genres = null;
            }
            film.setGenres(genres);
            return film;
        } catch (NotFoundException | EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable int id, @PathVariable int userId) throws ValidationException {
        try {
            return filmService.likeMovie(id, userId);
        } catch (NotFoundException | EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable int id, @PathVariable int userId) throws ValidationException {
        try {
            return filmService.deleteLike(id, userId);
        } catch (NotFoundException | EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
        return filmService.getPopular(count);
    }
}
