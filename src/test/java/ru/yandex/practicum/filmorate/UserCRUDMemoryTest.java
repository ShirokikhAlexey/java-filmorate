package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.db.memory.FilmCRUDMemory;
import ru.yandex.practicum.filmorate.db.memory.UserCRUDMemory;
import ru.yandex.practicum.filmorate.error.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.time.LocalDate;

@SpringBootTest
class UserCRUDMemoryTest {
    UserCRUDMemory db = new UserCRUDMemory();

    @Test
    void validateEmail() throws ValidationException {
        User user = new User(1, "", "Test", "test", LocalDate.of(2000, 1, 1));
        Assertions.assertThrows(ValidationException.class,  () -> db.validate(user));

        user.setEmail("    ");
        Assertions.assertThrows(ValidationException.class,  () -> db.validate(user));

        user.setEmail("InvalidEmail");
        Assertions.assertThrows(ValidationException.class,  () -> db.validate(user));

        user.setEmail("valid@email.test");
        db.validate(user);
    }

    @Test
    void validateLogin() throws ValidationException {
        User user = new User(1, "test@test.test", "", "test", LocalDate.of(2000, 1, 1));
        Assertions.assertThrows(ValidationException.class,  () -> db.validate(user));

        user.setLogin("Invalid Login");
        Assertions.assertThrows(ValidationException.class,  () -> db.validate(user));

        user.setLogin("ValidLogin");
        db.validate(user);
    }

    @Test
    void validateDate() throws ValidationException {
        User user = new User(1, "test@test.test", "test", "test", LocalDate.now().plusDays(1));
        Assertions.assertThrows(ValidationException.class,  () -> db.validate(user));

        user.setBirthday(LocalDate.now());
        db.validate(user);
    }

    @Test
    void validateName() throws ValidationException {
        User user = new User(1, "test@test.test", "test", "", LocalDate.of(2000, 1, 1));
        db.validate(user);

        Assertions.assertEquals(user.getName(), user.getLogin());
    }
}
