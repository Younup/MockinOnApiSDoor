package fr.younup.mock.mapper;

import fr.younup.mock.dto.TaskDto;
import fr.younup.mock.entity.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    Task toEntity(TaskDto taskDto);

    TaskDto toDto(Task task);
}
