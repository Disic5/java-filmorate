package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> findAllFilms() {
        return filmService.findAllFilms();
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Film>> findPopularFilms(@RequestParam(defaultValue = "10") int count) {
        List<Film> popularFilms = filmService.findPopularFilms(count);
        return new ResponseEntity<>(popularFilms, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable Long id) {
        Film film = filmService.getFilmById(id);
        return ResponseEntity.ok(film);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        try {
            filmService.createFilm(film);
            log.info("Film created: {}", film);
        } catch (ValidationException e) {
            log.error(e.getMessage());
        }
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> likeFilm(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLikeFilm(id, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        return filmService.updateFilm(newFilm);
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@Valid @PathVariable Long id) {
        try {
            filmService.deleteFilm(id);
            log.info("Film deleted: {}", id);
        } catch (ValidationException e) {
            log.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> removeLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.removeLikeFilm(id, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
