package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private Integer id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

    @JsonCreator
    public User(@JsonProperty("id") int id, @JsonProperty("email") String email, @JsonProperty("login") String login,
                @JsonProperty("name") String name, @JsonProperty("birthday") LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    @JsonCreator
    public User(@JsonProperty("email") String email, @JsonProperty("login") String login,
                @JsonProperty("name") String name, @JsonProperty("birthday") LocalDate birthday) {
        this.id = null;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
