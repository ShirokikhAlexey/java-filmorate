package ru.yandex.practicum.filmorate.db.memory;

import ru.yandex.practicum.filmorate.db.base.UserCRUD;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;

public class UserCRUDMemory implements UserCRUD<User, Integer> {
    private HashMap<Integer, User> db;

    public UserCRUDMemory(){
        this.db = new HashMap<>();
    }

    @Override
    public User read(Integer id) {
        return null;
    }

    @Override
    public void create(User object) {

    }

    @Override
    public void update(Integer id, User updatedObject) {

    }

    @Override
    public void delete(Integer id) {

    }
}