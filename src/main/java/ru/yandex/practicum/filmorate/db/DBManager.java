package ru.yandex.practicum.filmorate.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.db.dao.StorageManagerDb;
import ru.yandex.practicum.filmorate.db.memory.StorageManagerMemory;

@Component
public class DBManager {
    private static JdbcTemplate jdbcTemplate;

    public DBManager(JdbcTemplate jdbcTemplate){
        DBManager.jdbcTemplate = jdbcTemplate;
    }

    public static StorageManagerMemory getMemoryManager() {
        return new StorageManagerMemory();
    }

    public static StorageManagerDb getDbManager() {
        return new StorageManagerDb(jdbcTemplate);
    }
}
