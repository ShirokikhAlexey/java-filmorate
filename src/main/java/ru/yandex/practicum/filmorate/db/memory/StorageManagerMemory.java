package ru.yandex.practicum.filmorate.db.memory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.db.base.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserFilmLikes;

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
    public UserFilmLikesStorage<UserFilmLikes, Integer> getUserFilmLikesCRUD() {
        return null;
    }

    @Override
    public FriendsStorage<Friends, Integer> getFriendsCRUD() {
        return null;
    }
}
