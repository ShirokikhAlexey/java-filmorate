package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.db.base.FilmStorage;
import ru.yandex.practicum.filmorate.db.base.UserFilmLikesStorage;
import ru.yandex.practicum.filmorate.db.memory.StorageManagerMemory;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.UserFilmLikes;

import java.util.List;

@Service
public class FilmService {
    private final FilmStorage<Film, Integer> dbSessionFilm;
    private final UserFilmLikesStorage<UserFilmLikes, Integer> dbSessionUserFilmLikes;

    @Autowired
    public FilmService(StorageManagerMemory dbManager) {
        this.dbSessionFilm = dbManager.getFilmCRUD();
        this.dbSessionUserFilmLikes = dbManager.getUserFilmLikesCRUD();
    }

    public Film likeMovie(int userId, int filmId) throws ValidationException, NotFoundException {
        Film film = dbSessionFilm.read(filmId);
        this.dbSessionUserFilmLikes.addLike(userId, filmId);
        return film;
    }

    public Film deleteLike(int filmId, int userId) throws ValidationException, NotFoundException {
        Film film = dbSessionFilm.read(filmId);
        this.dbSessionUserFilmLikes.deleteLike(userId, filmId);
        return film;
    }

    public List<Film> getPopular(int count) {
        return this.dbSessionUserFilmLikes.getPopularFilms(count);
    }
}
