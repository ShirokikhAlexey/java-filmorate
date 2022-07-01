package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Rating {
    private Integer id;
    private String name;
    private String description;


    public Rating(String name, String description) {
        this.id = null;
        this.name = name;
        this.description = description;
    }


    public Rating(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    @JsonCreator
    public Rating(@JsonProperty("id") Integer id) {
        this.id = id;
    }
}
