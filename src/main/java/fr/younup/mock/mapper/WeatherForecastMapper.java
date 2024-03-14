package fr.younup.mock.mapper;

import fr.younup.mock.dto.WeatherForecastDto;
import fr.younup.mock.entity.WeatherForecast;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WeatherForecastMapper {

    WeatherForecast toEntity(WeatherForecastDto weatherForecastDto);

}
