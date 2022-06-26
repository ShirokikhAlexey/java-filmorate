package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.db.memory.FilmStorageMemory;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FilmCRUDMemoryTest {
    FilmStorageMemory db = new FilmStorageMemory();

    private String getRandomString(int strLength) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder str = new StringBuilder();
        Random rnd = new Random();
        while (str.length() < strLength) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            str.append(SALTCHARS.charAt(index));
        }
        String result = str.toString();
        return result;
    }

    @Test
    void validateName() throws ValidationException {
        Film film = new Film("", "Test", LocalDate.of(2000, 1, 1),
                Duration.ofHours(1), 1);
        assertThrows(ValidationException.class, () -> db.validate(film));

        film.setName("    ");
        assertThrows(ValidationException.class, () -> db.validate(film));

        film.setName("ValidName");
        db.validate(film);
    }

    @Test
    void validateDescription() throws ValidationException {
        Film film = new Film( "Test", "", LocalDate.of(2000, 1, 1),
                Duration.ofHours(1), 1);
        assertThrows(ValidationException.class, () -> db.validate(film));

        film.setDescription("   ");
        assertThrows(ValidationException.class, () -> db.validate(film));

        String invalidDescr = getRandomString(201);
        film.setDescription(invalidDescr);
        assertThrows(ValidationException.class, () -> db.validate(film));

        String validDescr = getRandomString(200);
        film.setDescription(validDescr);

        db.validate(film);
    }

    @Test
    void validateDate() throws ValidationException {
        Film film = new Film("Test", "test", LocalDate.of(1895, 12, 27),
                Duration.ofHours(1), 1);
        assertThrows(ValidationException.class, () -> db.validate(film));

        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        db.validate(film);
    }

    @Test
    void validateDuration() throws ValidationException {
        Film film = new Film( "Test", "test", LocalDate.of(1995, 12, 27),
                Duration.ofHours(-1), 1);
        assertThrows(ValidationException.class, () -> db.validate(film));

        film.setDuration(Duration.ofHours(0));
        assertThrows(ValidationException.class, () -> db.validate(film));

        film.setDuration(Duration.ofHours(10));
        db.validate(film);
    }
}
