package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.db.base.RatingStorage;
import ru.yandex.practicum.filmorate.db.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.db.dao.RatingDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/mpa")
public class RatingController {
    @Autowired
    private RatingDbStorage ratingCRUD;

    @GetMapping
    public List<Rating> findAll() {
        return ratingCRUD.readAll();
    }

    @GetMapping("/{id}")
    public Rating getRating(@PathVariable int id) {
        try {
            return ratingCRUD.read(id);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
