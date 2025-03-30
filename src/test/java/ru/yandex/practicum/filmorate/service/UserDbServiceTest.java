package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class})
class UserDbServiceTest {
    private final UserDbStorage userStorage;

    @DisplayName("Успешный поиск пользователя по id")
    @Test
    void getUserById() {
        User user = userStorage.findById(1L);
        assertEquals(1L, user.getId());
        assertEquals("Alice", user.getName());
    }

    @DisplayName("Получение всех пользователей")
    @Test
    void findFriends() {
        Collection<User> users = userStorage.findAll();
        assertEquals(3, users.size());
    }

    @DisplayName("Успешный поиск общих друзей пользователя")
    @Test
    void findCommonFriends() {
        boolean areFriends = userStorage.areFriends(1L, 2L);
        assertTrue(areFriends);
    }

    @DisplayName("Получение всех друзей")
    @Test
    void findAllFriend() {
        Set<Long> friends = userStorage.findFriends(1L);
        assertEquals(2, friends.size());
    }

    @DisplayName("Успешный проверка друга пользователя")
    @Test
    void addFriends() {
        boolean areFriends = userStorage.areFriends(1L, 2L);
        assertTrue(areFriends);
    }

    @DisplayName("Успешный удаления друга по id")
    @Test
    void deleteFriendById() {
        userStorage.removeFriends(1L, 2L);
        boolean areFriends = userStorage.areFriends(1L, 2L);
        assertFalse(areFriends);
    }
}