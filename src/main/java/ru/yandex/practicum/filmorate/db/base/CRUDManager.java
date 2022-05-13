package ru.yandex.practicum.filmorate.db.base;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

public interface CRUDManager {

    static FilmCRUD<Film, Integer> getFilmCRUD() {
        return null;
    }

    static UserCRUD<User, Integer> getUserCRUD(){return null;};
}
