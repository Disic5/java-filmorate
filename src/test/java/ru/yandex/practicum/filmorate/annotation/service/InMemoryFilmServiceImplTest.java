package ru.yandex.practicum.filmorate.annotation.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorageImpl;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryFilmServiceImplTest {
    private FilmStorage filmService;
    private Film film;

    @BeforeEach
    void setUp() {
        filmService = new InMemoryFilmStorageImpl();
        film = Film.builder()
                .id(1L)
                .name("Film 1")
                .duration(30)
                .description("Film 1 description")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .build();
    }

    @DisplayName("Успешное создание фильма")
    @Test
    void testCreateFilm() {
        filmService.createFilm(film);
        Collection<Film> allFilms = filmService.findAll();

        assertTrue(allFilms.contains(film));
        assertEquals(1, allFilms.size());
    }

    @DisplayName("Успешное обновление фильма")
    @Test
    void testUpdateFilm() {
        film.setId(2L);
        filmService.createFilm(film);
        Film film1 = Film.builder()
                .id(2L)
                .name("Film 2")
                .duration(30)
                .description("Film 2 description")
                .releaseDate(LocalDate.of(2024, 10, 10))
                .build();
        Film updateFilm = filmService.updateFilm(film1);
        assertNotNull(updateFilm);
        assertEquals(film1.getId(), updateFilm.getId());
        assertEquals(film1.getName(), updateFilm.getName());
    }

    @DisplayName("Ошибка обновление фильма")
    @Test
    void testUpdateFilmFailed() {
        Exception exception = assertThrows(NotFoundException.class, () -> filmService.updateFilm(film));
        assertEquals("Film not found", exception.getMessage());
    }

    @Test
    void deleteFilm() {
        filmService.createFilm(film);
        Collection<Film> all = filmService.findAll();
        assertTrue(all.contains(film));
        filmService.deleteFilm(film.getId());
        assertTrue(all.isEmpty());
    }
}