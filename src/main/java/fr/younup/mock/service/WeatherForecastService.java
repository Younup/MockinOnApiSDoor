package fr.younup.mock.service;

import fr.younup.mock.entity.WeatherForecast;
import fr.younup.mock.repository.WeatherForecastRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WeatherForecastService {

    private final WeatherForecastRepository weatherForecastRepository;

    public WeatherForecast createWeatherForecast(WeatherForecast weatherForecast) {
        return weatherForecastRepository.save(weatherForecast);
    }

}
