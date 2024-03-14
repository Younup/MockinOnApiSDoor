package fr.younup.mock.entity;

import jakarta.persistence.*;
import lombok.Data;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String status;
    private String location;
    @OneToOne(cascade = CascadeType.ALL)
    private WeatherForecast weatherForecast;
}
