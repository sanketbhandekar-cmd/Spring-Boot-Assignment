package com.company.leavemanagement.service;

import com.company.leavemanagement.dto.CreateLeaveRequest;
import com.company.leavemanagement.dto.LeaveResponse;
import com.company.leavemanagement.dto.UpdateLeaveRequest;
import com.company.leavemanagement.exception.LeaveNotFoundException;
import com.company.leavemanagement.model.LeaveRequest;
import com.company.leavemanagement.repository.LeaveRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LeaveServiceTest {

    @Mock
    private LeaveRepository leaveRepository;

    @InjectMocks
    private LeaveService leaveService;

    @Test
    void shouldCreateLeaveSuccessfully() {
        CreateLeaveRequest request = new CreateLeaveRequest(
                "EMP001",
                "CASUAL",
                LocalDate.of(2026, 12, 1),
                LocalDate.of(2026, 12, 5),
                "Family vacation"
        );

        LeaveRequest savedLeave = new LeaveRequest(
                1L,
                "EMP001",
                "CASUAL",
                LocalDate.of(2026, 12, 1),
                LocalDate.of(2026, 12, 5),
                "Family vacation",
                "PENDING"
        );

        when(leaveRepository.save(any(LeaveRequest.class))).thenReturn(savedLeave);

        LeaveResponse response = leaveService.createLeave(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("EMP001", response.getEmployeeId());
        assertEquals("PENDING", response.getStatus());
        verify(leaveRepository).save(any(LeaveRequest.class));
    }

    @Test
    void shouldReturnAllLeaves() {
        LeaveRequest leave1 = new LeaveRequest(
                1L, "EMP001", "CASUAL",
                LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 5),
                "Vacation", "PENDING"
        );
        LeaveRequest leave2 = new LeaveRequest(
                2L, "EMP002", "SICK",
                LocalDate.of(2026, 12, 2), LocalDate.of(2026, 12, 3),
                "Fever", "APPROVED"
        );

        when(leaveRepository.findAll()).thenReturn(List.of(leave1, leave2));

        List<LeaveResponse> responses = leaveService.getAllLeaves();

        assertEquals(2, responses.size());
        assertEquals("EMP001", responses.get(0).getEmployeeId());
        assertEquals("EMP002", responses.get(1).getEmployeeId());
    }

    @Test
    void shouldReturnLeaveWhenIdExists() {
        LeaveRequest leave = new LeaveRequest(
                1L, "EMP001", "CASUAL",
                LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 5),
                "Vacation", "PENDING"
        );

        when(leaveRepository.findById(1L)).thenReturn(Optional.of(leave));

        LeaveResponse response = leaveService.getLeaveById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("EMP001", response.getEmployeeId());
    }

    @Test
    void shouldThrowExceptionWhenLeaveNotFoundById() {
        when(leaveRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(LeaveNotFoundException.class, () -> leaveService.getLeaveById(99L));
    }

    @Test
    void shouldUpdateLeaveSuccessfully() {
        LeaveRequest existingLeave = new LeaveRequest(
                1L, "EMP001", "CASUAL",
                LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 5),
                "Vacation", "PENDING"
        );

        UpdateLeaveRequest updateRequest = new UpdateLeaveRequest(
                "SICK",
                LocalDate.of(2026, 12, 1),
                LocalDate.of(2026, 12, 6),
                "Updated reason",
                "APPROVED"
        );

        LeaveRequest updatedLeave = new LeaveRequest(
                1L, "EMP001", "SICK",
                LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 6),
                "Updated reason", "APPROVED"
        );

        when(leaveRepository.findById(1L)).thenReturn(Optional.of(existingLeave));
        when(leaveRepository.save(existingLeave)).thenReturn(updatedLeave);

        LeaveResponse response = leaveService.updateLeave(1L, updateRequest);

        assertNotNull(response);
        assertEquals("SICK", response.getLeaveType());
        assertEquals("APPROVED", response.getStatus());
        assertEquals("Updated reason", response.getReason());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentLeave() {
        UpdateLeaveRequest updateRequest = new UpdateLeaveRequest(
                "SICK",
                LocalDate.of(2026, 12, 1),
                LocalDate.of(2026, 12, 6),
                "Updated reason",
                "APPROVED"
        );

        when(leaveRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(LeaveNotFoundException.class, () -> leaveService.updateLeave(99L, updateRequest));
    }

    @Test
    void shouldDeleteLeaveSuccessfully() {
        when(leaveRepository.existsById(1L)).thenReturn(true);

        leaveService.deleteLeave(1L);

        verify(leaveRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentLeave() {
        when(leaveRepository.existsById(99L)).thenReturn(false);

        assertThrows(LeaveNotFoundException.class, () -> leaveService.deleteLeave(99L));
    }
}
