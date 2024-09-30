package com.example.feedbacksync.service;

import org.springframework.stereotype.Service;

import com.example.feedbacksync.entity.Goal;
import com.example.feedbacksync.entity.Task;
import com.example.feedbacksync.payloads.task.TaskRequest;
import com.example.feedbacksync.payloads.task.TaskResponse;
import com.example.feedbacksync.repository.GoalsRepository;
import com.example.feedbacksync.repository.TaskRepository;

import java.util.*;;

/**
 * TaskService class to handle task related operations
 */
@Service
public class TaskService {

    private TaskRepository taskRepository;
    private GoalsRepository goalsRepository;

    /**
     * Constructor
     * @param taskRepository TaskRepository
     * @param goalsRepository GoalsRepository
     */
    public TaskService(TaskRepository taskRepository, GoalsRepository goalsRepository) {
        this.taskRepository = taskRepository;
        this.goalsRepository = goalsRepository;
    }

    /**
     * Create a task for a goal
     * @param goalId Long 
     * @param taskRequest TaskRequest
     * @return TaskResponse
     */
    public TaskResponse createTask (Long goalId, TaskRequest taskRequest) {
        Goal goal = goalsRepository.findById(goalId).orElse(null);
        if (goal == null) {
            return null;
        }

        return createTask(goal, taskRequest);
    }

    /**
     * Create a task for a goal
     * @param goal Goal
     * @param taskRequest TaskRequest
     * @return TaskResponse
     */ 
    public TaskResponse createTask (Goal goal, TaskRequest taskRequest) {
        

        Task task = new Task();
        task.setGoal(goal);
        task.setTaskName(taskRequest.getTitle());
        task.setIsCompleted(taskRequest.getIsCompleted() != null ? taskRequest.getIsCompleted() : false);

        taskRepository.save(task);

        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setTaskId(task.getTaskId());
        taskResponse.setTaskName(task.getTaskName());
        taskResponse.setIsCompleted(task.getIsCompleted());

        if (task.getGoal() != null) {
            updateGoalProgress(task.getGoal());
        }
        return taskResponse;
    }

    /**
     * Update a task
     * @param taskId Task
     * @param taskRequest TaskRequest
     * @return TaskResponse
     */
    public TaskResponse updateTask (Long taskId, TaskRequest taskRequest) {
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null) {
            return null;
        }

        task.setTaskName(taskRequest.getTitle());
        task.setIsCompleted(taskRequest.getIsCompleted() != null ? taskRequest.getIsCompleted() : false);

        taskRepository.save(task);

        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setTaskId(task.getTaskId());
        taskResponse.setTaskName(task.getTaskName());
        taskResponse.setIsCompleted(task.getIsCompleted());

        if(task.getGoal() != null) {
            updateGoalProgress(task.getGoal());
        }

        return taskResponse;
    }


    /**
     * Delete a task
     * @param taskId Long
     */
    public boolean deleteTask (Long taskId) {
       Task task = taskRepository.findById(taskId).orElse(null);
         if (task != null) {
                deleteTask(task);
                if (task.getGoal() != null) {
                    updateGoalProgress(task.getGoal());
                }
                return true;
         }
            return false;


    }

    /**
     * Delete a task
     * @param task Task
     */
    public void deleteTask (Task task) {
        taskRepository.delete(task);
    }

    /**
     * Get a task by taskId
     * @param taskId Long
     * @return Task
     */
    public Task getTaskById (Long taskId) {
        return taskRepository.findById(taskId).orElse(null);
    }


    /**
     * Get all tasks for a goal 
     * @param goal Goal
     * @return List of TaskResponse
     */
    public List<TaskResponse> getTasksByGoal (Goal goal) {
        List<Task> tasks = taskRepository.findAllByGoal(goal);
        return getTaskResponses(tasks);
    }

    /**
     * Get all tasks for a goal 
     * @param tasks List of Task
     * @return List of TaskResponse
     */
    private List<TaskResponse> getTaskResponses (List<Task> tasks) {
        return tasks.stream().map(this::getTaskResponse).toList();
    }

    /**
     * Convert Task to TaskResponse
     * @param task Task
     * @return TaskResponse
     */
    private TaskResponse getTaskResponse (Task task) {
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setTaskId(task.getTaskId());
        taskResponse.setTaskName(task.getTaskName());
        taskResponse.setIsCompleted(task.getIsCompleted());

        return taskResponse;
    }

    /**
     * Update the progress of a goal
     * @param goal Goal
     */
    public void updateGoalProgress(Goal goal) {
        List<Task> tasks = taskRepository.findAllByGoal(goal);
        long completedTasks = tasks.stream().filter(task -> task.getIsCompleted()).count();
        int totalTasks = tasks.size();

        // Calculate progress as a percentage
        int progress = (int) ((completedTasks * 100.0) / totalTasks);

        goal.setProgress(progress);
        goalsRepository.save(goal);
    }
        
    
}
