package ru.yandex.practicum.filmorate.db.memory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.db.base.*;
import ru.yandex.practicum.filmorate.model.*;

@Component
public class StorageManagerMemory implements StorageManager {
    private static FilmStorage<Film, Integer> filmCRUD;
    private static UserStorage<User, Integer> userCRUD;

    @Override
    public FilmStorage<Film, Integer> getFilmCRUD() {
        if (filmCRUD == null) {
            filmCRUD = new FilmStorageMemory();
        }
        return filmCRUD;
    }

    @Override
    public UserStorage<User, Integer> getUserCRUD() {
        if (userCRUD == null) {
            userCRUD = new UserStorageMemory();
        }
        return userCRUD;
    }

    @Override
    public GenreStorage<Genre, Integer> getGenreCRUD() {
        return null;
    }

    @Override
    public RatingStorage<Rating, Integer> getRatingCRUD() {
        return null;
    }
}
