package com.example.feedbacksync.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.feedbacksync.payloads.pips.PipRequest;
import com.example.feedbacksync.service.PipService;

@RestController
@RequestMapping("/api/pips")
@CrossOrigin(origins = "http://localhost:5173")
public class PipController {


    private final PipService pipService;

    /**
     * Constructor
     * @param pipService PipService
     */
    public PipController(PipService pipService){
        this.pipService = pipService;
    }

    /**
     * Create a pip
     * @param request PipRequest
     * @return ResponseEntity<?>
     */
    @PostMapping
    public ResponseEntity<?> createPip(@RequestBody PipRequest request){
        try {
            return new ResponseEntity<>(pipService.create(request), HttpStatus.CREATED);
        } catch (Exception e) {
           return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get  pip by id
     * @return ResponseEntity<?>
     */
    @GetMapping("/{pipId}")
    public ResponseEntity<?> getPipById(@PathVariable("pipId") Long pipId){
        try {
            return new ResponseEntity<>(pipService.getPipById(pipId), HttpStatus.OK);
        } catch (Exception e) {
           return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Update a pip
     * @param request PipRequest
     * @return ResponseEntity<?>
     */
    @PutMapping("/{pipId}")
    public ResponseEntity<?> updatePip(@PathVariable("pipId") Long pipId,@RequestBody PipRequest request){
        try {
            return new ResponseEntity<>(pipService.update(request,pipId), HttpStatus.OK);
        } catch (Exception e) {
           return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get all pips of employee
     * @return ResponseEntity<?>
     */
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<?> getPipsByEmployeeId(@PathVariable("employeeId") Long employeeId){
        try {
            return new ResponseEntity<>(pipService.getAllPipsForEmployee(employeeId), HttpStatus.OK);
        } catch (Exception e) {
           return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get all pips of manager
     * @return ResponseEntity<?>
     */
    @GetMapping("/manager/{managerId}")
    public ResponseEntity<?> getPipsByManagerId(@PathVariable("managerId") Long managerId){
        try {
            return new ResponseEntity<>(pipService.getAllPipsAssignedByManager(managerId), HttpStatus.OK);
        } catch (Exception e) {
           return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Delete a pip
     * @param pipId Long
     * @return ResponseEntity<?>
     */
    @DeleteMapping("/{pipId}")
    public ResponseEntity<?> deletePip(@PathVariable("pipId") Long pipId){
        try {
            if (!pipService.deletePip(pipId)) {
                throw new Exception("Pip not found");
            }
            return ResponseEntity.ok("Pip deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }




    
}
