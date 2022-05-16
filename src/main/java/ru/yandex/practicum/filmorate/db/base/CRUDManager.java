package ru.yandex.practicum.filmorate.db.base;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

public interface CRUDManager {

    FilmCRUD<Film, Integer> getFilmCRUD();

    UserCRUD<User, Integer> getUserCRUD();
}
