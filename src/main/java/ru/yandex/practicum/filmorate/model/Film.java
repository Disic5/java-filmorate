package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.annotation.PositiveDuration;
import ru.yandex.practicum.filmorate.annotation.ValidReleaseDate;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Film.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    private Long id;

    @NotBlank(message = "Название не может быть пустым")
    private String name;

    @Size(max = 200, message = "Максимальная длина описания не должна превышать 200 символов")
    @NotBlank(message = "Описание не должно быть пустым")
    private String description;

    @NotNull(message = "Дата релиза не должна быть пустой")
    @ValidReleaseDate
    private LocalDate releaseDate;

    @PositiveDuration
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Duration duration;
}