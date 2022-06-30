package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Data
public class Film {
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Duration duration;
    private Integer rate;
    private Rating mpa;
    private List<Genre> genres;

    @JsonCreator
    public Film(@JsonProperty("name") String name, @JsonProperty("description") String description,
                @JsonProperty("releaseDate") LocalDate releaseDate, @JsonProperty("duration") Integer duration,
                @JsonProperty("rate") Integer rate, @JsonProperty("mpa") Rating mpa,
                @JsonProperty("genres") List<Genre> genres) {
        this.id = null;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = Duration.ofMinutes(duration);
        this.rate = rate;
        this.mpa = mpa;
        this.genres = null;

        if(genres != null) {
            this.genres = new ArrayList<>();
            for(Genre genre : genres){
                if(!this.genres.contains(genre)) {
                    this.genres.add(genre);
                }
            }
        }

        if(rate == null){
            this.rate = 0;
        }
    }

    public Film(Integer id, String name, String description, LocalDate releaseDate, Duration duration, Integer rate,
                Rating mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
        this.mpa = mpa;
    }

    public Long getDuration() {
        return this.duration.toMinutes();
    }
}
