package ru.yandex.practicum.filmorate.db.base;

import java.util.List;

public interface UserCRUD <model, primaryKeyType> extends BaseCRUD <model, primaryKeyType>{
    List<model> readAll();
}
