package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.FriendDto;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorageImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private UserService userService;
    private InMemoryUserStorageImpl storage;
    private User user;

    @BeforeEach
    void setUp() {
        storage = new InMemoryUserStorageImpl();
        userService = new UserService(storage);
        user = User.builder().id(1L).name("user").build();
    }

    @Test
    @DisplayName("Вывести всех друзей пользователя")
    void findAllFriendTest() {
        User friend = User.builder().id(2L).name("friend").build();
        User friend2 = User.builder().id(3L).name("friend").build();
        storage.save(user);
        storage.save(friend);
        storage.save(friend2);
        boolean isFriend = userService.addFriends(user.getId(), friend.getId());
        boolean isFriend2 = userService.addFriends(user.getId(), friend2.getId());
        List<FriendDto> userFriend = userService.findAllFriend(user.getId());
        assertTrue(isFriend);
        assertTrue(isFriend2);
        assertEquals(2, userFriend.size());
    }

    @Test
    @DisplayName("Вывести всех общих друзей пользователя")
    void findCommonFriendsTest() {
        User otherUser = User.builder().id(4L).name("friend").build();
        User friend = User.builder().id(2L).name("friend").build();
        User friend2 = User.builder().id(3L).name("friend").build();
        storage.save(user);
        storage.save(otherUser);
        storage.save(friend);
        storage.save(friend2);
        userService.addFriends(user.getId(), friend.getId());
        userService.addFriends(otherUser.getId(), friend.getId());
        List<FriendDto> commonFriends1 = userService.findCommonFriends(user.getId(), otherUser.getId());
        assertEquals(1, commonFriends1.size());

        userService.addFriends(user.getId(), friend2.getId());
        userService.addFriends(otherUser.getId(), friend2.getId());
        List<FriendDto> commonFriends = userService.findCommonFriends(user.getId(), otherUser.getId());
        assertEquals(2, commonFriends.size());
    }


    @Test
    @DisplayName("Успешное добавление в друзья")
    void addFriendsSuccessTest() {
        User friend = User.builder().id(2L).name("friend").build();
        storage.save(user);
        storage.save(friend);
        boolean added = userService.addFriends(user.getId(), friend.getId());
        assertTrue(added);
    }

    @Test
    @DisplayName("Ошибка нельзя добавить самого себя")
    void addFriendsErrorTest() {
        storage.save(user);
        assertThrows(ValidationException.class, () -> userService.addFriends(user.getId(), user.getId()));
    }

    @Test
    @DisplayName("Удаление из друзей по id")
    void deleteFriendByIdTest() {
        User friend = User.builder().id(2L).name("friend").build();
        storage.save(user);
        storage.save(friend);

        boolean added = userService.addFriends(user.getId(), friend.getId());
        assertTrue(added);
        List<FriendDto> userFriend = userService.findAllFriend(user.getId());
        assertEquals(1, userFriend.size());

        boolean deleted = userService.deleteFriendById(user.getId(), friend.getId());
        List<FriendDto> deleteFriend = userService.findFriends(user.getId());
        assertTrue(deleted);
        assertTrue(deleteFriend.isEmpty());
    }
}