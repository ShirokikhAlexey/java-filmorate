package ru.yandex.practicum.filmorate.db.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.db.base.FilmStorage;
import ru.yandex.practicum.filmorate.db.base.StorageManager;
import ru.yandex.practicum.filmorate.db.base.UserFilmLikesStorage;
import ru.yandex.practicum.filmorate.db.base.UserStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserFilmLikes;

public class StorageManagerDb implements StorageManager {
    private JdbcTemplate jdbcTemplate;

    public StorageManagerDb(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public FilmStorage<Film, Integer> getFilmCRUD() {
        return new FilmDbStorage(this.jdbcTemplate);
    }

    @Override
    public UserStorage<User, Integer> getUserCRUD() {
        return new UserDbStorage(this.jdbcTemplate);
    }

    @Override
    public UserFilmLikesStorage<UserFilmLikes, Integer> getUserFilmLikesCRUD() {
        return new UserFilmLikesDbStorage(this.jdbcTemplate);
    }
}
