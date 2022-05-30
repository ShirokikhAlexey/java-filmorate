package ru.yandex.practicum.filmorate.db.base;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

public interface FilmCRUD<M, K> extends BaseCRUD<M, K> {
    List<M> readAll();

    default void validate(Film film) throws ValidationException {
        if (film.getName().isBlank()) {
            throw new ValidationException();
        }
        if (film.getDescription().isBlank() || film.getDescription().length() > 200) {
            throw new ValidationException();
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException();
        }
        if (film.getDuration().isNegative() || film.getDuration().isZero()) {
            throw new ValidationException();
        }

    }
}
