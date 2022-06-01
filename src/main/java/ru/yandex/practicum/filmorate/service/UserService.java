package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.db.base.UserStorage;
import ru.yandex.practicum.filmorate.db.memory.StorageManagerMemory;
import ru.yandex.practicum.filmorate.model.User;

public class UserService {
    private final StorageManagerMemory dbManager;
    private final UserStorage dbSession;

    @Autowired
    public UserService(StorageManagerMemory dbManager) {
        this.dbManager = dbManager;
        this.dbSession = dbManager.getUserCRUD();
    }

    public void sendFriendRequest(User from, User to) {
        to.getFriendsList().add(from.getId());
        from.getFriendsList().add(to.getId());
    }
}
