package ru.yandex.practicum.filmorate.db.memory;

import ru.yandex.practicum.filmorate.db.base.FilmCRUD;
import ru.yandex.practicum.filmorate.error.NotFoundError;
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
    public Film read(Integer id) throws NotFoundError {
        if (db.containsKey(id)){
            return db.get(id);
        }
        throw new NotFoundError();
    }

    @Override
    public void create(Film object) {
        db.put(object.getId(), object);
    }

    @Override
    public void update(Film updatedObject) throws NotFoundError {
        if (db.containsKey(updatedObject.getId())){
            db.put(updatedObject.getId(), updatedObject);
        } else {
            throw new NotFoundError();
        }
    }

    @Override
    public void delete(Integer id) throws NotFoundError {
        if (db.containsKey(id)){
            db.remove(id);
        } else {
            throw new NotFoundError();
        }
    }

    @Override
    public List<Film> readAll() throws NotFoundError {
        if (db.isEmpty()){
            throw new NotFoundError();
        } else {
            return new ArrayList<>(db.values());
        }
    }
}
