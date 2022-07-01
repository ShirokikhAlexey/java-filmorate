package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.db.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.db.dao.RatingDbStorage;
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
	private final RatingDbStorage ratingStorage;

	@Test
	public void testCreateUser()  throws ValidationException {
		User testUser = new User("test@test.test", "test", "test", LocalDate.of(2000, 1, 1));
		User created = userStorage.create(testUser);
		Assertions.assertTrue(userStorage.contains(created.getId()));
	}

	@Test
	public void testFindUserById() throws NotFoundException, ValidationException {
		User testUser = new User("testFind@test.test", "testFind", "testFind",
				LocalDate.of(2000, 1, 1));
		User created = userStorage.create(testUser);

		User user = userStorage.read(created.getId());
		Assertions.assertEquals(user, created);
	}

	@Test
	public void testUpdateUser() throws NotFoundException, ValidationException {
		User testUser = new User("test@test.test", "test", "test", LocalDate.of(2000, 1, 1));
		User createdUser = userStorage.create(testUser);
		createdUser.setLogin("testNew");

		userStorage.update(createdUser);

		User updatedUser = userStorage.read(createdUser.getId());
		Assertions.assertEquals(createdUser, updatedUser);
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
		User testUser = new User("test@test.test", "test", "test", LocalDate.of(2000, 1, 1));
		User created = userStorage.create(testUser);
		Assertions.assertTrue(userStorage.contains(created.getId()));

		userStorage.delete(created.getId());
		Assertions.assertFalse(userStorage.contains(created.getId()));
	}

	@Test
	public void testCreateFilm()  throws ValidationException {
		Film testFilm = new Film("Test", "Test", LocalDate.of(2000, 1, 1), 1, 1, new Rating(1, "test", "test"), null);
		Film createdFilm = filmStorage.create(testFilm);
		Assertions.assertTrue(filmStorage.contains(createdFilm.getId()));
	}

	@Test
	public void testFindFilmById() throws NotFoundException, ValidationException {
		Film testFilm = new Film("Test", "Test", LocalDate.of(2000, 1, 1), 1, 1, new Rating(1, "test", "test"), null);
		Film created = filmStorage.create(testFilm);
		Rating rating = ratingStorage.read(created.getMpa().getId());
		created.setMpa(rating);

		Film film = filmStorage.read(created.getId());
		Assertions.assertEquals(film, created);
	}

	@Test
	public void testUpdateFilm() throws NotFoundException, ValidationException {
		Film firstFilm =  new Film("Test", "Test", LocalDate.of(2000, 1, 1), 1, 1, new Rating(1, "test", "test"), null);
		Film createdFilm = filmStorage.create(firstFilm);
		createdFilm.setName("testNew");
		Rating rating = ratingStorage.read(firstFilm.getMpa().getId());
		createdFilm.setMpa(rating);
		filmStorage.update(createdFilm);

		Film updatedFilm = filmStorage.read(createdFilm.getId());
		Assertions.assertEquals(createdFilm, updatedFilm);
	}

	@Test
	public void testGetAllFilms() throws NotFoundException, ValidationException {
		Film firstFilm =  new Film("Test", "Test", LocalDate.of(2000, 1, 1), 1, 1, new Rating(1, "test", "test"), null);
		filmStorage.create(firstFilm);
		Film secondFilm =  new Film("TestNew", "TestNew", LocalDate.of(2000, 1, 1), 1, 1, new Rating(1, "test", "test"), null);
		filmStorage.create(secondFilm);

		List<Film> inDbFilms = filmStorage.readAll();
		Assertions.assertEquals(inDbFilms.toArray().length, 2);
	}

	@Test
	public void testDeleteFilm() throws NotFoundException, ValidationException {
		Film testFilm = new Film("Test", "Test", LocalDate.of(2000, 1, 1), 1, 1, new Rating(1, "test", "test"), null);
		Film createdFilm = filmStorage.create(testFilm);
		Assertions.assertTrue(filmStorage.contains(createdFilm.getId()));

		filmStorage.delete(createdFilm.getId());
		Assertions.assertFalse(filmStorage.contains(createdFilm.getId()));
	}
}
