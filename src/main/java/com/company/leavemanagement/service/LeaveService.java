package com.company.leavemanagement.service;

import com.company.leavemanagement.dto.CreateLeaveRequest;
import com.company.leavemanagement.dto.LeaveResponse;
import com.company.leavemanagement.dto.UpdateLeaveRequest;
import com.company.leavemanagement.exception.LeaveNotFoundException;
import com.company.leavemanagement.model.LeaveRequest;
import com.company.leavemanagement.repository.LeaveRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LeaveService {

    private final LeaveRepository leaveRepository;

    public LeaveService(LeaveRepository leaveRepository) {
        this.leaveRepository = leaveRepository;
    }

    public List<LeaveResponse> getAllLeaves() {
        List<LeaveRequest> leaves = leaveRepository.findAll();
        List<LeaveResponse> responses = new ArrayList<>();
        for (LeaveRequest leave : leaves) {
            responses.add(mapToResponse(leave));
        }
        return responses;
    }

    public LeaveResponse getLeaveById(Long id) {
        LeaveRequest leave = leaveRepository.findById(id)
                .orElseThrow(() -> new LeaveNotFoundException("Leave request not found with id: " + id));
        return mapToResponse(leave);
    }

    public LeaveResponse createLeave(CreateLeaveRequest request) {
        LeaveRequest leave = new LeaveRequest();
        leave.setEmployeeId(request.getEmployeeId());
        leave.setLeaveType(request.getLeaveType());
        leave.setStartDate(request.getStartDate());
        leave.setEndDate(request.getEndDate());
        leave.setReason(request.getReason());
        leave.setStatus("PENDING");

        LeaveRequest savedLeave = leaveRepository.save(leave);
        return mapToResponse(savedLeave);
    }

    public LeaveResponse updateLeave(Long id, UpdateLeaveRequest request) {
        LeaveRequest existingLeave = leaveRepository.findById(id)
                .orElseThrow(() -> new LeaveNotFoundException("Leave request not found with id: " + id));

        existingLeave.setLeaveType(request.getLeaveType());
        existingLeave.setStartDate(request.getStartDate());
        existingLeave.setEndDate(request.getEndDate());
        existingLeave.setReason(request.getReason());
        existingLeave.setStatus(request.getStatus());

        LeaveRequest updatedLeave = leaveRepository.save(existingLeave);
        return mapToResponse(updatedLeave);
    }

    public void deleteLeave(Long id) {
        if (!leaveRepository.existsById(id)) {
            throw new LeaveNotFoundException("Leave request not found with id: " + id);
        }
        leaveRepository.deleteById(id);
    }

    private LeaveResponse mapToResponse(LeaveRequest leave) {
        return new LeaveResponse(
                leave.getId(),
                leave.getEmployeeId(),
                leave.getLeaveType(),
                leave.getStartDate(),
                leave.getEndDate(),
                leave.getReason(),
                leave.getStatus()
        );
    }
}
