package ru.yandex.practicum.filmorate.db;

import ru.yandex.practicum.filmorate.db.memory.CRUDManagerMemory;

public class dbManager {
    public static CRUDManagerMemory getMemoryManager() {
        return new CRUDManagerMemory();
    }
}
