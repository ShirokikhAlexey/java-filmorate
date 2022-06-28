package ru.yandex.practicum.filmorate.db.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.db.base.FilmStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Repository
public class FilmDbStorage implements FilmStorage<Film, Integer> {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film read(Integer id) throws NotFoundException {
        String sql = "SELECT \"f\".\"id\" as \"id\", \"f\".\"name\" as \"name\", \"f\".\"description\" as \"description\", " +
                "\"f\".\"releaseDate\" as \"releaseDate\", \"f\".\"duration\" as \"duration\", \"r\".\"id\" as \"ratingID\", " +
                "\"r\".\"name\" as \"ratingName\", \"r\".\"description\" as \"ratingDescription\" " +
                "FROM \"films\" as \"f\" JOIN \"ratings\" as \"r\" ON \"r\".\"id\" = \"f\".\"rating\" WHERE \"f\".\"id\" = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeFilm(rs), id);
    }

    @Override
    public Film create(Film object) throws ValidationException {
        this.validate(object);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO \"films\" (\"name\", \"description\", \"releaseDate\", \"duration\", \"rating\") " +
                "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sql, new String[] {"id"});
            ps.setString(1, object.getName());
            ps.setString(2, object.getDescription());
            ps.setDate(3, Date.valueOf(object.getReleaseDate()));
            ps.setFloat(4, object.getDuration());
            ps.setInt(5, object.getRatingID());
            return ps;
        }, keyHolder);
        object.setId(keyHolder.getKey().intValue());
        return object;
    }

    @Override
    public Film update(Film updatedObject) throws NotFoundException, ValidationException {
        this.validate(updatedObject);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "UPDATE \"films\" SET \"name\"=?, \"description\"=?, \"releaseDate\"=?, \"duration\"=? ," +
                "\"rating\" = ?" +
                "WHERE \"id\"=?";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sql, new String[] {"id"});
            ps.setString(1, updatedObject.getName());
            ps.setString(2, updatedObject.getDescription());
            ps.setDate(3, Date.valueOf(updatedObject.getReleaseDate()));
            ps.setFloat(4, updatedObject.getDuration());
            ps.setInt(5, updatedObject.getRatingID());
            ps.setInt(6, updatedObject.getId());
            return ps;
        }, keyHolder);
        updatedObject.setId(keyHolder.getKey().intValue());
        return updatedObject;
    }

    @Override
    public void delete(Integer id) throws NotFoundException {
        String sql = "DELETE FROM \"films\" WHERE \"id\"=?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean contains(Integer id) {
        String sql = "SELECT COUNT(*) FROM \"films\" WHERE \"id\" = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count != 0;
    }

    @Override
    public List<Film> readAll() {
        String sql = "SELECT \"f\".\"id\" as \"id\", \"f\".\"name\" as \"name\", " +
                "\"f\".\"description\" as \"description\", " +
                "\"f\".\"releaseDate\" as \"releaseDate\", \"f\".\"duration\" as \"duration\", " +
                "\"r\".\"id\" as \"ratingID\", " +
                "\"r\".\"name\" as \"ratingName\", \"r\".\"description\" as \"ratingDescription\" " +
                "FROM \"films\" as \"f\" JOIN \"ratings\" as \"r\" ON \"r\".\"id\" = \"f\".\"rating\"";

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

        return new Film(id, name, description, releaseDate, duration, ratingID,
                new Rating(ratingID, ratingName, ratingDescription));
    }
}
