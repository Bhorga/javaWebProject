package com.javaWebProject.ecomproj.service;

import com.javaWebProject.ecomproj.dto.TaskPayload;
import com.javaWebProject.ecomproj.dto.TaskUpdateRequest;
import com.javaWebProject.ecomproj.model.Employee;
import com.javaWebProject.ecomproj.model.Task;
import com.javaWebProject.ecomproj.model.TaskStatus;
import com.javaWebProject.ecomproj.repo.EmployeeRepository;
import com.javaWebProject.ecomproj.repo.TaskRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public Task createTask(TaskPayload payload) {
        Employee employee = employeeRepository.findById(payload.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + payload.getEmployeeId()));

        Task task = new Task();
        task.setTitle(payload.getTitle());
        task.setDescription(payload.getDescription());
        task.setStatus(payload.getStatus());
        task.setEmployee(employee);
        task.setCreatedAt(payload.getCreatedAt());

        return taskRepository.save(task);
    }

    public Page<Task> getTasksWithFilters(int page, int size, TaskStatus status, Long employeeId) {
        Pageable pageable = PageRequest.of(page, size);
        if (status != null && employeeId != null) {
            return taskRepository.findByStatusAndEmployeeId(status, employeeId, pageable);
        } else if (status != null) {
            return taskRepository.findByStatus(status, pageable);
        } else if (employeeId != null) {
            return taskRepository.findByEmployeeId(employeeId, pageable);
        } else {
            return taskRepository.findAll(pageable);
        }
    }

    // Get task by ID
    public Task getTaskById(Long taskId) {
        Optional<Task> task = taskRepository.findById(taskId);
        return task.orElse(null);
    }


    public Task updateTask(Long taskId, TaskUpdateRequest taskUpdateRequest) {
    Task existingTask = getTaskById(taskId);
    if (existingTask != null) {
        existingTask.setTitle(taskUpdateRequest.getTitle());
        existingTask.setDescription(taskUpdateRequest.getDescription());
        existingTask.setStatus(TaskStatus.valueOf(taskUpdateRequest.getStatus()));

        Long employeeId = taskUpdateRequest.getEmployee_id();
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));
        existingTask.setEmployee(employee);

        return taskRepository.save(existingTask);
    }
    return null;
}

    public boolean deleteTask(Long taskId) {
        Task existingTask = getTaskById(taskId);
        if (existingTask != null) {
            taskRepository.delete(existingTask);
            return true;
        }
        return false;
    }
}
