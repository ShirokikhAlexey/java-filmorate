package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.db.base.FriendsStorage;
import ru.yandex.practicum.filmorate.db.base.UserStorage;
import ru.yandex.practicum.filmorate.db.memory.StorageManagerMemory;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserStorage<User, Integer> dbUserSession;
    private final FriendsStorage<Friends, Integer> dbFriendsSession;

    @Autowired
    public UserService(StorageManagerMemory dbManager) {
        this.dbUserSession = dbManager.getUserCRUD();
        this.dbFriendsSession = dbManager.getFriendsCRUD();
    }

    public User sendFriendRequest(int from, int to) throws ValidationException, NotFoundException {
        User userFrom = dbUserSession.read(from);
        User userTo = dbUserSession.read(to);
        dbFriendsSession.addFriend(userFrom.getId(), userTo.getId());
        return userFrom;
    }

    public User deleteFriend(int from, int to) throws ValidationException, NotFoundException {
        User userFrom = dbUserSession.read(from);
        User userTo = dbUserSession.read(to);
        dbFriendsSession.deleteFriend(userFrom.getId(), userTo.getId());
        return userFrom;
    }

    public List<User> getUserFriends(int userId) throws NotFoundException {
        return dbFriendsSession.getUserFriends(userId);
    }

    public List<User> getCommonFriends(int userId, int secondUserId) throws NotFoundException {
        List<User> firstFriends = dbFriendsSession.getUserFriends(userId);

        List<User> commonFriends = new ArrayList<User>();
        for (User friend : dbFriendsSession.getUserFriends(secondUserId)) {
            if (firstFriends.contains(friend)) {
                commonFriends.add(friend);
            }
        }
        return commonFriends;

    }
}
