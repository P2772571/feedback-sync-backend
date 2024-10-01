package com.example.feedbacksync.controller;

import com.example.feedbacksync.payloads.task.TaskRequest;
import com.example.feedbacksync.service.TaskService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * TaskController class to handle task related operations
 */
@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "http://localhost:5173")
public class TaskController {


    private final TaskService taskService;

    /**
     * Constructor
     * @param taskService TaskService
     */
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Create a task for a goal
     * @param taskRequest TaskRequest
     * @return ResponseEntity<?>
     */ 
    @PostMapping
        public ResponseEntity<?> createTask( @RequestBody TaskRequest taskRequest) {
        try{
            return ResponseEntity.ok(taskService.createTask(taskRequest));


        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Update a task
     * @param taskId Long taskId
     * @param taskRequest TaskRequest
     * @return ResponseEntity<?>
     */
    @PutMapping("/{taskId}")
    public ResponseEntity<?> updateTask(@PathVariable Long taskId , @RequestBody TaskRequest taskRequest) {
        try{
            return ResponseEntity.ok(taskService.updateTask(taskId, taskRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        
    }

    /**
     * Delete a task by taskId
     * @param taskId Long taskId
     * @return ResponseEntity<?>  
     */
    @DeleteMapping ("/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Long taskId) {
        try{
            if (!taskService.deleteTask(taskId)) {
                throw new Exception("Task not found");
            }
            return ResponseEntity.ok("Task deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



}
