package ru.yandex.practicum.filmorate.db.base;

import ru.yandex.practicum.filmorate.error.NotFoundError;
import ru.yandex.practicum.filmorate.error.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

public interface FilmCRUD<model, primaryKeyType> extends BaseCRUD<model, primaryKeyType> {
    List<model> readAll() throws NotFoundError;

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
