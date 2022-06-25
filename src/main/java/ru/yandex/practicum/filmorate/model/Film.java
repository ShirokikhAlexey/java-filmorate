package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film implements Comparable<Film> {
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Duration duration;
    private Integer ratingID;
    private Rating rating;
    private Set<Integer> likes = new HashSet<>();

    public Film(String name, String description, LocalDate releaseDate, Duration duration, Integer ratingID) {
        this.id = null;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.ratingID = ratingID;
    }

    public Film(Integer id, String name, String description, LocalDate releaseDate, Duration duration,
                Integer ratingID, Rating rating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.ratingID = ratingID;
        this.rating = rating;
    }

    public Long getDuration() {
        return this.duration.toMinutes();
    }

    @Override
    public int compareTo(Film o) {
        return Integer.compare(this.likes.size(), o.likes.size());
    }
}
