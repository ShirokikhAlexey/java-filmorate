package ru.yandex.practicum.filmorate.db.memory;

import ru.yandex.practicum.filmorate.db.base.FilmCRUD;
import ru.yandex.practicum.filmorate.error.NotFoundException;
import ru.yandex.practicum.filmorate.error.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FilmCRUDMemory implements FilmCRUD<Film, Integer> {
    private HashMap<Integer, Film> db;

    public FilmCRUDMemory() {
        this.db = new HashMap<>();
    }

    @Override
    public Film read(Integer id) throws NotFoundException {
        if (db.containsKey(id)) {
            return db.get(id);
        }
        throw new NotFoundException();
    }

    @Override
    public void create(Film object) throws ValidationException {
        this.validate(object);
        db.put(object.getId(), object);
    }

    @Override
    public void update(Film updatedObject) throws NotFoundException, ValidationException {
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
    public boolean contains(Integer id) {
        return db.containsKey(id);
    }

    @Override
    public List<Film> readAll() {
        return new ArrayList<>(db.values());
    }
}
