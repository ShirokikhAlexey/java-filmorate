package ru.yandex.practicum.filmorate.db.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.db.base.FriendsStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
public class FriendsDbStorage implements FriendsStorage<Friends, Integer> {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendsDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Friends read(Integer id) throws NotFoundException {
        String sql = "SELECT * FROM friends f WHERE f.id = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeFriend(rs), id);
    }

    @Override
    public void create(Friends object) throws ValidationException {
        String sql = "INSERT INTO friends (user_id, friend_id, confirmed) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, object.getUserId(), object.getFriendId(), object.isConfirmed());
    }

    @Override
    public void update(Friends updatedObject) throws NotFoundException, ValidationException {
        String sql = "UPDATE friends SET user_id=?, friend_id=?, confirmed=?  WHERE id=?";
        jdbcTemplate.update(sql, updatedObject.getUserId(), updatedObject.getFriendId(),
                updatedObject.isConfirmed(), updatedObject.getId());
    }

    @Override
    public void delete(Integer id) throws NotFoundException {
        String sql = "DELETE FROM friends WHERE id=?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean contains(Integer id) {
        String sql = "SELECT COUNT(*) FROM friends WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count != 0;
    }

    @Override
    public List<Friends> readAll() {
        String sql = "SELECT * FROM friends";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFriend(rs));
    }

    private Friends makeFriend(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        Integer userID = rs.getInt("user_id");
        Integer friendId = rs.getInt("friend_id");
        boolean confirmed = rs.getBoolean("confirmed");

        return new Friends(id, userID, friendId, confirmed);
    }

    @Override
    public Integer getUserFriend(Integer userId, Integer friendId) {
        String sql = "SELECT id FROM friends WHERE user_id = ? AND friend_id=?";
        return jdbcTemplate.queryForObject(sql, Integer.class, userId, friendId);
    }

    @Override
    public void addFriend(Integer user, Integer friend) throws ValidationException {
        Integer likeId = this.getUserFriend(user, friend);
        if (likeId == null) {
            this.create(new Friends(user, friend, false));
        }
    }

    @Override
    public void deleteFriend(Integer user, Integer friend) throws NotFoundException {
        Integer likeId = this.getUserFriend(user, friend);
        if (likeId != null) {
            this.delete(likeId);
        }
    }

    private User makeUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String login = rs.getString("login");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();

        return new User(id, email, login, name, birthday);
    }

    @Override
    public List<User> getUserFriends(Integer userId) {
        String sql = "SELECT u.id as 'id', u.name as 'name', u.email as 'email', u.login as 'login', " +
                "u.birthday as 'birthday'" +
                " FROM friends f" +
                "JOIN users u ON u.id = f.friend_id " +
                "WHERE u.user_id = ? ";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), userId);
    }
}
