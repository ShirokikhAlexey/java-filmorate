package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.db.base.FilmStorage;
import ru.yandex.practicum.filmorate.db.base.UserStorage;
import ru.yandex.practicum.filmorate.db.memory.StorageManagerMemory;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collections;
import java.util.List;

@Service
public class FilmService {
    private final FilmStorage<Film, Integer> dbSessionFilm;
    private final UserStorage<User, Integer> dbSessionUser;

    @Autowired
    public FilmService(StorageManagerMemory dbManager) {
        this.dbSessionFilm = dbManager.getFilmCRUD();
        this.dbSessionUser = dbManager.getUserCRUD();
    }

    public Film likeMovie(int userId, int filmId) throws ValidationException, NotFoundException {
        Film film = dbSessionFilm.read(filmId);
        User user = dbSessionUser.read(userId);

        film.getLikes().add(user.getId());
        user.getLikedMovies().add(film.getId());
        dbSessionFilm.update(film);
        dbSessionUser.update(user);
        return film;
    }

    public Film deleteLike(int filmId, int userId) throws ValidationException, NotFoundException {
        Film film = dbSessionFilm.read(filmId);
        User user = dbSessionUser.read(userId);

        if (film.getLikes().contains(userId)) {
            film.getLikes().remove(user.getId());
        }
        if (user.getLikedMovies().remove(filmId)) {
            user.getLikedMovies().remove(film.getId());
        }

        dbSessionFilm.update(film);
        dbSessionUser.update(user);
        return film;
    }

    public List<Film> getPopular(int count) {
        List<Film> allFilms = dbSessionFilm.readAll();
        Collections.sort(allFilms);

        if (allFilms.size() > count) {
            return allFilms.subList(0, count);
        }
        return allFilms;
    }
}
