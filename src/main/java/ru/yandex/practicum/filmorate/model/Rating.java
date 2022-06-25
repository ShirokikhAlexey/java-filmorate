package ru.yandex.practicum.filmorate.model;

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
}
