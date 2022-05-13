package ru.yandex.practicum.filmorate.db.base;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

public interface CRUDManager {

    public FilmCRUD<Film, Integer> getFilmCRUD();

    public UserCRUD<User, Integer> getUserCRUD();
}
