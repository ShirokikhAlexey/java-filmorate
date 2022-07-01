package ru.yandex.practicum.filmorate.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.db.memory.StorageManagerMemory;

@Component
public class DBManager {
    public static StorageManagerMemory getMemoryManager() {
        return new StorageManagerMemory();
    }
}
