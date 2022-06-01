package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.db.base.UserStorage;
import ru.yandex.practicum.filmorate.db.memory.StorageManagerMemory;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

@Service
public class UserService {
    private final UserStorage<User, Integer> dbSession;

    @Autowired
    public UserService(StorageManagerMemory dbManager) {
        this.dbSession = dbManager.getUserCRUD();
    }

    public void sendFriendRequest(User from, User to) throws ValidationException, NotFoundException {
        to.getFriendsList().add(from.getId());
        from.getFriendsList().add(to.getId());
        dbSession.update(to);
        dbSession.update(from);
    }
}
