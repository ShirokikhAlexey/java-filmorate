package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.db.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.db.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
	private final UserDbStorage userStorage;
	private final FilmDbStorage filmStorage;

	@Test
	public void testCreateUser()  throws ValidationException {
		Assertions.assertFalse(userStorage.contains(1));
		User testUser = new User("test@test.test", "test", "test", LocalDate.of(2000, 1, 1));
		userStorage.create(testUser);
		Assertions.assertTrue(userStorage.contains(1));
	}

	@Test
	public void testFindUserById() throws NotFoundException {
		User user = userStorage.read(1);
		Assertions.assertEquals(user, new User(1, "test@test.test", "test", "test",
				LocalDate.of(2000, 1, 1)));
	}

	@Test
	public void testUpdateUser() throws NotFoundException, ValidationException {
		User user = userStorage.read(1);
		user.setLogin("testNew");

		userStorage.update(user);

		User updatedUser = userStorage.read(1);
		Assertions.assertEquals(user, updatedUser);
	}

	@Test
	public void testGetAllUser() throws NotFoundException, ValidationException {
		User secondUser =  new User("testNew@test.test", "testNew", "testNew",
				LocalDate.of(2000, 1, 1));
		userStorage.create(secondUser);

		List<User> inDbUsers = userStorage.readAll();
		Assertions.assertEquals(inDbUsers.toArray().length, 2);
	}

	@Test
	public void testDeleteUser() throws NotFoundException, ValidationException {
		userStorage.delete(1);
		assertThrows(NotFoundException.class, () -> userStorage.read(1));
	}

	@Test
	public void testCreateFilm()  throws ValidationException {
		Assertions.assertFalse(filmStorage.contains(1));
		Film testFilm = new Film("Test", "Test", LocalDate.of(2000, 1, 1), 1, 1, new Rating(1, "test", "test"), null);
		filmStorage.create(testFilm);
		Assertions.assertTrue(filmStorage.contains(1));
	}

	@Test
	public void testFindFilmById() throws NotFoundException {
		Film film = filmStorage.read(1);
		Assertions.assertEquals(film, new Film("Test", "Test", LocalDate.of(2000, 1, 1), 1, 1, new Rating(1, "test", "test"), null));
	}

	@Test
	public void testUpdateFilm() throws NotFoundException, ValidationException {
		Film film = filmStorage.read(1);
		film.setName("testNew");

		filmStorage.update(film);

		Film updatedFilm = filmStorage.read(1);
		Assertions.assertEquals(film, updatedFilm);
	}

	@Test
	public void testGetAllFilms() throws NotFoundException, ValidationException {
		Film secondFilm =  new Film("TestNew", "TestNew", LocalDate.of(2000, 1, 1), 1, 1, new Rating(1, "test", "test"), null);
		filmStorage.create(secondFilm);

		List<Film> inDbFilms = filmStorage.readAll();
		Assertions.assertEquals(inDbFilms.toArray().length, 2);
	}

	@Test
	public void testDeleteFilm() throws NotFoundException, ValidationException {
		filmStorage.delete(1);
		assertThrows(NotFoundException.class, () -> filmStorage.read(1));
	}
}
