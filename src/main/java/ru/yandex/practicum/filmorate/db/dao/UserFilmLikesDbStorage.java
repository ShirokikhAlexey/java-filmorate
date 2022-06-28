package ru.yandex.practicum.filmorate.db.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.db.base.UserFilmLikesStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.UserFilmLikes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Repository
public class UserFilmLikesDbStorage implements UserFilmLikesStorage<UserFilmLikes, Integer> {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserFilmLikesDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserFilmLikes read(Integer id) throws NotFoundException {
        String sql = "SELECT * FROM \"user_film_likes\" \"ufl\" WHERE \"ufl.id\" = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeUserFilmLikes(rs), id);
    }

    @Override
    public UserFilmLikes create(UserFilmLikes object) throws ValidationException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO \"user_film_likes\" (\"user_id\", \"film_id\") VALUES (?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sql, new String[] {"id"});
            ps.setInt(1, object.getUserId());
            ps.setInt(2, object.getFilmId());
            return ps;
        }, keyHolder);
        object.setId(keyHolder.getKey().intValue());
        return object;
    }

    @Override
    public UserFilmLikes update(UserFilmLikes updatedObject) throws NotFoundException, ValidationException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "UPDATE \"user_film_likes\" SET \"user_id\"=?, \"film_id\"=?  WHERE \"id\"=?";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sql, new String[] {"id"});
            ps.setInt(1, updatedObject.getUserId());
            ps.setInt(2, updatedObject.getFilmId());
            ps.setInt(3, updatedObject.getId());
            return ps;
        }, keyHolder);
        updatedObject.setId(keyHolder.getKey().intValue());
        return updatedObject;
    }

    @Override
    public void delete(Integer id) throws NotFoundException {
        String sql = "DELETE FROM \"user_film_likes\" WHERE \"id\"=?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean contains(Integer id) {
        String sql = "SELECT COUNT(*) FROM \"user_film_likes\" WHERE \"id\" = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count != 0;
    }

    @Override
    public List<UserFilmLikes> readAll() {
        String sql = "SELECT * FROM \"user_film_likes\"";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUserFilmLikes(rs));
    }

    private UserFilmLikes makeUserFilmLikes(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        Integer userID = rs.getInt("user_id");
        Integer filmID = rs.getInt("film_id");

        return new UserFilmLikes(id, userID, filmID);
    }

    @Override
    public HashSet<Integer> getFilmLikes(Integer filmId) {
        String sql = "SELECT \"user_id\" FROM \"user_film_likes\" WHERE \"film_id\"=?";
        List<Integer> data = jdbcTemplate.queryForList(sql, Integer.class, filmId);
        return new HashSet<>(data);
    }

    @Override
    public HashSet<Integer> getUserLikes(Integer userId) {
        String sql = "SELECT \"film_id\" FROM \"user_film_likes\" WHERE \"user_id\"=?";
        List<Integer> data = jdbcTemplate.queryForList(sql, Integer.class, userId);
        return new HashSet<>(data);
    }

    @Override
    public Integer getUserLike(Integer userId, Integer filmId) {
        String sql = "SELECT \"id\" FROM \"user_film_likes\" WHERE \"user_id\" = ? AND \"film_id\"=?";
        return jdbcTemplate.queryForObject(sql, Integer.class, userId, filmId);
    }

    @Override
    public void addLike(Integer userId, Integer filmId) throws ValidationException {
        Integer likeId = this.getUserLike(userId, filmId);
        if (likeId == null) {
            this.create(new UserFilmLikes(userId, filmId));
        }
    }

    @Override
    public void deleteLike(Integer userId, Integer filmId) throws NotFoundException {
        Integer likeId = this.getUserLike(userId, filmId);
        if (likeId != null) {
            this.delete(likeId);
        }
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

    public List<Film> getPopularFilms(int count) {
        String sql = "SELECT \"f.id\" as \"id\", \"f.name\" as \"name\", \"f.description\" as \"description\", " +
                "\"f.releaseDate\" as \"releaseDate\", \"f.duration\" as \"duration\", \"r.id\" as \"ratingID\", " +
                "\"r.name\" as \"ratingName\", \"r.description\" as \"ratingDescription\", " +
                "COUNT(\"ufl.like_id\") as \"counter\" " +
                "FROM \"user_film_likes\" \"ufl\" " +
                "JOIN \"films\" \"f\" ON \"f.id\" = \"ufl.film_id\" " +
                "JOIN \"ratings\" \"r\" ON \"r.id\" = \"f.rating\" " +
                "GROUP BY \"ufl.film_id\"" +
                "ORDER BY \"counter\" desc " +
                "LIMIT ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), count);
    }

}
