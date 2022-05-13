package ru.yandex.practicum.filmorate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.yandex.practicum.filmorate.db.base.CRUDManager;
import ru.yandex.practicum.filmorate.db.dbManager;

@SpringBootApplication
public class FilmorateApplication {
	public static CRUDManager db;
	public static void main(String[] args) {
		db = dbManager.getMemoryManager();
		SpringApplication.run(FilmorateApplication.class, args);
	}

}
