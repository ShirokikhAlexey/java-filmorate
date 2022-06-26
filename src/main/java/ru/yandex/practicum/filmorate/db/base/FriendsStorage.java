package ru.yandex.practicum.filmorate.db.base;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendsStorage<M, K> extends BaseStorage<M, K> {
    void addFriend(K user, K friend) throws ValidationException;

    void deleteFriend(K user, K friend) throws NotFoundException;

    List<User> getUserFriends(K userId);

    K getUserFriend(K userId, K friendId);
}
