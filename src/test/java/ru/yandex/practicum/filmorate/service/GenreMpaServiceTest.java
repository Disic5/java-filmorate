package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.GenreRepository;
import ru.yandex.practicum.filmorate.storage.RatingRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({GenreRepository.class, RatingRepository.class})
class GenreMpaServiceTest {
    private final GenreRepository genreRepository;
    private final RatingRepository ratingRepository;

    @DisplayName("Получение всех жанров")
    @Test
    void getAllGenres() {
        List<Genre> allGenres = genreRepository.findAll();
        assertEquals(6, allGenres.size());
    }

    @DisplayName("Получение жанров по Id")
    @Test
    void getGenreById() {
        Optional<Genre> byId = genreRepository.findById(1);
        assertTrue(byId.isPresent());
        assertEquals("Комедия", byId.get().getName());
    }

    @DisplayName("Получение всех MPA")
    @Test
    void getAllMpa() {
        List<Rating> allRatings = ratingRepository.findAll();
        assertEquals(5, allRatings.size());
    }

    @DisplayName("Получение Mpa по id")
    @Test
    void getMpaById() {
        Optional<Rating> byId = ratingRepository.findById(1);
        assertTrue(byId.isPresent());
        assertEquals("G", byId.get().getName());
    }
}