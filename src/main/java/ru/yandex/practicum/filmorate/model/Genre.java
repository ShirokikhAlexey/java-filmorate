package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Genre {
    private Integer id;
    private String name;
    private String description;

    @JsonCreator
    public Genre(@JsonProperty("name") String name, @JsonProperty("description") String description) {
        this.id = null;
        this.name = name;
        this.description = description;
    }

    @JsonCreator
    public Genre(@JsonProperty("id") Integer id, @JsonProperty("name") String name,
                 @JsonProperty("description") String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
