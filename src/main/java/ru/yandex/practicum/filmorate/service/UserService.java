package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.db.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserDbStorage dbUserSession;

    public User sendFriendRequest(int from, int to) throws ValidationException, NotFoundException {
        User userFrom = dbUserSession.read(from);
        User userTo = dbUserSession.read(to);
        dbUserSession.addFriend(userFrom.getId(), userTo.getId());
        return userFrom;
    }

    public User deleteFriend(int from, int to) throws ValidationException, NotFoundException {
        User userFrom = dbUserSession.read(from);
        User userTo = dbUserSession.read(to);
        dbUserSession.deleteFriend(userFrom.getId(), userTo.getId());
        return userFrom;
    }

    public List<User> getUserFriends(int userId) throws NotFoundException {
        return dbUserSession.getUserFriends(userId);
    }

    public List<User> getCommonFriends(int userId, int secondUserId) throws NotFoundException {
        List<User> firstFriends = dbUserSession.getUserFriends(userId);

        List<User> commonFriends = new ArrayList<User>();
        for (User friend : dbUserSession.getUserFriends(secondUserId)) {
            if (firstFriends.contains(friend)) {
                commonFriends.add(friend);
            }
        }
        return commonFriends;

    }
}
