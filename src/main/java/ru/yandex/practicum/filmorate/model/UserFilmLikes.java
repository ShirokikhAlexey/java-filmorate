package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class UserFilmLikes {
    private Integer id;
    private Integer userId;
    private Integer filmId;

    public UserFilmLikes(Integer userId, Integer filmId) {
        this.id = null;
        this.userId = userId;
        this.filmId = filmId;
    }

    public UserFilmLikes(Integer id, Integer userId, Integer filmId) {
        this.id = id;
        this.userId = userId;
        this.filmId = filmId;
    }
}