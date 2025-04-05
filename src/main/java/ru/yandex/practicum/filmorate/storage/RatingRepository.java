package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RatingRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<Rating> findAll() {
        String sql = "SELECT id, name FROM rating ORDER BY id";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Rating(rs.getInt("id"), rs.getString("name")));
    }

    public Optional<Rating> findById(int id) {
        String sql = "SELECT id, name FROM rating WHERE id = ?";
        try {
            Rating rating = jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                    new Rating(rs.getInt("id"), rs.getString("name")), id);
            return Optional.ofNullable(rating);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public String findRatingNameById(int id) {
        String sql = "SELECT name FROM rating WHERE id = ?";
        List<String> result = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> rs.getString("name"),
                id
        );
        return result.isEmpty() ? null : result.get(0);
    }

    public List<Rating> findAllMpa() {
        String sql = "SELECT * FROM rating ORDER BY id";
        return jdbcTemplate.query(sql, this::mapRowToRating);
    }

    private Rating mapRowToRating(ResultSet rs, int rowNum) throws SQLException {
        return new Rating(rs.getInt("id"), rs.getString("code"));
    }
}
