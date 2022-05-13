package ru.yandex.practicum.filmorate.db.base;

public interface BaseCRUD <model, primaryKeyType>{
    public model read(primaryKeyType id);

    public void create(model object);

    public void update(primaryKeyType id, model updatedObject);

    public void delete(primaryKeyType id);
}
