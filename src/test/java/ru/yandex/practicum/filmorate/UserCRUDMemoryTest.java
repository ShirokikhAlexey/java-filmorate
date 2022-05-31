package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.db.memory.UserCRUDMemory;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserCRUDMemoryTest {
    UserCRUDMemory db = new UserCRUDMemory();

    @Test
    void validateEmail() throws ValidationException {
        User user = new User(1, "", "Test", "test", LocalDate.of(2000, 1, 1));
        assertThrows(ValidationException.class, () -> db.validate(user));

        user.setEmail("    ");
        assertThrows(ValidationException.class, () -> db.validate(user));

        user.setEmail("InvalidEmail");
        assertThrows(ValidationException.class, () -> db.validate(user));

        user.setEmail("valid@email.test");
        db.validate(user);
    }

    @Test
    void validateLogin() throws ValidationException {
        User user = new User(1, "test@test.test", "", "test", LocalDate.of(2000, 1, 1));
        assertThrows(ValidationException.class, () -> db.validate(user));

        user.setLogin("Invalid Login");
        assertThrows(ValidationException.class, () -> db.validate(user));

        user.setLogin("ValidLogin");
        db.validate(user);
    }

    @Test
    void validateDate() throws ValidationException {
        User user = new User(1, "test@test.test", "test", "test", LocalDate.now().plusDays(1));
        assertThrows(ValidationException.class, () -> db.validate(user));

        user.setBirthday(LocalDate.now());
        db.validate(user);
    }

    @Test
    void validateName() throws ValidationException {
        User user = new User(1, "test@test.test", "test", "", LocalDate.of(2000, 1, 1));
        db.validate(user);

        assertEquals(user.getName(), user.getLogin());
    }
}
