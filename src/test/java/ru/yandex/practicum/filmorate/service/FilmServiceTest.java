//package ru.yandex.practicum.filmorate.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.springframework.jdbc.core.JdbcTemplate;
//import ru.yandex.practicum.filmorate.exception.NotFoundException;
//import ru.yandex.practicum.filmorate.model.Film;
//import ru.yandex.practicum.filmorate.model.User;
//import ru.yandex.practicum.filmorate.storage.*;
//
//import java.util.List;
//import java.util.Set;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class FilmServiceTest {
//    private FilmService service;
//    private UserStorage userStorage;
//    private LikeStorage likeStorage;
//    private Film film;
//    private User user;
//
//    @BeforeEach
//    void setUp() {
//        FilmStorage filmStorage = new InMemoryFilmStorageImpl();
//        userStorage = new InMemoryUserStorageImpl();
//        JdbcTemplate jdbcTemplate = new JdbcTemplate();
//        likeStorage = new LikeDbStorage(jdbcTemplate);
//        service = new FilmService(filmStorage, userStorage, likeStorage);
//        film = Film.builder().id(1L).name("Test").build();
//        user = User.builder().id(1L).name("user").build();
//    }
//
//    @DisplayName("Получение всех фильмов")
//    @Test
//    void findAllFilmsSuccess() {
//        service.createFilm(film);
//        List<Film> allFilms = service.findAllFilms();
//        assertEquals(1, allFilms.size());
//    }
//
//    @DisplayName("Успешное удаление фильма по id")
//    @Test
//    void deleteFilmSuccess() {
//        service.createFilm(film);
//        service.deleteFilm(film.getId());
//        List<Film> allFilms = service.findAllFilms();
//        assertEquals(0, allFilms.size());
//    }
//
//    @DisplayName("Успешное добавление лайка")
//    @Test
//    void addLikeFilm() {
//        service.createFilm(film);
//        userStorage.createUser(user);
//        service.addLikeFilm(film.getId(), user.getId());
//        Set<Long> likesFilm = service.getLikes(film.getId());
//        assertEquals(1, likesFilm.size());
//    }
//
//    @DisplayName("Ошибка добавление лайка")
//    @Test
//    void addLikeFilmFailure() {
//        userStorage.createUser(user);
//        assertThrows(NotFoundException.class, () -> service.addLikeFilm(film.getId(), user.getId()));
//    }
//
//    @DisplayName("Успешное удаление лайка у фильма по id")
//    @Test
//    void removeLikeFilm() {
//        service.createFilm(film);
//        userStorage.createUser(user);
//        service.addLikeFilm(film.getId(), user.getId());
//        Set<Long> likesFilm = service.getLikes(film.getId());
//        service.removeLikeFilm(film.getId(), user.getId());
//        assertEquals(0, likesFilm.size());
//    }
//
//    @DisplayName("Успешное получение лайков у фильма")
//    @Test
//    void getLikes() {
//        service.createFilm(film);
//        userStorage.createUser(user);
//        service.addLikeFilm(film.getId(), user.getId());
//        Set<Long> likes = service.getLikes(film.getId());
//        assertEquals(1, likes.size());
//    }
//
//    @DisplayName("Успешное удаление фильма по id")
//    @Test
//    void findPopularFilms() {
//        Film film2 = Film.builder().id(2L).name("Test").build();
//        Film film3 = Film.builder().id(3L).name("Test").build();
//        User user2 = User.builder().id(2L).name("user2").build();
//        User user3 = User.builder().id(3L).name("user2").build();
//        service.createFilm(film);
//        service.createFilm(film2);
//        service.createFilm(film3);
//        userStorage.createUser(user);
//        userStorage.createUser(user2);
//        userStorage.createUser(user3);
//        //1 film
//        service.addLikeFilm(film.getId(), user.getId());
//        service.addLikeFilm(film.getId(), user2.getId());
//        service.addLikeFilm(film.getId(), user3.getId());
//        //2 film
//        service.addLikeFilm(film2.getId(), user.getId());
//        //3 film
//        service.addLikeFilm(film3.getId(), user.getId());
//        service.addLikeFilm(film3.getId(), user2.getId());
//        service.addLikeFilm(film3.getId(), user3.getId());
//        List<Film> popularFilms = service.findPopularFilms(2);
//        assertEquals(2, popularFilms.size());
//        assertTrue(popularFilms.contains(film));
//        assertTrue(popularFilms.contains(film3));
//        assertFalse(popularFilms.contains(film2));
//    }
//}