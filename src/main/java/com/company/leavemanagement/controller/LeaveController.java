package com.company.leavemanagement.controller;

import com.company.leavemanagement.dto.CreateLeaveRequest;
import com.company.leavemanagement.dto.LeaveResponse;
import com.company.leavemanagement.dto.UpdateLeaveRequest;
import com.company.leavemanagement.service.LeaveService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/leaves")
public class LeaveController {

    private final LeaveService leaveService;

    // Constructor-based Dependency Injection
    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    // Handles GET /leaves
    @GetMapping
    public List<LeaveResponse> getAllLeaves() {
        return leaveService.getAllLeaves();
    }

    // Handles GET /leaves/{id}
    @GetMapping("/{id}")
    public LeaveResponse getLeaveById(@PathVariable Long id) {
        return leaveService.getLeaveById(id);
    }

    // Handles POST /leaves
    @PostMapping
    public ResponseEntity<LeaveResponse> createLeave(@Valid @RequestBody CreateLeaveRequest request) {
        LeaveResponse response = leaveService.createLeave(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Handles PUT /leaves/{id}
    @PutMapping("/{id}")
    public LeaveResponse updateLeave(@PathVariable Long id, @Valid @RequestBody UpdateLeaveRequest request) {
        return leaveService.updateLeave(id, request);
    }

    // Handles DELETE /leaves/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeave(@PathVariable Long id) {
        leaveService.deleteLeave(id);
        return ResponseEntity.noContent().build();
    }
}
