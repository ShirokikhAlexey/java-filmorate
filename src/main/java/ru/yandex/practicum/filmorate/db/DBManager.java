package ru.yandex.practicum.filmorate.db;

import ru.yandex.practicum.filmorate.db.memory.StorageManagerMemory;

public class DBManager {
    public static StorageManagerMemory getMemoryManager() {
        return new StorageManagerMemory();
    }
}
