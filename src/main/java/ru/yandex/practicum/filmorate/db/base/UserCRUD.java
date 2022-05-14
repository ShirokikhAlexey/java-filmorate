package ru.yandex.practicum.filmorate.db.base;

import ru.yandex.practicum.filmorate.error.NotFoundError;
import ru.yandex.practicum.filmorate.error.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

public interface UserCRUD <model, primaryKeyType> extends BaseCRUD <model, primaryKeyType>{
    List<model> readAll() throws NotFoundError;

    default void validate(User user) throws ValidationException {
        if (user.getEmail().isBlank() || !(user.getEmail().contains("@"))) {
            throw new ValidationException();
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException();
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException();
        }
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

    }
}
