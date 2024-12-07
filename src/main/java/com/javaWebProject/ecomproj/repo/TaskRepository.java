package com.javaWebProject.ecomproj.repo;

import com.javaWebProject.ecomproj.model.Task;
import com.javaWebProject.ecomproj.model.TaskStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

    // Pagination with status filter
    Page<Task> findByStatus(TaskStatus status, Pageable pageable);

    // Pagination with employee ID filter
    Page<Task> findByEmployeeId(Long employeeId, Pageable pageable);

    // Pagination with both status and employee ID filters
    Page<Task> findByStatusAndEmployeeId(TaskStatus status, Long employeeId, Pageable pageable);
}
