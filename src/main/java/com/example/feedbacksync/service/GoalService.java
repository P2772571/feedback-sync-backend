package com.example.feedbacksync.service;

import com.example.feedbacksync.entity.Goal;
import com.example.feedbacksync.entity.User;
import com.example.feedbacksync.payloads.goals.GoalRequest;
import com.example.feedbacksync.payloads.goals.GoalResponse;
import com.example.feedbacksync.payloads.profile.ProfileResponse;
import com.example.feedbacksync.repository.GoalsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * GoalService class to handle goal related Business logic and operations
 * @Service annotation is used to mark the class as a service provider in Spring
 */
@Service
public class GoalService {

    private final GoalsRepository goalsRepository;
    private final UserService userService;
    private final ProfileService profileService;
    private final TaskService taskService;


    /**
     * This is the constructor of the GoalService class.
     * The constructor takes GoalsRepository, UserService, and ProfileService as arguments.
     * @param goalsRepository  GoalsRepository object to interact with the Goal table in the database.
     * @param userService UserService object to interact with the User table in the database.
     * @param profileService ProfileService object to interact with the Profile table in the database.
     */
    public GoalService(GoalsRepository goalsRepository, UserService userService, ProfileService profileService, TaskService taskService) {
        this.goalsRepository = goalsRepository;
        this.userService = userService;
        this.profileService = profileService;
        this.taskService = taskService;
    }

    /**
     * Create a new goal for a user
     * @param request  GoalRequest
     * @return GoalResponse
     * @throws Exception if user not found
     */
    public GoalResponse create(GoalRequest request ) throws Exception{
        User employee = request.getUserId() != null ? userService.findUserByUserId(request.getUserId()): null;
        if (employee == null){
            throw  new Exception("User not found with given id "+ request.getUserId());
        }
        User manager= null ;
        if (request.getAssignedById() != null){
            manager = userService.findUserByUserId(request.getAssignedById());
            if (manager == null){
                throw new Exception("Manger is not found with given id "+ request.getAssignedById());
            }
        }

        Goal goal = new Goal();
        goal.setGoalName(request.getGoalName());
        goal.setDescription(request.getDescription());
        goal.setProgress(request.getProgress() != null? request.getProgress():0);
        goal.setDueDate(request.getDueDate());
        goal.setUser(employee);
        goal.setAssignedBy(manager);
        Goal savedGaol = goalsRepository.save(goal);
        // Task associated with goal
        if (request.getTasks() != null){
            request.getTasks().forEach(taskRequest -> taskService.createTask(savedGaol, taskRequest));
        }
        
        return getGoalResponse(savedGaol);

    }

    /**
     * Update a goal for a user
     * @param request  GoalRequest
     * @param goalId  Long
     * @return GoalResponse
     * @throws IllegalArgumentException if goal not found
     */
    public GoalResponse update(GoalRequest request, Long goalId){
        Goal goal = goalsRepository.findById(goalId).orElse(null);
        if (goal == null){
            throw new IllegalArgumentException("Goal not found with given id "+ goalId);
        }
        goal.setStatus(request.getStatus());
        return getGoalResponse(goalsRepository.save(goal));

    }

    /**
     * Get all goals for a user
     * @param userId  Long
     * @return List of GoalResponse
     */
    public List<GoalResponse> getAllGoalsForUser(Long userId){
        User user = userService.findUserByUserId(userId);
        if (user == null){
            throw new IllegalArgumentException("User not found with given id "+ userId);
        }
        List<Goal> goals = goalsRepository.findAllByUser(user);
        return getGoalResponse(goals);
    }

    /**
     * Get all goals assigned by a manager
     * @param managerId  Long
     * @return List of GoalResponse
     */
    public List<GoalResponse> getAllGoalsAssignedByManager(Long managerId){
        User manager = userService.findUserByUserId(managerId);
        if (manager == null){
            throw new IllegalArgumentException("Manager not found with given id "+ managerId);
        }
        List<Goal> goals = goalsRepository.findAllByAssignedBy(manager);
        return getGoalResponse(goals);
    }

    /**
     * Get a goal by its id
     * @param goalId  Long
     * @return GoalResponse
     * @throws IllegalArgumentException if goal not found
     */
    public GoalResponse getGoalById(Long goalId){
        Goal goal = goalsRepository.findById(goalId).orElse(null);
        if (goal == null){
            throw new IllegalArgumentException("Goal not found with given id "+ goalId);
        }
        return getGoalResponse(goal);
    }

    /**
     * This helper method convert List of Goal object to List of GoalResponse object   
     * @param goals  List of Goal
     * @return List of GoalResponse
     */
    private List<GoalResponse> getGoalResponse(List<Goal> goals) {
        return goals.stream().map(this::getGoalResponse).toList();
    }
    

    /**
     * This helper method covert Goal object to GoalResponse object
     * @param savedGoal  Goal
     * @return List of GoalResponse
     */
    private GoalResponse getGoalResponse (Goal savedGoal){
        ProfileResponse employee = profileService.getUserProfile(savedGoal.getUser().getUsername());
        ProfileResponse manager = savedGoal.getAssignedBy() != null ? profileService.getUserProfile(savedGoal.getAssignedBy().getUsername()) : null;

        GoalResponse response = new GoalResponse();
        response.setGoalId(savedGoal.getGoalId());
        response.setGoalName(savedGoal.getGoalName());
        response.setDescription(savedGoal.getDescription());
        response.setProgress(savedGoal.getProgress());
        response.setDueDate(savedGoal.getDueDate());
        response.setStatus(savedGoal.getStatus());
        response.setEmployeeName(employee.getFullName());
        response.setMangerName(manager!=null ? manager.getFullName() : null);
        response.setTasks(taskService.getTasksByGoal(savedGoal));

        return  response;

    }
}
