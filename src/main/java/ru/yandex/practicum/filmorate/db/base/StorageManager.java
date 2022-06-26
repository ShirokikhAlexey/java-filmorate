package ru.yandex.practicum.filmorate.db.base;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserFilmLikes;

public interface StorageManager {

    FilmStorage<Film, Integer> getFilmCRUD();

    UserStorage<User, Integer> getUserCRUD();

    UserFilmLikesStorage<UserFilmLikes, Integer> getUserFilmLikesCRUD();

    FriendsStorage<Friends, Integer> getFriendsCRUD();
}
