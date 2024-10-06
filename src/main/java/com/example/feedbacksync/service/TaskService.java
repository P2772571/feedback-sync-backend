package com.example.feedbacksync.service;

import com.example.feedbacksync.entity.enums.GoalStatus;
import com.example.feedbacksync.entity.enums.PipStatus;
import org.springframework.stereotype.Service;

import com.example.feedbacksync.entity.Goal;
import com.example.feedbacksync.entity.Pip;
import com.example.feedbacksync.entity.Task;
import com.example.feedbacksync.payloads.task.TaskRequest;
import com.example.feedbacksync.payloads.task.TaskResponse;
import com.example.feedbacksync.repository.GoalsRepository;
import com.example.feedbacksync.repository.PipRepository;
import com.example.feedbacksync.repository.TaskRepository;

import java.util.*;;

/**
 * TaskService class to handle task related operations
 */
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final GoalsRepository goalsRepository;
    private final PipRepository pipRepository;

    /**
     * Constructor
     * @param taskRepository TaskRepository
     * @param goalsRepository GoalsRepository
     * @param pipRepository PipRepository
     */
    public TaskService(TaskRepository taskRepository, GoalsRepository goalsRepository, PipRepository pipRepository) {
        this.taskRepository = taskRepository;
        this.goalsRepository = goalsRepository;
        this.pipRepository = pipRepository;
    }

    /**
     * Create a task for a goal
     * @param taskRequest TaskRequest
     * @return TaskResponse
     */
    public TaskResponse createTask (TaskRequest taskRequest) {
        Goal goal = taskRequest.getGoalId() != null ? goalsRepository.findById(taskRequest.getGoalId()).orElse(null):null;
        Pip pip = taskRequest.getPipId() != null ? pipRepository.findById(taskRequest.getPipId()).orElse(null): null;
        if (goal != null) {
           return createTask(goal, taskRequest);
        }
        else if (pip  != null){
            return  createTask(pip, taskRequest);
        }
        else {
            throw new IllegalArgumentException("Either GoalId or PipId is incorrect");
        }
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
        task.setTaskName(taskRequest.getTaskName());
        task.setIsCompleted(taskRequest.getIsCompleted() != null && taskRequest.getIsCompleted());

        task = taskRepository.save(task);

        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setTaskId(task.getTaskId());
        taskResponse.setTaskName(task.getTaskName());
        taskResponse.setIsCompleted(task.getIsCompleted());
        taskResponse.setGoalId(task.getGoal() != null ? task.getGoal().getGoalId() : null);
        taskResponse.setPipId(task.getPip() != null ? task.getPip().getPipId() : null);

        if (task.getGoal() != null) {
            updateGoalProgress(task.getGoal());
        }

        
        return taskResponse;
    }

    /**
     * Create a task for a pip
     * @param pip Pip
     * @param taskRequest String
     * @return TaskResponse 
     */
    public TaskResponse createTask (Pip pip, TaskRequest taskRequest) {
        Task task = new Task();
        task.setPip(pip);
        task.setTaskName(taskRequest.getTaskName());
        task.setIsCompleted(taskRequest.getIsCompleted() != null ? taskRequest.getIsCompleted() : false);

        task = taskRepository.save(task);

        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setTaskId(task.getTaskId());
        taskResponse.setTaskName(task.getTaskName());
        taskResponse.setIsCompleted(task.getIsCompleted());
        taskResponse.setGoalId(task.getGoal() != null ? task.getGoal().getGoalId() : null);
        taskResponse.setPipId(task.getPip() != null ? task.getPip().getPipId() : null);

         if (task.getPip() != null) {
             updatePipProgress(task.getPip());
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

        task.setTaskName(taskRequest.getTaskName());
            task.setIsCompleted(taskRequest.getIsCompleted() != null ? taskRequest.getIsCompleted() : false);



        task = taskRepository.save(task);

        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setTaskId(task.getTaskId());
        taskResponse.setTaskName(task.getTaskName());
        taskResponse.setIsCompleted(task.getIsCompleted());
        taskResponse.setGoalId(task.getGoal() != null ? task.getGoal().getGoalId() : null);
        taskResponse.setPipId(task.getPip() != null ? task.getPip().getPipId() : null);

        if(task.getGoal() != null) {
            updateGoalProgress(task.getGoal());
        }

        if(task.getPip() != null) {
            updatePipProgress(task.getPip());
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
                if (task.getPip() != null) {
                    updatePipProgress(task.getPip());
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
     * Get all tasks for a pip
     * @param pip Pip
     * @return List of TaskResponse
     */
    public List<TaskResponse> getTasksByPip (Pip pip) {
        List<Task> tasks = taskRepository.findAllByPip(pip);
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
        taskResponse.setGoalId(task.getGoal() != null ? task.getGoal().getGoalId() : null);
        taskResponse.setPipId(task.getPip() != null ? task.getPip().getPipId() : null);

        return taskResponse;
    }

    /**
     * Update the progress of a goal
     * @param goal Goal
     */
    public void updateGoalProgress(Goal goal) {
        List<Task> tasks = taskRepository.findAllByGoal(goal);
        long completedTasks = tasks.stream().filter(Task::getIsCompleted).count();
        int totalTasks = tasks.size();

        // Calculate progress as a percentage
        int progress = (int) ((completedTasks * 100.0) / totalTasks);
        if (progress == 100) {
            goal.setStatus(GoalStatus.COMPLETED);
        }
        else if (progress > 0) {
            goal.setStatus(GoalStatus.IN_PROGRESS);
        }
        else {
            goal.setStatus(GoalStatus.PENDING);
        }


        goal.setProgress(progress);
        goalsRepository.save(goal);
    }

    /**
     * Update the progress of a pip
     * @param pip Pip
     */
    public void updatePipProgress(Pip pip) {
        List<Task> tasks = taskRepository.findAllByPip(pip);
        long completedTasks = tasks.stream().filter(Task::getIsCompleted).count();
        int totalTasks = tasks.size();

        // Calculate progress as a percentage
        int progress = (int) ((completedTasks * 100.0) / totalTasks);

        if (progress == 100) {
            pip.setStatus(PipStatus.COMPLETED);
        }
        else if (progress > 0) {
            pip.setStatus(PipStatus.ACTIVE);
        }
        else {
            pip.setStatus(PipStatus.PENDING);
        }

        pip.setProgress(progress);
        pipRepository.save(pip);
    }
        
    
}
