package ru.yandex.practicum.filmorate.db.base;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.List;

public interface BaseStorage<M, K> {
    M read(K id) throws NotFoundException;

    void create(M object) throws ValidationException;

    void update(M updatedObject) throws NotFoundException, ValidationException;

    void delete(K id) throws NotFoundException;

    boolean contains(K id);

    List<M> readAll();
}
