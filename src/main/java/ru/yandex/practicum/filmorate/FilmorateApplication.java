package ru.yandex.practicum.filmorate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.yandex.practicum.filmorate.db.base.StorageManager;
import ru.yandex.practicum.filmorate.db.DBManager;

@SpringBootApplication
public class FilmorateApplication {
    public static StorageManager db;

    public static void main(String[] args) {
        db = DBManager.getDbManager();
        SpringApplication.run(FilmorateApplication.class, args);
    }

}
