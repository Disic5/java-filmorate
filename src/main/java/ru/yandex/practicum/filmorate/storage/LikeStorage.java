package ru.yandex.practicum.filmorate.storage;


import java.util.Set;

public interface LikeStorage {
    void addLike(Long filmId, Long userId);

    void removeLike(Long filmId, Long userId);

    boolean exists(Long filmId, Long userId);

    Set<Long> getLikes(Long filmId);
}