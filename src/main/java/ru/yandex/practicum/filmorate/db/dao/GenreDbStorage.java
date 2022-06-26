package ru.yandex.practicum.filmorate.db.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.db.base.FilmStorage;
import ru.yandex.practicum.filmorate.db.base.GenreStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class GenreDbStorage implements GenreStorage<Genre, Integer> {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre read(Integer id) throws NotFoundException {
        String sql = "SELECT * FROM genres WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeGenre(rs), id);
    }

    @Override
    public void create(Genre object) throws ValidationException {
        String sql = "INSERT INTO genres (name, description) VALUES (?, ?)";
        jdbcTemplate.update(sql, object.getName(), object.getDescription());
    }

    @Override
    public void update(Genre updatedObject) throws NotFoundException, ValidationException {
        String sql = "UPDATE genres SET name=?, description=?  WHERE id=?";
        jdbcTemplate.update(sql, updatedObject.getName(), updatedObject.getDescription(), updatedObject.getId());
    }

    @Override
    public void delete(Integer id) throws NotFoundException {
        String sql = "DELETE FROM genres WHERE id=?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean contains(Integer id) {
        String sql = "SELECT COUNT(*) FROM genres WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count != 0;
    }

    @Override
    public List<Genre> readAll() {
        String sql = "SELECT * FROM genres";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");

        return new Genre(id, name, description);
    }
}
