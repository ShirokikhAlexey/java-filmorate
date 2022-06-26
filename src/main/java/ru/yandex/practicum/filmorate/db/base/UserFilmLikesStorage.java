package ru.yandex.practicum.filmorate.db.base;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashSet;
import java.util.List;

public interface UserFilmLikesStorage<M, K> extends BaseStorage<M, K> {
    HashSet<Integer> getFilmLikes(K filmId);

    HashSet<Integer> getUserLikes(K userId);

    K getUserLike(K userId, K filmId);

    void addLike(K userId, K filmId) throws ValidationException;

    void deleteLike(K userId, K filmId) throws NotFoundException;

    List<Film> getPopularFilms(int count);

}
