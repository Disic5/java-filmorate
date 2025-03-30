package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;

    public List<Film> findAllFilms() {
        return filmStorage.findAll().stream().toList();
    }

    public void deleteFilm(Long id) {
        filmStorage.deleteFilm(id);
    }

    public void addLikeFilm(Long filmId, Long userId) {
        Film film = getFilmById(filmId);
        User user = userStorage.findById(userId);
        boolean existsLikes = existsLikes(film.getId(), user.getId());
        if (existsLikes) {
            throw new ValidationException("Lke", "userId", "Пользователь уже ставил лайк");
        }
        likeStorage.addLike(filmId, userId);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public void removeLikeFilm(Long filmId, Long userId) {
        Film film = getFilmById(filmId);
        User user = userStorage.findById(userId);
        boolean existsLikes = existsLikes(film.getId(), user.getId());
        if (!existsLikes) {
            throw new ValidationException("Lke", "userId", " фильма нет лайков");
        }

        likeStorage.removeLike(filmId, userId);
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id);
    }

    public Set<Long> getLikes(Long filmId) {
        return likeStorage.getLikes(filmId);
    }

    private boolean existsLikes(Long filmId, Long userId) {
        return likeStorage.exists(filmId, userId);
    }

    public List<Film> findPopularFilms(int count) {
        return filmStorage.findPopularFilm(count);
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }
}
