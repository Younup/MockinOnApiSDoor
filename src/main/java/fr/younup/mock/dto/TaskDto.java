package fr.younup.mock.dto;

import fr.younup.mock.entity.WeatherForecast;
import lombok.Builder;

@Builder(toBuilder = true)
public record TaskDto(Long id, String name, String description, String status, String location,
                      WeatherForecast weatherForecast) {
}
