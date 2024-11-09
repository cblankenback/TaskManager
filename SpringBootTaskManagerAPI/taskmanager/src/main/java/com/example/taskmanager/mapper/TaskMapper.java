package com.example.taskmanager.mapper;

import com.example.taskmanager.dto.TaskRequestDTO;
import com.example.taskmanager.dto.TaskResponseDTO;
import com.example.taskmanager.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(source = "createdBy.employeeId", target = "createdById")
    @Mapping(source = "priority.priorityId", target = "priorityId")
    @Mapping(source = "dependencyTask.taskId", target = "dependencyTaskId")
    TaskResponseDTO toDTO(Task task);

    // Ignore nested associations to prevent transient entity creation
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "priority", ignore = true)
    @Mapping(target = "dependencyTask", ignore = true)
    @Mapping(target = "dependentTasks", ignore = true)
    Task toEntity(TaskRequestDTO taskRequestDTO);
}
