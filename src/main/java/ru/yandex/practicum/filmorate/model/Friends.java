package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Friends {
    private Integer id;
    private Integer userId;
    private Integer friendId;
    private boolean confirmed;

    public Friends(Integer userId, Integer friendId, boolean confirmed) {
        this.id = null;
        this.userId = userId;
        this.friendId = friendId;
        this.confirmed = confirmed;
    }

    public Friends(Integer id, Integer userId, Integer friendId, boolean confirmed) {
        this.id = id;
        this.userId = userId;
        this.friendId = friendId;
        this.confirmed = confirmed;
    }
}
