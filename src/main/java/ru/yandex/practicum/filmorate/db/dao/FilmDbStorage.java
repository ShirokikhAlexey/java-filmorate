package ru.yandex.practicum.filmorate.db.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.db.base.FilmStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class FilmDbStorage implements FilmStorage<Film, Integer> {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film read(Integer id) throws NotFoundException {
        String sql = "SELECT f.id as 'id', f.name as 'name', f.description as 'description', " +
                "f.releaseDate as 'releaseDate', f.duration as 'duration', r.id as 'ratingID', r.name as 'ratingName'," +
                "r.description as 'ratingDescription' " +
                "FROM films f JOIN ratings r ON r.id = f.rating WHERE f.id = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeFilm(rs), id);
    }

    @Override
    public void create(Film object) throws ValidationException {
        String sql = "INSERT INTO films (name, description, releaseDate, duration, rating) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, object.getName(), object.getDescription(), object.getReleaseDate(),
                object.getDuration(), object.getRatingID());
    }

    @Override
    public void update(Film updatedObject) throws NotFoundException, ValidationException {
        String sql = "UPDATE films SET name=?, description=?, releaseDate=?, duration=? WHERE id=?";
        jdbcTemplate.update(sql, updatedObject.getName(), updatedObject.getDescription(),
                updatedObject.getReleaseDate(), updatedObject.getDuration(), updatedObject.getId());
    }

    @Override
    public void delete(Integer id) throws NotFoundException {
        String sql = "DELETE FROM films WHERE id=?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean contains(Integer id) {
        String sql = "SELECT COUNT(*) FROM films WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count != 0;
    }

    @Override
    public List<Film> readAll() {
        String sql = "SELECT f.id as 'id', f.name as 'name', f.description as 'description', " +
                "f.releaseDate as 'releaseDate', f.duration as 'duration', r.id as 'ratingID', r.name as 'ratingName'," +
                "r.description as 'ratingDescription' " +
                "FROM films f JOIN ratings r ON r.id = f.rating";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("releaseDate").toLocalDate();
        Duration duration = Duration.ofMinutes((long) rs.getFloat("duration"));
        Integer ratingID = rs.getInt("ratingID");
        String ratingName = rs.getString("ratingName");
        String ratingDescription = rs.getString("ratingDescription");

        return new Film(id,  name, description, releaseDate, duration, ratingID,
                new Rating(ratingID, ratingName, ratingDescription));
    }
}
