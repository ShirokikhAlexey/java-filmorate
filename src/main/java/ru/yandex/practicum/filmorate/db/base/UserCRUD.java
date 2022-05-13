package ru.yandex.practicum.filmorate.db.base;

import ru.yandex.practicum.filmorate.error.NotFoundError;

import java.util.List;

public interface UserCRUD <model, primaryKeyType> extends BaseCRUD <model, primaryKeyType>{
    List<model> readAll() throws NotFoundError;
}
