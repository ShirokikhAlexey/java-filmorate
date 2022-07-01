package ru.yandex.practicum.filmorate.db.base;

import java.util.List;

public interface GenreStorage<M, K> extends BaseStorage<M, K> {
    List<M> readAll();
}
