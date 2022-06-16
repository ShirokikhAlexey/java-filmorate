package ru.yandex.practicum.filmorate.db.base;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

public interface StorageManager {

    FilmStorage<Film, Integer> getFilmCRUD();

    UserStorage<User, Integer> getUserCRUD();
}
