package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Set;

public interface UserStorage {
    Collection<User> findAll();

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(Long id);

    User findById(Long id);

    Set<Long> findFriends(Long userId);

    void addNewFriend(Long userId, Long friendId);

    boolean areFriends(Long userId, Long friendId);

    void removeFriends(Long userId, Long friendId);
}
