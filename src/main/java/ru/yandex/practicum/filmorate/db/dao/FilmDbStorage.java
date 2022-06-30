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
import ru.yandex.practicum.filmorate.model.Genre;
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
        String sql = "SELECT \"f\".\"id\" as \"filmID\", \"f\".\"name\" as \"filmName\", " +
                "\"f\".\"description\" as \"filmDescription\", " +
                "\"f\".\"releaseDate\" as \"filmReleaseDate\", \"f\".\"duration\" as \"filmDuration\", " +
                "\"f\".\"rate\" as \"filmRate\" , \"r\".\"id\" as \"filmRatingID\", " +
                "\"r\".\"name\" as \"filmRatingName\", \"r\".\"description\" as \"filmRatingDescription\" " +
                "FROM \"films\" as \"f\" JOIN \"ratings\" as \"r\" ON \"r\".\"id\" = \"f\".\"mpa_id\" WHERE \"f\".\"id\" = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeFilm(rs), id);
    }

    @Override
    public Film create(Film object) throws ValidationException {
        this.validate(object);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO \"films\" (\"name\", \"description\", \"releaseDate\", \"duration\", \"rate\", " +
                "\"mpa_id\") " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sql, new String[] {"id"});
            ps.setString(1, object.getName());
            ps.setString(2, object.getDescription());
            ps.setDate(3, Date.valueOf(object.getReleaseDate()));
            ps.setFloat(4, object.getDuration());
            ps.setDouble(5, object.getRate());
            ps.setInt(6, object.getMpa().getId());
            return ps;
        }, keyHolder);
        object.setId(keyHolder.getKey().intValue());
        return object;
    }

    public void addLike(Integer filmId, Integer userId) {
        String sql = "INSERT INTO \"user_film_likes\" (\"film_id\", \"user_id\") VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        String sql = "DELETE FROM \"user_film_likes\" WHERE \"film_id\" = ? AND \"user_id\" = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public Film update(Film updatedObject) throws NotFoundException, ValidationException {
        this.validate(updatedObject);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "UPDATE \"films\" SET \"name\"=?, \"description\"=?, \"releaseDate\"=?, \"duration\"=? ," +
                "\"rate\" = ?, \"mpa_id\" = ?" +
                "WHERE \"id\"=?";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sql, new String[] {"id"});
            ps.setString(1, updatedObject.getName());
            ps.setString(2, updatedObject.getDescription());
            ps.setDate(3, Date.valueOf(updatedObject.getReleaseDate()));
            ps.setFloat(4, updatedObject.getDuration());
            ps.setDouble(5, updatedObject.getRate());
            ps.setInt(6, updatedObject.getMpa().getId());
            ps.setInt(7, updatedObject.getId());
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

    public boolean hasLike(Integer filmId, Integer userId) {
        String sql = "SELECT COUNT(*) FROM \"user_film_likes\" WHERE \"film_id\" = ? AND \"user_id\" = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, filmId, userId);
        return count != null && count != 0;
    }

    @Override
    public List<Film> readAll() {
        String sql = "SELECT \"f\".\"id\" as \"filmID\", \"f\".\"name\" as \"filmName\", " +
                "\"f\".\"description\" as \"filmDescription\", " +
                "\"f\".\"releaseDate\" as \"filmReleaseDate\", \"f\".\"duration\" as \"filmDuration\", " +
                "\"f\".\"rate\" as \"filmRate\" , \"r\".\"id\" as \"filmRatingID\", " +
                "\"r\".\"name\" as \"filmRatingName\", \"r\".\"description\" as \"filmRatingDescription\" " +
                "FROM \"films\" as \"f\" JOIN \"ratings\" as \"r\" ON \"r\".\"id\" = \"f\".\"mpa_id\"";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        int id = rs.getInt("filmID");
        String name = rs.getString("filmName");
        String description = rs.getString("filmDescription");
        LocalDate releaseDate = rs.getDate("filmReleaseDate").toLocalDate();
        Duration duration = Duration.ofMinutes((long) rs.getFloat("filmDuration"));
        Integer rate = rs.getInt("filmRate");
        Integer ratingId = rs.getInt("filmRatingID");
        String ratingName = rs.getString("filmRatingName");
        String ratingDescription = rs.getString("filmRatingDescription");

        return new Film(id, name, description, releaseDate, duration, rate,
                new Rating(ratingId, ratingName, ratingDescription));
    }

    public List<Film> getPopularFilms(int count) {
        String sql = "SELECT \"f\".\"id\" as \"filmID\", \"f\".\"name\" as \"filmName\", " +
                "\"f\".\"description\" as \"filmDescription\", " +
                "\"f\".\"releaseDate\" as \"filmReleaseDate\", \"f\".\"duration\" as \"filmDuration\", " +
                "\"f\".\"rate\" as \"filmRate\" , \"r\".\"id\" as \"filmRatingID\", " +
                "\"r\".\"name\" as \"filmRatingName\", \"r\".\"description\" as \"filmRatingDescription\", " +
                "COUNT(\"f\".\"id\") as \"ctr\" " +
                "FROM \"films\" as \"f\" " +
                "LEFT JOIN \"user_film_likes\" as \"ufl\" ON \"f\".\"id\" = \"ufl\".\"film_id\" " +
                "LEFT JOIN \"ratings\" as \"r\" ON \"r\".\"id\" = \"f\".\"mpa_id\" " +
                "GROUP BY \"f\".\"id\" " +
                "ORDER BY \"ctr\" desc " +
                "LIMIT ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), count);
    }
}
