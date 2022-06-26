package ru.yandex.practicum.filmorate.db.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.db.base.*;
import ru.yandex.practicum.filmorate.model.*;

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

    @Override
    public FriendsStorage<Friends, Integer> getFriendsCRUD() {
        return new FriendsDbStorage(this.jdbcTemplate);
    }

    @Override
    public GenreStorage<Genre, Integer> getGenreCRUD() {
        return new GenreDbStorage(this.jdbcTemplate);
    }

    @Override
    public RatingStorage<Rating, Integer> getRatingCRUD() {
        return new RatingDbStorage(this.jdbcTemplate);
    }
}
