package ru.yandex.practicum.filmorate.db.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.db.base.RatingStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

public class RatingDbStorage implements RatingStorage<Rating, Integer> {
    private final JdbcTemplate jdbcTemplate;

    public RatingDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Rating read(Integer id) throws NotFoundException {
        String sql = "SELECT * FROM ratings WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeRating(rs), id);
    }

    @Override
    public void create(Rating object) throws ValidationException {
        String sql = "INSERT INTO ratings (name, description) VALUES (?, ?)";
        jdbcTemplate.update(sql, object.getName(), object.getDescription());
    }

    @Override
    public void update(Rating updatedObject) throws NotFoundException, ValidationException {
        String sql = "UPDATE ratings SET name=?, description=?  WHERE id=?";
        jdbcTemplate.update(sql, updatedObject.getName(), updatedObject.getDescription(), updatedObject.getId());
    }

    @Override
    public void delete(Integer id) throws NotFoundException {
        String sql = "DELETE FROM ratings WHERE id=?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean contains(Integer id) {
        String sql = "SELECT COUNT(*) FROM ratings WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count != 0;
    }

    @Override
    public List<Rating> readAll() {
        String sql = "SELECT * FROM ratings";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeRating(rs));
    }

    private Rating makeRating(ResultSet rs) throws SQLException {
        Integer ratingID = rs.getInt("id");
        String ratingName = rs.getString("name");
        String ratingDescription = rs.getString("description");

        return new Rating(ratingID, ratingName, ratingDescription);
    }
}
