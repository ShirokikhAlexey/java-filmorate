package ru.yandex.practicum.filmorate.db.memory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.db.base.UserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class UserStorageMemory implements UserStorage<User, Integer> {
    private HashMap<Integer, User> db;
    private int autoincrement = 0;

    public UserStorageMemory() {
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
    public User create(User object) throws ValidationException {
        this.validate(object);
        if (object.getId() == 0) {
            autoincrement += 1;
            db.put(autoincrement, object);
            object.setId(autoincrement);
        } else {
            if (db.containsKey(object.getId())) {
                throw new ValidationException();
            }
            db.put(object.getId(), object);
        }
        return object;
    }

    @Override
    public User update(User updatedObject) throws NotFoundException, ValidationException {
        this.validate(updatedObject);
        if (db.containsKey(updatedObject.getId())) {
            db.put(updatedObject.getId(), updatedObject);
        } else {
            throw new NotFoundException();
        }
        return updatedObject;
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
