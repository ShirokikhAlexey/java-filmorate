package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.db.memory.FilmCRUDMemory;
import ru.yandex.practicum.filmorate.error.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Random;

@SpringBootTest
class FilmCRUDMemoryTest {
    FilmCRUDMemory db = new FilmCRUDMemory();

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
    void validateName() throws ValidationException{
        Film film = new Film(1, "", "Test", LocalDate.of(2000, 1, 1),
                Duration.ofHours(1));
        Assertions.assertThrows(ValidationException.class,  () -> db.validate(film));

        film.setName("    ");
        Assertions.assertThrows(ValidationException.class,  () -> db.validate(film));

        film.setName("ValidName");
        db.validate(film);
    }

    @Test
    void validateDescription() throws ValidationException {
        Film film = new Film(1, "Test", "", LocalDate.of(2000, 1, 1),
                Duration.ofHours(1));
        Assertions.assertThrows(ValidationException.class,  () -> db.validate(film));

        film.setDescription("   ");
        Assertions.assertThrows(ValidationException.class,  () -> db.validate(film));

        String invalidDescr = getRandomString(201);
        film.setDescription(invalidDescr);
        Assertions.assertThrows(ValidationException.class,  () -> db.validate(film));

        String validDescr = getRandomString(200);
        film.setDescription(validDescr);

        db.validate(film);
    }

    @Test
    void validateDate() throws ValidationException {
        Film film = new Film(1, "Test", "test", LocalDate.of(1895, 12, 27),
                Duration.ofHours(1));
        Assertions.assertThrows(ValidationException.class,  () -> db.validate(film));

        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        db.validate(film);
    }

    @Test
    void validateDuration() throws ValidationException {
        Film film = new Film(1, "Test", "test", LocalDate.of(1995, 12, 27),
                Duration.ofHours(-1));
        Assertions.assertThrows(ValidationException.class,  () -> db.validate(film));

        film.setDuration(Duration.ofHours(0));
        Assertions.assertThrows(ValidationException.class,  () -> db.validate(film));

        film.setDuration(Duration.ofHours(10));
        db.validate(film);
    }
}
