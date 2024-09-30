package com.example.feedbacksync.controller;

import com.example.feedbacksync.payloads.goals.GoalRequest;
import com.example.feedbacksync.payloads.goals.GoalResponse;
import com.example.feedbacksync.service.GoalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * GoalController class is used to handle requests related to goals.
 * This class is annotated with @RestController annotation to indicate that it is a Spring Rest Controller.
 * The class has a constructor that takes GoalService as an argument.
 * The createGoal method is used to create a new goal.
 * The updateGoal method is used to update an existing goal.
 */
@RestController
@RequestMapping("/api/goals")
public class GoalController {

    private final GoalService goalService;

    /**
     * This is the constructor of the GoalController class.
     * The constructor takes GoalService as an argument.
     * @param goalService GoalService object to interact with the Goal table in the database.
     */
    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    /**
     * This method is used to create a new goal.
     * The method takes a GoalRequest object as a parameter and returns a ResponseEntity.
     * If the goal is created successfully, it returns a ResponseEntity with the goal response and HttpStatus OK.
     * If there is an error in creating the goal, it returns a ResponseEntity with the error message and HttpStatus BAD_REQUEST.
     * @param request GoalRequest object containing the details of the goal to be created.
     * @return ResponseEntity with the goal response or error message.
     */
    @PostMapping
    public ResponseEntity<?> createGoal(@RequestBody GoalRequest request){
        try{
            GoalResponse response =  goalService.create(request);
            return  new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
           return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * This method gets all the goals of a user.
     * The method takes the id of the user as a parameter and returns a ResponseEntity with the list of goals and HttpStatus OK.
     * @param userId Long id of the user whose goals are to be fetched.
     * @return ResponseEntity with the list of goals.
     * @throws Exception if user not found
     */
    @GetMapping("/employee/{userId}")
    public ResponseEntity<?> getGoalsByUserId(@PathVariable("userId") Long userId) throws Exception {
       try{
           return new ResponseEntity<>(goalService.getAllGoalsForUser(userId), HttpStatus.OK);
       } catch (Exception e) {
           return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
       }
    }

    /**
     * This method gets all the goals assigned by a manager.
     * The method takes the id of the manager as a parameter and returns a ResponseEntity with the list of goals and HttpStatus OK.
     * @param managerId Long id of the manager whose assigned goals are to be fetched.
     * @return ResponseEntity with the list of goals.
     * @throws Exception if user not found
     */
    @GetMapping("/manager/{managerId}")
    public ResponseEntity<?> getGoalsByManagerId(@PathVariable("managerId") Long managerId) throws Exception {
        try{
            return new ResponseEntity<>(goalService.getAllGoalsAssignedByManager(managerId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * This method gets a goal by its id.
     * The method takes the id of the goal as a parameter and returns a ResponseEntity with the goal response and HttpStatus OK.
     * If the goal is not found, it returns a ResponseEntity with the error message and HttpStatus BAD_REQUEST.
     * @param id Long id of the goal to be fetched.
     * @return ResponseEntity with the goal response or error message.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getGoalById(@PathVariable("id") Long id){
        try{
            GoalResponse response =  goalService.getGoalById(id);
            return  new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
           return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



    /**
     * This method is used to update an existing goal.
     * The method takes a GoalRequest object and the id of the goal to be updated as parameters.
     * It returns a ResponseEntity with the updated goal response and HttpStatus OK if the goal is updated successfully.
     * If there is an error in updating the goal, it returns a ResponseEntity with the error message and HttpStatus BAD_REQUEST.
     * @param request GoalRequest object containing the details of the goal to be updated.
     * @param id Long id of the goal to be updated.
     * @return ResponseEntity with the updated goal response or error message.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateGoal(@RequestBody GoalRequest request, @PathVariable("id") Long id){
        try{
            GoalResponse response =  goalService.update(request, id);
            return  new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
           return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}
