package ru.yandex.practicum.filmorate.db.memory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.db.base.StorageManager;
import ru.yandex.practicum.filmorate.db.base.FilmStorage;
import ru.yandex.practicum.filmorate.db.base.UserStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

@Component
public class StorageManagerMemory implements StorageManager {
    private static FilmStorage<Film, Integer> filmCRUD;
    private static UserStorage<User, Integer> userCRUD;

    public FilmStorage<Film, Integer> getFilmCRUD() {
        if (filmCRUD == null) {
            filmCRUD = new FilmStorageMemory();
        }
        return filmCRUD;
    }

    public UserStorage<User, Integer> getUserCRUD() {
        if (userCRUD == null) {
            userCRUD = new UserStorageMemory();
        }
        return userCRUD;
    }
}
