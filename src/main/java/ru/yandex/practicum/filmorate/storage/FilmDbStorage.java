package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.*;
import java.util.Collection;
import java.util.List;

@Primary
@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Film> findAll() {
        String sql = "SELECT * FROM film";
        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    @Override
    public Film createFilm(Film film) {
        // Вставка фильма
        String sql = "INSERT INTO film (name, description, release_date, duration, rating_id) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, Math.toIntExact(film.getDuration()));
            ps.setObject(5, film.getMpa() != null ? film.getMpa().getId() : null);
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            film.setId(keyHolder.getKey().longValue());
        }

        // Добавление жанров
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            String genreSql = "MERGE INTO film_genre (film_id, genre_id) KEY(film_id, genre_id) VALUES (?, ?)";
            jdbcTemplate.batchUpdate(genreSql, film.getGenres(), film.getGenres().size(),
                    (ps, genre) -> {
                        ps.setLong(1, film.getId());
                        ps.setLong(2, genre.getId());
                    });
        }

        // Подгружаем жанры
        String genreSelectSql = "SELECT g.id, g.name FROM genre g JOIN film_genre fg ON g.id = fg.genre_id WHERE fg.film_id = ?";
        List<Genre> genres = jdbcTemplate.query(genreSelectSql, (rs, rowNum) -> {
            Genre genre = new Genre();
            genre.setId(rs.getInt("id"));
            genre.setName(rs.getString("name"));
            return genre;
        }, film.getId());
        film.setGenres(genres);

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "UPDATE film SET name = ?, description = ?, release_date = ?, duration = ? WHERE id = ?";
        int updated = jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getId());

        if (updated == 0) {
            throw new NotFoundException("Film not found");
        }
        return film;
    }

    @Override
    public void deleteFilm(Long id) {
        int deleted = jdbcTemplate.update("DELETE FROM film WHERE id = ?", id);
        if (deleted == 0) {
            throw new NotFoundException("Film not found");
        }
    }

    @Override
    public Film getFilmById(Long id) {
        try {
            String sql = "SELECT * FROM film WHERE id = ?";
            Film film = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Film f = new Film();
                f.setId(rs.getLong("id"));
                f.setName(rs.getString("name"));
                f.setDescription(rs.getString("description"));
                f.setReleaseDate(rs.getDate("release_date").toLocalDate());
                f.setDuration(rs.getInt("duration"));
                Integer ratingId = rs.getObject("rating_id", Integer.class);
                if (ratingId != null) {
                    String ratingName = jdbcTemplate.queryForObject(
                            "SELECT name FROM rating WHERE id = ?", String.class, ratingId);
                    f.setMpa(new Rating(ratingId, ratingName));
                }
                return f;
            }, id);

            // Жанры
            String genreSql = """
                        SELECT g.id, g.name
                        FROM genre g
                        JOIN film_genre fg ON g.id = fg.genre_id
                        WHERE fg.film_id = ?
                    """;
            List<Genre> genres = jdbcTemplate.query(genreSql, (rs, rowNum) -> {
                Genre genre = new Genre();
                genre.setId(rs.getInt("id"));
                genre.setName(rs.getString("name"));
                return genre;
            }, id);
            film.setGenres(genres);

            return film;
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Фильм с id=" + id + " не найден");
        }
    }

    public List<Genre> getGenres(Long filmId) {
        String sql = """
                    SELECT g.id, g.name
                    FROM genre g
                    JOIN film_genre fg ON g.id = fg.genre_id
                    WHERE fg.film_id = ?
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Genre genre = new Genre();
            genre.setId(rs.getInt("id"));
            genre.setName(rs.getString("name"));
            return genre;
        }, filmId);
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));

        Integer ratingId = rs.getObject("rating_id", Integer.class);
        if (ratingId != null) {
            String ratingName = jdbcTemplate.queryForObject(
                    "SELECT name FROM rating WHERE id = ?", String.class, ratingId);
            film.setMpa(new Rating(ratingId, ratingName));
        }
        return film;
    }

    public List<Film> findPopularFilm(int count) {
        String sql = """
                    SELECT f.*
                    FROM film f
                    LEFT JOIN film_likes fl ON f.id = fl.film_id
                    GROUP BY f.id
                    ORDER BY COUNT(fl.user_id) DESC
                    LIMIT ?
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Film film = mapRowToFilm(rs, rowNum);
            film.setGenres(getGenres(film.getId()));
            return film;
        }, count);
    }
}
