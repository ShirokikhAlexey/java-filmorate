package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.db.base.UserStorage;
import ru.yandex.practicum.filmorate.db.memory.StorageManagerMemory;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserStorage<User, Integer> dbSession;

    @Autowired
    public UserService(StorageManagerMemory dbManager) {
        this.dbSession = dbManager.getUserCRUD();
    }

    public User sendFriendRequest(int from, int to) throws ValidationException, NotFoundException {
        User userFrom = dbSession.read(from);
        User userTo = dbSession.read(to);

        userTo.getFriendsList().add(userFrom.getId());
        userFrom.getFriendsList().add(userTo.getId());
        dbSession.update(userTo);
        dbSession.update(userFrom);
        return userFrom;
    }

    public User deleteFriend(int from, int to) throws ValidationException, NotFoundException {
        User userFrom = dbSession.read(from);
        User userTo = dbSession.read(to);

        userTo.getFriendsList().remove(userFrom.getId());
        userFrom.getFriendsList().remove(userTo.getId());
        dbSession.update(userTo);
        dbSession.update(userFrom);
        return userFrom;
    }

    public List<User> getUserFriends(int userId) throws NotFoundException {
        List<User> friendsList = new ArrayList<>();
        User user = dbSession.read(userId);
        for (int friendId : user.getFriendsList()) {
            friendsList.add(dbSession.read(friendId));
        }
        return friendsList;
    }

    public List<User> getCommonFriends(int userId, int secondUserId) throws NotFoundException {
        User firstUser = dbSession.read(userId);
        User secondUser = dbSession.read(secondUserId);

        Set<Integer> intersection = new HashSet<Integer>(firstUser.getFriendsList());
        intersection.retainAll(secondUser.getFriendsList());

        List<User> commonFriends = new ArrayList<User>();
        for (int friendId : intersection) {
            commonFriends.add(dbSession.read(friendId));
        }
        return commonFriends;

    }
}
