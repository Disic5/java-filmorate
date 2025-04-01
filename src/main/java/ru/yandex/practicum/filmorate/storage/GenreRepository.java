package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class GenreRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<Genre> findAll() {
        String sql = "SELECT * FROM genre ORDER BY id";
        return jdbcTemplate.query(sql, this::mapRowToGenre);
    }

    public Optional<Genre> findById(int id) {
        String sql = "SELECT * FROM genre WHERE id = ?";
        List<Genre> result = jdbcTemplate.query(sql, this::mapRowToGenre, id);
        return result.stream().findFirst();
    }

    public List<Integer> findMissingIds(List<Integer> ids) {
        String inSql = ids.stream().map(id -> "?").collect(Collectors.joining(", ", "(", ")"));
        String sql = "SELECT id FROM genre WHERE id IN " + inSql;
        List<Integer> existing = jdbcTemplate.query(
                sql,
                ids.toArray(),
                (rs, rowNum) -> rs.getInt("id")
        );
        return ids.stream()
                .filter(id -> !existing.contains(id))
                .collect(Collectors.toList());
    }

    private Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
        Genre genre = new Genre();
        genre.setId(rs.getInt("id"));
        genre.setName(rs.getString("name"));
        return genre;
    }
}
