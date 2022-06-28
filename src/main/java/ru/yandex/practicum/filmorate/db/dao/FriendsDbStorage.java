package ru.yandex.practicum.filmorate.db.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.db.base.FriendsStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
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
        String sql = "SELECT * FROM \"friends\" \"f\" WHERE \"f.id\" = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeFriend(rs), id);
    }

    @Override
    public Friends create(Friends object) throws ValidationException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO \"friends\" (\"user_id\", \"friend_id\", \"confirmed\") VALUES (?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sql, new String[] {"id"});
            ps.setInt(1, object.getUserId());
            ps.setInt(2, object.getFriendId());
            ps.setBoolean(3, object.isConfirmed());
            return ps;
        }, keyHolder);
        object.setId(keyHolder.getKey().intValue());
        return object;
    }

    @Override
    public Friends update(Friends updatedObject) throws NotFoundException, ValidationException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "UPDATE \"friends\" SET \"user_id\"=?, \"friend_id\"=?, \"confirmed\"=?  WHERE \"id\"=?";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sql, new String[] {"id"});
            ps.setInt(1, updatedObject.getUserId());
            ps.setInt(2, updatedObject.getFriendId());
            ps.setBoolean(3, updatedObject.isConfirmed());
            ps.setInt(4, updatedObject.getId());
            return ps;
        }, keyHolder);
        updatedObject.setId(keyHolder.getKey().intValue());
        return updatedObject;
    }

    @Override
    public void delete(Integer id) throws NotFoundException {
        String sql = "DELETE FROM \"friends\" WHERE \"id\"=?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean contains(Integer id) {
        String sql = "SELECT COUNT(*) FROM \"friends\" WHERE \"id\" = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count != 0;
    }

    @Override
    public List<Friends> readAll() {
        String sql = "SELECT * FROM \"friends\"";

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
        String sql = "SELECT \"id\" FROM \"friends\" WHERE \"user_id\" = ? AND \"friend_id\"=?";
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
        String sql = "SELECT \"users\".\"id\" as \"id\", \"users\".\"name\" as \"name\", \"users\".\"email\" as \"email\", " +
                "\"users\".\"login\" as \"login\", \"users\".\"birthday\" as \"birthday\" " +
                "FROM \"friends\" " +
                "JOIN \"users\" ON \"users\".\"id\" = \"friends\".\"friend_id\" " +
                "WHERE \"friends\".\"user_id\" = ? ";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), userId);
    }
}
