package ru.yandex.practicum.filmorate.db.base;

import ru.yandex.practicum.filmorate.error.NotFoundError;
import ru.yandex.practicum.filmorate.error.ValidationException;

public interface BaseCRUD<model, primaryKeyType> {
    model read(primaryKeyType id) throws NotFoundError;

    void create(model object) throws ValidationException;

    void update(model updatedObject) throws NotFoundError, ValidationException;

    void delete(primaryKeyType id) throws NotFoundError;

    boolean contains(primaryKeyType id);
}
