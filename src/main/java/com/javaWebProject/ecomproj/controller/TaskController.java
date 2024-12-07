package com.javaWebProject.ecomproj.controller;

import com.javaWebProject.ecomproj.dto.TaskPayload;
import com.javaWebProject.ecomproj.dto.TaskUpdateRequest;
import com.javaWebProject.ecomproj.model.Task;
import com.javaWebProject.ecomproj.model.TaskStatus;
import com.javaWebProject.ecomproj.service.TaskService;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "http://localhost:5173") // Adjust for your frontend
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody TaskPayload payload) {
        try {
            Task createdTask = taskService.createTask(payload);
            return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
        } catch (EntityNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while creating the task.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<Page<Task>> getTasks( 
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) Long employeeId
    ) {
        Integer pageValue =  (Integer)page - 1;
        Page<Task> tasks = taskService.getTasksWithFilters(pageValue, size, status, employeeId);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long taskId) {
        Task task = taskService.getTaskById(taskId);
        return task != null 
            ? new ResponseEntity<>(task, HttpStatus.OK) 
            : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(
            @PathVariable Long taskId,
            @RequestBody TaskUpdateRequest taskUpdateRequest) {
        try {
            Task updatedTask = taskService.updateTask(taskId, taskUpdateRequest);
            return ResponseEntity.ok(updatedTask);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    @DeleteMapping("/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable Long taskId) {
        boolean isDeleted = taskService.deleteTask(taskId);
        return isDeleted
            ? new ResponseEntity<>("Task deleted successfully", HttpStatus.OK)
            : new ResponseEntity<>("Task not found", HttpStatus.NOT_FOUND);
    }
}
