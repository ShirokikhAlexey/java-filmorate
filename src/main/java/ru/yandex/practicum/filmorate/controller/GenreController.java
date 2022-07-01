package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.db.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
public class GenreController {

    @Autowired
    private GenreDbStorage genreCRUD;

    @GetMapping
    public List<Genre> findAll() {
        return genreCRUD.readAll();
    }

    @GetMapping("/{id}")
    public Genre getGenre(@PathVariable int id) {
        try {
            return genreCRUD.read(id);
        } catch (NotFoundException | EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
