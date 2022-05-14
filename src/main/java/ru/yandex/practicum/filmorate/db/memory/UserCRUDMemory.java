package ru.yandex.practicum.filmorate.db.memory;

import ru.yandex.practicum.filmorate.db.base.UserCRUD;
import ru.yandex.practicum.filmorate.error.NotFoundError;
import ru.yandex.practicum.filmorate.error.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserCRUDMemory implements UserCRUD<User, Integer> {
    private HashMap<Integer, User> db;

    public UserCRUDMemory(){
        this.db = new HashMap<>();
    }

    @Override
    public User read(Integer id) throws NotFoundError{
        if (db.containsKey(id)){
            return db.get(id);
        }
        throw new NotFoundError();
    }

    @Override
    public void create(User object) throws ValidationException {
        this.validate(object);
        db.put(object.getId(), object);
    }

    @Override
    public void update(User updatedObject) throws NotFoundError, ValidationException{
        this.validate(updatedObject);
        if (db.containsKey(updatedObject.getId())){
            db.put(updatedObject.getId(), updatedObject);
        } else {
            throw new NotFoundError();
        }
    }

    @Override
    public void delete(Integer id) throws NotFoundError{
        if (db.containsKey(id)){
            db.remove(id);
        } else {
            throw new NotFoundError();
        }
    }

    @Override
    public List<User> readAll() throws NotFoundError {
        if (db.isEmpty()){
            throw new NotFoundError();
        } else {
            return new ArrayList<>(db.values());
        }
    }
}
