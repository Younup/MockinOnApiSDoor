package fr.younup.mock.controller;

import fr.younup.mock.dto.TaskDto;
import fr.younup.mock.entity.Task;
import fr.younup.mock.entity.WeatherForecast;
import fr.younup.mock.exception.ResourceNotFoundException;
import fr.younup.mock.mapper.TaskMapper;
import fr.younup.mock.mapper.WeatherForecastMapper;
import fr.younup.mock.service.TaskService;
import fr.younup.mock.service.WeatherApiService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final WeatherApiService weatherApiService;
    private final TaskMapper taskMapper;
    private final WeatherForecastMapper weatherForecastMapper;

    @GetMapping
    public Page<TaskDto> getAllTasks(Pageable pageable) {
        return taskService.getAllTasks(pageable).map(taskMapper::toDto);
    }

    @GetMapping("/{id}")
    public TaskDto getTaskById(@PathVariable Long id) {
        return taskMapper.toDto(taskService.getTaskById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id)));
    }

    @PostMapping()
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto taskDto) {
        // Appel à l'API externe pour obtenir la météo pour la localisation de la tâche
        WeatherForecast weatherForecast = weatherForecastMapper.toEntity(weatherApiService.getWeatherForecast(taskDto.location()));
        Task task = taskMapper.toEntity(taskDto);
        task.setWeatherForecast(weatherForecast);
        TaskDto createdTask = taskMapper.toDto(taskService.createTask(task));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @PutMapping("/{id}")
    public TaskDto updateTask(@PathVariable Long id, @RequestBody TaskDto taskDetails) {
        Task task = taskMapper.toEntity(taskDetails);
        return taskMapper.toDto(taskService.updateTask(id, task));
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
