package com.example.feedbacksync.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.example.feedbacksync.payloads.task.TaskResponse;
import org.springframework.stereotype.Service;

import com.example.feedbacksync.entity.Pip;
import com.example.feedbacksync.entity.User;
import com.example.feedbacksync.payloads.pips.PipRequest;
import com.example.feedbacksync.payloads.pips.PipResponse;
import com.example.feedbacksync.payloads.profile.ProfileResponse;
import com.example.feedbacksync.payloads.task.TaskRequest;
import com.example.feedbacksync.repository.PipRepository;

@Service
public class PipService {

    private final PipRepository pipRepository;

    private final UserService userService;

    private final ProfileService profileService;
    private final TaskService taskService;

    /**
     * Constructor for PipService
     * @param pipRepository PipRepository
     * @param userService UserService
     * @param profileService ProfileService
     * @param taskService TaskService
     */
    public PipService(PipRepository pipRepository, UserService userService, ProfileService profileService, TaskService taskService) {
        this.pipRepository = pipRepository;
        this.userService = userService;
        this.profileService = profileService;
        this.taskService = taskService;
    }

    /**
     * Create a new pip for a user
     * @param request PipRequest
     * @return PipResponse
     * @throws Exception if user not found
     */
    public PipResponse create(PipRequest request ) throws Exception{
        User employee = request.getEmployeeId() != null ? userService.findUserByUserId(request.getEmployeeId()): null;
        if (employee == null){
            throw  new Exception("User not found with given id "+ request.getEmployeeId());
        }
        User manager= request.getManagerId() != null ? userService.findUserByUserId(request.getManagerId()): null;
        if (manager == null){
            throw new Exception("Manger is not found with given id "+ request.getManagerId());
        }

        Pip pip = new Pip();
        pip.setEmployee(employee);
        pip.setManager(manager);
        pip.setTitle(request.getTitle());
        pip.setStartDate(request.getStartDate() != null ? request.getStartDate() : LocalDateTime.now());
        pip.setEndDate(request.getEndDate());
        pip.setSupport(request.getSupport());
        pip.setOutcome(request.getOutcome());
        
        Pip savedPip= pipRepository.save(pip);
        if (request.getTasks() != null){
            for (TaskRequest taskRequest: request.getTasks()){
                taskService.createTask(savedPip, taskRequest);
            }
        }

        return getPipResponse(pip);

    }

    /**
     * Get Pip by id
     * @param id Long id of the pip
     * @return Pip
     * @throws Exception if pip not found
     */
    public PipResponse getPipById(Long id) throws Exception{
        Pip pip =  pipRepository.findById(id).orElseThrow(() -> new Exception("Pip not found with given id "+ id));
        return getPipResponse(pip);
    }

    /**
     * Update a pip for a user
     * @param request PipRequest
     * @param pipId Long
     * @return PipResponse
     * @throws IllegalArgumentException if pip not found
     */
    public PipResponse update(PipRequest request, Long pipId){
        Pip pip = pipRepository.findById(pipId).orElse(null);
        if (pip == null){
            throw new IllegalArgumentException("Pip not found with given id "+ pipId);
        }
        pip.setEndDate(request.getEndDate() != null ? request.getEndDate() : pip.getEndDate());
        pip.setStartDate(request.getStartDate() != null ? request.getStartDate() : pip.getStartDate());
        pip.setOutcome(request.getOutcome() != null ? request.getOutcome() : pip.getOutcome());
        pip.setSupport(request.getSupport() != null ? request.getSupport() : pip.getSupport());
        pip.setTitle(request.getTitle() != null ? request.getTitle() : pip.getTitle());
        pip.setStatus(request.getStatus() != null ? request.getStatus() : pip.getStatus());
        
        return getPipResponse(pipRepository.save(pip));
    }

    /**
     * Delete a pip by pipId
     * @param pipId Long
     * @return boolean
     */
    public boolean deletePip(Long pipId){
        Pip pip = pipRepository.findById(pipId).orElse(null);
        if (pip == null){
            return false;
        }
       List<TaskResponse> tasks = taskService.getTasksByPip(pip);
        for (TaskResponse task: tasks){
            taskService.deleteTask(task.getTaskId());
        }


        pipRepository.delete(pip);
        return true;
    } 
    
    /**
     * Get all pips for a user
     * @param userId Long
     * @return List of PipResponse
     */
    public List<PipResponse> getAllPipsForEmployee(Long userId){
        User user = userService.findUserByUserId(userId);
        if (user == null){
            throw new IllegalArgumentException("User not found with given id "+ userId);
        }
        List<Pip> pips = pipRepository.findAllByEmployee(user);
        return getPipResponse(pips);
    }

    /**
     * Get all pips assigned by a manager
     * @param managerId Long
     * @return List of PipResponse
     */
    public List<PipResponse> getAllPipsAssignedByManager(Long managerId){
        User manager = userService.findUserByUserId(managerId);
        if (manager == null){
            throw new IllegalArgumentException("Manager not found with given id "+ managerId);
        }
        List<Pip> pips = pipRepository.findAllByManager(manager);
        return getPipResponse(pips);
    }

    /**
     * Get List of PipResponse from List of Pip
     * @param  pips List of Pip
     * @return List of PipResponse
     */
    public List<PipResponse> getPipResponse(List<Pip> pips){
        return pips.stream().map(this::getPipResponse).collect(Collectors.toList());
    }
    /**
     * Get PipResponse from Pip
     * @param pip Pip
     * @return PipResponse
     */
    public PipResponse getPipResponse(Pip pip){

        ProfileResponse employeeProfile = profileService.getUserProfile(pip.getEmployee().getUsername());
        ProfileResponse managerProfile = profileService.getUserProfile(pip.getManager().getUsername());
        

        PipResponse response = new PipResponse();
        response.setPipId(pip.getPipId());
        response.setTitle(pip.getTitle());
        response.setStartDate(pip.getStartDate());
        response.setEndDate(pip.getEndDate());
        response.setSupport(pip.getSupport());
        response.setOutcome(pip.getOutcome());
        response.setStatus(pip.getStatus());
        response.setProgress(pip.getProgress());
        response.setEmployeeName(employeeProfile.getFullName() != null ? employeeProfile.getFullName() : pip.getEmployee().getUsername());
        response.setManagerName(managerProfile.getFullName() != null ? managerProfile.getFullName() : pip.getManager().getUsername());
        response.setTasks(taskService.getTasksByPip(pip));
        return response;
    }
    
}
