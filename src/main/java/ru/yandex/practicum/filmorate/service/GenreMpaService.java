package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.GenreRepository;
import ru.yandex.practicum.filmorate.storage.RatingRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreMpaService {
    private final GenreRepository genreRepository;
    private final RatingRepository ratingRepository;

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    public Genre getGenreById(int id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Жанр с id=" + id + " не найден"));
    }

    public List<Rating> getAllMpa() {
        return ratingRepository.findAll();
    }

    public Rating getMpaById(int id) {
        return ratingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Рейтинг с id=" + id + " не найден"));
    }

    public void validateGenres(List<Genre> genres) {
        if (genres == null || genres.isEmpty()) return;

        List<Integer> genreIds = genres.stream()
                .map(Genre::getId)
                .distinct()
                .collect(Collectors.toList());

        List<Integer> notFound = genreRepository.findMissingIds(genreIds);
        if (!notFound.isEmpty()) {
            throw new NotFoundException("Жанры с id " + notFound + " не найдены");
        }
    }

    public void validateRating(Rating rating) {
        if (rating == null || rating.getId() == 0) return;

        String name = ratingRepository.findRatingNameById(rating.getId());
        if (name == null) {
            throw new NotFoundException("Рейтинг с id=" + rating.getId() + " не найден");
        }

        rating.setName(name);
    }
}