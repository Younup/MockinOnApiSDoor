package fr.younup.mock.service;

import fr.younup.mock.dto.WeatherForecastDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class WeatherApiService {

    private final RestTemplate restTemplate;

    @Value("${weather.api.url}")
    private String apiUrl;

    @Value("${weather.api.path}")
    private String apiPath;

    public WeatherForecastDto getWeatherForecast(String city) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(apiUrl + apiPath)
                .queryParam("city", city);
        return restTemplate.getForObject(builder.toUriString(), WeatherForecastDto.class);
    }
}
