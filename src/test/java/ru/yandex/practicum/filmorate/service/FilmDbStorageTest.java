package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmDbStorage.class, UserDbStorage.class, LikeDbStorage.class})
class FilmDbStorageTest {

    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;
    private final LikeDbStorage likeStorage;

    @Test
    @DisplayName("Успешное добавление лайка")
    void addLikeFilm() {
        User user1 = userStorage.createUser(new User(1L, "mail@test.ru", "Name", "login", LocalDate.of(1990, 1, 1)));
        User user2 = userStorage.createUser(new User(2L, "mail@test.ru", "Name", "login", LocalDate.of(1990, 1, 1)));
        User user3 = userStorage.createUser(new User(3L, "mail@test.ru", "Name", "login", LocalDate.of(1990, 1, 1)));
        User user4 = userStorage.createUser(new User(4L, "mail@test.ru", "Name", "login", LocalDate.of(1990, 1, 1)));

        Film film = filmStorage.createFilm(new Film(0L, "Film", "desc", LocalDate.of(2000, 1, 1), 100, List.of(), new Rating(1, null)));

        likeStorage.addLike(film.getId(), user1.getId());
        likeStorage.addLike(film.getId(), user2.getId());
        likeStorage.addLike(film.getId(), user3.getId());
        likeStorage.addLike(film.getId(), user4.getId());

        List<Film> popular = filmStorage.findPopularFilm(10);
        assertThat(popular).isNotEmpty();
        assertThat(popular.get(0).getId()).isEqualTo(film.getId());
    }

    @DisplayName("Получение всех фильмов")
    @Test
    void findAllFilmsSuccess() {
        Collection<Film> films = filmStorage.findAll();
        assertEquals(3, films.size());
    }

    @DisplayName("Успешное удаление фильма по id")
    @Test
    void deleteFilmSuccess() {
        Film film = filmStorage.createFilm(new Film(0L, "Film", "desc", LocalDate.of(2000, 1, 1), 100, List.of(), new Rating(1, null)));
        filmStorage.deleteFilm(film.getId());
        Collection<Film> films = filmStorage.findAll();
        assertEquals(3, films.size());
    }


    @DisplayName("Успешное получение лайков у фильма")
    @Test
    void getLikes() {
        User user = userStorage.createUser(new User(1L, "mail@test.ru", "Name", "login", LocalDate.of(1990, 1, 1)));
        Film film = filmStorage.createFilm(new Film(1L, "Film", "desc", LocalDate.of(2000, 1, 1), 100, List.of(), new Rating(1, null)));
        likeStorage.addLike(film.getId(), user.getId());
        Set<Long> likes = likeStorage.getLikes(film.getId());
        assertEquals(1, likes.size());
    }

    @DisplayName("Успешное удаление лайка у фильма")
    @Test
    void removeLike() {
        User user = userStorage.createUser(new User(1L, "mail@test.ru", "Name", "login", LocalDate.of(1990, 1, 1)));
        Film film = filmStorage.createFilm(new Film(1L, "Film", "desc", LocalDate.of(2000, 1, 1), 100, List.of(), new Rating(1, null)));
        likeStorage.addLike(film.getId(), user.getId());
        likeStorage.removeLike(film.getId(), user.getId());
        Set<Long> likes = likeStorage.getLikes(film.getId());
        assertThat(likes).isEmpty();
    }
}
