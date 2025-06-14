package com.voyagerss.api.controller.admin;

import com.voyagerss.api.service.ArrangeService;
import com.voyagerss.persist.dto.TaskEmployeeDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/arrange")
@RequiredArgsConstructor
@Slf4j
public class ArrangeController {
    
    private final ArrangeService arrangeService;
    
    /**
     * Generate TaskEmployee entities based on the 6 rules
     * 
     * @param teamId The team ID
     * @param taskId The task ID
     * @param targetDate The target date for assignment
     * @return List of created TaskEmployee DTOs
     */
    @PostMapping("/generate-task-employees")
    public ResponseEntity<List<TaskEmployeeDTO>> generateTaskEmployees(
            @RequestParam Long teamId,
            @RequestParam Long taskId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate) {
        
        try {
            log.info("Generating TaskEmployee entities for team: {}, task: {}, date: {}", 
                    teamId, taskId, targetDate);
            
            List<TaskEmployeeDTO> taskEmployees = arrangeService.generateTaskEmployees(teamId, taskId, targetDate);
            
            log.info("Successfully generated {} TaskEmployee entities", taskEmployees.size());
            return ResponseEntity.ok(taskEmployees);
            
        } catch (Exception e) {
            log.error("Error generating TaskEmployee entities: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Generate monthly attendance schedule
     * 
     * @param teamId The team ID
     * @param year The year
     * @param month The month
     * @return Success response
     */
    @PostMapping("/generate-monthly-attendance")
    public ResponseEntity<String> generateMonthlyAttendance(
            @RequestParam Long teamId,
            @RequestParam int year,
            @RequestParam int month) {
        
        try {
            log.info("Generating monthly attendance for team: {}, year: {}, month: {}", 
                    teamId, year, month);
            
            arrangeService.generateMonthlyAttendance(teamId, year, month);
            
            log.info("Successfully generated monthly attendance");
            return ResponseEntity.ok("Monthly attendance generated successfully");
            
        } catch (Exception e) {
            log.error("Error generating monthly attendance: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
} 