package ru.yandex.practicum.filmorate.db.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.db.base.GenreStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GenreDbStorage implements GenreStorage<Genre, Integer> {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre read(Integer id) throws NotFoundException {
        String sql = "SELECT * FROM \"genres\" WHERE \"id\" = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeGenre(rs), id);
    }

    @Override
    public Genre create(Genre object) throws ValidationException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO \"genres\" (\"name\", \"description\") VALUES (?, ?)";
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
    public Genre update(Genre updatedObject) throws NotFoundException, ValidationException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "UPDATE \"genres\" SET \"name\"=?, \"description\"=?  WHERE \"id\"=?";
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
        String sql = "DELETE FROM \"genres\" WHERE \"id\"=?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean contains(Integer id) {
        String sql = "SELECT COUNT(*) FROM \"genres\" WHERE \"id\" = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count != 0;
    }

    @Override
    public List<Genre> readAll() {
        String sql = "SELECT * FROM \"genres\"";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    public List<Genre> getFilmGenres(Integer filmId) {
        String sql = "SELECT \"g\".\"id\" as \"id\", \"g\".\"name\" as \"name\", " +
                "\"g\".\"description\" as \"description\" " +
                "FROM \"film_genre\" as \"fg\" " +
                "LEFT JOIN \"genres\" as \"g\" on \"g\".\"id\" = \"fg\".\"genre_id\" " +
                "WHERE \"fg\".\"film_id\" = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), filmId);
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");

        return new Genre(id, name, description);
    }

    public void addFilmGenre(Integer filmId, Integer genreId) {
        String sql = "SELECT COUNT(*) FROM \"film_genre\" WHERE \"film_id\" = ? AND \"genre_id\" = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, filmId, genreId);
        if (count == null || count == 0) {
            String sqlInsert = "INSERT INTO \"film_genre\" (\"film_id\", \"genre_id\") VALUES(?, ?)";
            jdbcTemplate.update(sqlInsert, filmId, genreId);
        }
    }

    public void deleteFilmGenre(Integer filmId, Integer genreId) {
        String sqlInsert = "DELETE FROM \"film_genre\" WHERE \"film_id\" = ? AND \"genre_id\" = ?";
        jdbcTemplate.update(sqlInsert, filmId, genreId);
    }

    public void updateFilmGenres(Film film) {
        List<Genre> currentGenres = getFilmGenres(film.getId());
        List<Integer> newGenres = new ArrayList<>();

        if (film.getGenres() != null){
            for (Genre genre : film.getGenres()){
                newGenres.add(genre.getId());
            }
        }

        for (Genre genre : currentGenres) {
            if (!newGenres.contains(genre.getId())) {
                deleteFilmGenre(film.getId(), genre.getId());
            }
        }
        for (Integer genreId : newGenres){
            addFilmGenre(film.getId(), genreId);
        }

    }
}
