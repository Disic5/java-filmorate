package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class})
class FilmorateApplicationTests {

    private final UserDbStorage userStorage;

    @Test
    public void testFindUserById() {
        // Подготовка: сначала добавим пользователя, чтобы точно был в базе
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testuser");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        User created = userStorage.createUser(user);

        // Тест: получить по ID
        User userById = userStorage.findById(created.getId());

        // Проверки
        assertThat(userById).isNotNull();
        assertThat(userById.getId()).isEqualTo(created.getId());
        assertThat(userById.getEmail()).isEqualTo("test@example.com");
        assertThat(userById.getLogin()).isEqualTo("testuser");
        assertThat(userById.getName()).isEqualTo("Test User");
        assertThat(userById.getBirthday()).isEqualTo(LocalDate.of(1990, 1, 1));
    }

    @Test
    void testCreateUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testuser");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        User created = userStorage.createUser(user);

        assertThat(created.getId()).isPositive();
        assertThat(userStorage.findById(created.getId())).isEqualTo(created);
    }
}