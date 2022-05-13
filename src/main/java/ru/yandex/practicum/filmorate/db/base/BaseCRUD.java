package ru.yandex.practicum.filmorate.db.base;

import ru.yandex.practicum.filmorate.error.NotFoundError;

public interface BaseCRUD <model, primaryKeyType>{
    model read(primaryKeyType id);

    void create(model object);

    void update(model updatedObject) throws NotFoundError;

    void delete(primaryKeyType id);
}
