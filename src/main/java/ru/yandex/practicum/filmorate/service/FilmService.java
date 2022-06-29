package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.db.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Service
public class FilmService {
    @Autowired
    private FilmDbStorage dbSessionFilm;

    public Film likeMovie(int userId, int filmId) throws ValidationException, NotFoundException {
        Film film = dbSessionFilm.read(filmId);
        dbSessionFilm.addLike(userId, filmId);
        return film;
    }

    public Film deleteLike(int filmId, int userId) throws ValidationException, NotFoundException {
        Film film = dbSessionFilm.read(filmId);
        dbSessionFilm.deleteLike(userId, filmId);
        return film;
    }

    public List<Film> getPopular(int count) {
        return dbSessionFilm.getPopularFilms(count);
    }
}
