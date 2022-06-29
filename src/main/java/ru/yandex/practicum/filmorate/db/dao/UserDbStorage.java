package ru.yandex.practicum.filmorate.db.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.db.base.UserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

@Repository
public class UserDbStorage implements UserStorage<User, Integer> {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User read(Integer id) throws NotFoundException {
        String sql = "SELECT * FROM \"users\" WHERE \"id\" = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeUser(rs), id);
    }

    @Override
    public User create(User object) throws ValidationException {
        this.validate(object);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO \"users\" (\"name\", \"email\", \"login\", \"birthday\") VALUES(?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sql, new String[] {"id"});
            ps.setString(1, object.getName());
            ps.setString(2, object.getEmail());
            ps.setString(3, object.getLogin());
            ps.setDate(4, Date.valueOf(object.getBirthday()));
            return ps;
        }, keyHolder);
        object.setId(keyHolder.getKey().intValue());
        return object;
    }

    @Override
    public User update(User updatedObject) throws NotFoundException, ValidationException {
        this.validate(updatedObject);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "UPDATE \"users\" SET \"name\"=?, \"email\"=?, \"login\"=?, \"birthday\"=? WHERE \"id\"=?";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sql, new String[] {"id"});
            ps.setString(1, updatedObject.getName());
            ps.setString(2, updatedObject.getEmail());
            ps.setString(3, updatedObject.getLogin());
            ps.setDate(4, Date.valueOf(updatedObject.getBirthday()));
            ps.setInt(5, updatedObject.getId());
            return ps;
        }, keyHolder);
        updatedObject.setId(keyHolder.getKey().intValue());
        return updatedObject;
    }

    @Override
    public void delete(Integer id) throws NotFoundException {
        String sql = "DELETE FROM \"users\" WHERE \"id\"=?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean contains(Integer id) {
        String sql = "SELECT COUNT(*) FROM \"users\" WHERE \"id\" = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count != 0;
    }

    @Override
    public List<User> readAll() {
        String sql = "SELECT * FROM \"users\"";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    private User makeUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String login = rs.getString("login");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();

        return new User(id, email, login, name, birthday);
    }

    public void addFriend(Integer userId, Integer friendId) throws ValidationException {
        String sql = "INSERT INTO \"friends\" (\"user_id\", \"friend_id\", \"confirmed\") VALUES(?, ?, ?)";
        jdbcTemplate.update(sql, userId, friendId, false);
    }

    public void deleteFriend(Integer userId, Integer friendId) throws ValidationException {
        String sql = "DELETE FROM \"friends\" WHERE \"user_id\" = ? AND \"friend_id\" = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    public List<User> getUserFriends(Integer userId) throws NotFoundException {
        String sql = "SELECT \"u\".\"id\" as \"id\", \"u\".\"name\" as \"name\", \"u\".\"email\" as \"email\", " +
                "\"u\".\"login\" as \"login\", \"u\".\"birthday\" as \"birthday\" " +
                "FROM \"friends\" \"f\" " +
                "JOIN \"users\" \"u\" on \"u\".\"id\" = \"f\".\"friend_id\" " +
                "WHERE \"f\".\"user_id\" = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), userId);
    }
}
