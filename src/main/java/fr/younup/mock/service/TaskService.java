package fr.younup.mock.service;

import fr.younup.mock.entity.Task;
import fr.younup.mock.entity.WeatherForecast;
import fr.younup.mock.exception.ResourceNotFoundException;
import fr.younup.mock.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final WeatherForecastService weatherForecastService;

    public Page<Task> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task createTask(Task task) {
        WeatherForecast weatherForecast = weatherForecastService.createWeatherForecast(task.getWeatherForecast());
        task.setWeatherForecast(weatherForecast);
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task taskDetails) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));

        task.setName(taskDetails.getName());
        task.setDescription(taskDetails.getDescription());
        task.setStatus(taskDetails.getStatus());

        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));

        taskRepository.delete(task);
    }
}
