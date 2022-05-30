package ru.yandex.practicum.filmorate.db.memory;

import ru.yandex.practicum.filmorate.db.base.UserCRUD;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserCRUDMemory implements UserCRUD<User, Integer> {
    private HashMap<Integer, User> db;

    public UserCRUDMemory() {
        this.db = new HashMap<>();
    }

    @Override
    public User read(Integer id) throws NotFoundException {
        if (db.containsKey(id)) {
            return db.get(id);
        }
        throw new NotFoundException();
    }

    @Override
    public void create(User object) throws ValidationException {
        this.validate(object);
        db.put(object.getId(), object);
    }

    @Override
    public void update(User updatedObject) throws NotFoundException, ValidationException {
        this.validate(updatedObject);
        if (db.containsKey(updatedObject.getId())) {
            db.put(updatedObject.getId(), updatedObject);
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public void delete(Integer id) throws NotFoundException {
        if (db.containsKey(id)) {
            db.remove(id);
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public List<User> readAll() {
        return new ArrayList<>(db.values());
    }

    @Override
    public boolean contains(Integer id) {
        return db.containsKey(id);
    }
}
