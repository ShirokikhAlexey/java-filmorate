package ru.yandex.practicum.filmorate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.yandex.practicum.filmorate.db.base.CRUDManager;
import ru.yandex.practicum.filmorate.db.DBManager;

@SpringBootApplication
public class FilmorateApplication {
    public static CRUDManager db;

    public static void main(String[] args) {
        db = DBManager.getMemoryManager();
        SpringApplication.run(FilmorateApplication.class, args);
    }

}
