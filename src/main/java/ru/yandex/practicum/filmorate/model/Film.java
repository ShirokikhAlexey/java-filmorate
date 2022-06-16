package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film implements Comparable<Film> {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Duration duration;
    private Set<Integer> likes = new HashSet<>();

    public Film(int id, String name, String description, LocalDate releaseDate, Duration duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public Long getDuration() {
        return this.duration.toSeconds();
    }

    @Override
    public int compareTo(Film o) {
        return Integer.compare(this.likes.size(), o.likes.size());
    }
}
