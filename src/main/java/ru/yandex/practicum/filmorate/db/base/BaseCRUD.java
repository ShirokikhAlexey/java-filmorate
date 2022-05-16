package ru.yandex.practicum.filmorate.db.base;

import ru.yandex.practicum.filmorate.error.NotFoundError;
import ru.yandex.practicum.filmorate.error.ValidationException;

public interface BaseCRUD<M, K> {
    M read(K id) throws NotFoundError;

    void create(M object) throws ValidationException;

    void update(M updatedObject) throws NotFoundError, ValidationException;

    void delete(K id) throws NotFoundError;

    boolean contains(K id);
}
