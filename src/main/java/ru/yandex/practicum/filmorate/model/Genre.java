package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Genre {
    private Integer id;
    private String name;
    private String description;

    public Genre(String name, String description) {
        this.id = null;
        this.name = name;
        this.description = description;
    }

    public Genre(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    @JsonCreator
    public Genre(@JsonProperty("id") Integer id) {
        this.id = id;
    }
}
