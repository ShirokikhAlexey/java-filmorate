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

@Service
public class FilmService {
    private final StorageManagerMemory dbManager;
    private final FilmStorage dbSessionFilm;
    private final UserStorage dbSessionUser;

    @Autowired
    public FilmService(StorageManagerMemory dbManager) {
        this.dbManager = dbManager;
        this.dbSessionFilm = dbManager.getFilmCRUD();
        this.dbSessionUser = dbManager.getUserCRUD();
    }

    public void likeMovie(User user, Film film) throws ValidationException, NotFoundException {
        film.getLikes().add(user.getId());
        user.getLikedMovies().add(film.getId());
        dbSessionFilm.update(film);
        dbSessionUser.update(user);
    }
}
