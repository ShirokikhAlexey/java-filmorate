package ru.yandex.practicum.filmorate.db.base;

public interface BaseCRUD <model, primaryKeyType>{
    model read(primaryKeyType id);

    void create(model object);

    void update(primaryKeyType id, model updatedObject);

    void delete(primaryKeyType id);
}
