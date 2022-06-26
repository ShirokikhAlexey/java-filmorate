package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.db.base.FilmStorage;
import ru.yandex.practicum.filmorate.db.base.GenreStorage;
import ru.yandex.practicum.filmorate.db.base.RatingStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;

import static ru.yandex.practicum.filmorate.FilmorateApplication.db;

@Slf4j
@RestController
@RequestMapping("/mpa")
public class RatingController {
    @GetMapping
    public List<Rating> findAll() {
        RatingStorage<Rating, Integer> connection = db.getRatingCRUD();
        return connection.readAll();
    }

    @GetMapping("/{id}")
    public Rating getRating(@PathVariable int id) {
        try {
            RatingStorage<Rating, Integer> connection = db.getRatingCRUD();
            return connection.read(id);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
