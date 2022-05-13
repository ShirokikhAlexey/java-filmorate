package ru.yandex.practicum.filmorate.db.memory;

import ru.yandex.practicum.filmorate.db.base.FilmCRUD;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;

public class FilmCRUDMemory implements FilmCRUD<Film, Integer> {
    private HashMap<Integer, Film> db;

    public FilmCRUDMemory() {
        this.db = new HashMap<>();
    }

    @Override
    public Film read(Integer id) {
        return null;
    }

    @Override
    public void create(Film object) {

    }

    @Override
    public void update(Film updatedObject) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public List<Film> readAll() {
        return null;
    }
}
