package ru.yandex.practicum.filmorate.db.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.db.base.RatingStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RatingDbStorage implements RatingStorage<Rating, Integer> {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RatingDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Rating read(Integer id) throws NotFoundException {
        String sql = "SELECT * FROM \"ratings\" WHERE \"id\" = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeRating(rs), id);
    }

    @Override
    public Rating create(Rating object) throws ValidationException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO \"ratings\" (\"name\", \"description\") VALUES (?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sql, new String[]{"id"});
            ps.setString(1, object.getName());
            ps.setString(2, object.getDescription());
            return ps;
        }, keyHolder);
        object.setId(keyHolder.getKey().intValue());
        return object;
    }

    @Override
    public Rating update(Rating updatedObject) throws NotFoundException, ValidationException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "UPDATE \"ratings\" SET \"name\"=?, \"description\"=?  WHERE \"id\"=?";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sql, new String[]{"id"});
            ps.setString(1, updatedObject.getName());
            ps.setString(2, updatedObject.getDescription());
            ps.setInt(3, updatedObject.getId());
            return ps;
        }, keyHolder);
        updatedObject.setId(keyHolder.getKey().intValue());
        return updatedObject;
    }

    @Override
    public void delete(Integer id) throws NotFoundException {
        String sql = "DELETE FROM \"ratings\" WHERE \"id\"=?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean contains(Integer id) {
        String sql = "SELECT COUNT(*) FROM \"ratings\" WHERE \"id\" = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count != 0;
    }

    @Override
    public List<Rating> readAll() {
        String sql = "SELECT * FROM \"ratings\"";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeRating(rs));
    }

    private Rating makeRating(ResultSet rs) throws SQLException {
        Integer ratingID = rs.getInt("id");
        String ratingName = rs.getString("name");
        String ratingDescription = rs.getString("description");

        return new Rating(ratingID, ratingName, ratingDescription);
    }
}
