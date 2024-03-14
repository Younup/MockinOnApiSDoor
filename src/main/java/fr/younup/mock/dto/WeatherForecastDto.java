package fr.younup.mock.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record WeatherForecastDto(Long id, String temperature, String description) {
}
