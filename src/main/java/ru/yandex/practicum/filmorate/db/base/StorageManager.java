package ru.yandex.practicum.filmorate.db.base;

import ru.yandex.practicum.filmorate.model.*;

public interface StorageManager {

    FilmStorage<Film, Integer> getFilmCRUD();

    UserStorage<User, Integer> getUserCRUD();

    GenreStorage<Genre, Integer> getGenreCRUD();

    RatingStorage<Rating, Integer> getRatingCRUD();
}
