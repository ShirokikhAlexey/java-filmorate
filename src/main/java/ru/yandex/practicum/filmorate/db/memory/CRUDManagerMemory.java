package ru.yandex.practicum.filmorate.db.memory;

import ru.yandex.practicum.filmorate.db.base.CRUDManager;
import ru.yandex.practicum.filmorate.db.base.FilmCRUD;
import ru.yandex.practicum.filmorate.db.base.UserCRUD;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

public class CRUDManagerMemory implements CRUDManager {
    private static FilmCRUD<Film, Integer> filmCRUD;
    private static UserCRUD<User, Integer> userCRUD;

    public FilmCRUD<Film, Integer> getFilmCRUD() {
        if (filmCRUD == null) {
            filmCRUD = new FilmCRUDMemory();
        }
        return filmCRUD;
    }

    public UserCRUD<User, Integer> getUserCRUD() {
        if (userCRUD == null) {
            userCRUD = new UserCRUDMemory();
        }
        return userCRUD;
    }
}
