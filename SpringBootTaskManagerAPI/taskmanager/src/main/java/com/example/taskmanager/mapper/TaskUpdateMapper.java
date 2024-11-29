package com.example.taskmanager.mapper;

import com.example.taskmanager.dto.TaskUpdateRequestDTO;
import com.example.taskmanager.dto.TaskUpdateResponseDTO;
import com.example.taskmanager.entity.TaskUpdate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskUpdateMapper {

    @Mapping(source = "task.taskId", target = "taskId")
    @Mapping(source = "status.statusId", target = "statusId")
    @Mapping(source = "employee.employeeId", target = "employeeId")
    TaskUpdateResponseDTO toDTO(TaskUpdate taskUpdate);

    // Ignore nested associations to prevent transient entity creation
    @Mapping(target = "task", ignore = true)
    @Mapping(target = "comment", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "updateDate", ignore = true) // Set in service
    TaskUpdate toEntity(TaskUpdateRequestDTO taskUpdateRequestDTO);
}
