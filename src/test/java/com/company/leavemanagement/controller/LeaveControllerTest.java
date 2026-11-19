package com.company.leavemanagement.controller;

import com.company.leavemanagement.dto.CreateLeaveRequest;
import com.company.leavemanagement.dto.LeaveResponse;
import com.company.leavemanagement.dto.UpdateLeaveRequest;
import com.company.leavemanagement.exception.LeaveNotFoundException;
import com.company.leavemanagement.service.LeaveService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LeaveController.class)
class LeaveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LeaveService leaveService;

    @Test
    void shouldReturnAllLeaves() throws Exception {
        LeaveResponse response = new LeaveResponse(
                1L, "EMP001", "CASUAL",
                LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 5),
                "Vacation", "PENDING"
        );

        when(leaveService.getAllLeaves()).thenReturn(List.of(response));

        mockMvc.perform(get("/leaves"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].employeeId").value("EMP001"));
    }

    @Test
    void shouldReturnLeaveWhenIdExists() throws Exception {
        LeaveResponse response = new LeaveResponse(
                1L, "EMP001", "CASUAL",
                LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 5),
                "Vacation", "PENDING"
        );

        when(leaveService.getLeaveById(1L)).thenReturn(response);

        mockMvc.perform(get("/leaves/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.employeeId").value("EMP001"))
                .andExpect(jsonPath("$.leaveType").value("CASUAL"));
    }

    @Test
    void shouldReturnNotFoundWhenLeaveDoesNotExist() throws Exception {
        when(leaveService.getLeaveById(99L))
                .thenThrow(new LeaveNotFoundException("Leave request not found with id: 99"));

        mockMvc.perform(get("/leaves/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Leave request not found with id: 99"));
    }

    @Test
    void shouldCreateLeaveSuccessfully() throws Exception {
        LeaveResponse response = new LeaveResponse(
                1L, "EMP001", "CASUAL",
                LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 5),
                "Vacation", "PENDING"
        );

        when(leaveService.createLeave(any(CreateLeaveRequest.class))).thenReturn(response);

        String jsonPayload = """
                {
                    "employeeId": "EMP001",
                    "leaveType": "CASUAL",
                    "startDate": "2026-12-01",
                    "endDate": "2026-12-05",
                    "reason": "Vacation"
                }
                """;

        mockMvc.perform(post("/leaves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.employeeId").value("EMP001"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void shouldReturnBadRequestWhenCreatePayloadIsInvalid() throws Exception {
        // Missing required fields
        String invalidJsonPayload = """
                {
                    "employeeId": "",
                    "leaveType": ""
                }
                """;

        mockMvc.perform(post("/leaves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonPayload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateLeaveSuccessfully() throws Exception {
        LeaveResponse updatedResponse = new LeaveResponse(
                1L, "EMP001", "SICK",
                LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 5),
                "Medical leave", "APPROVED"
        );

        when(leaveService.updateLeave(eq(1L), any(UpdateLeaveRequest.class))).thenReturn(updatedResponse);

        String jsonPayload = """
                {
                    "leaveType": "SICK",
                    "startDate": "2026-12-01",
                    "endDate": "2026-12-05",
                    "reason": "Medical leave",
                    "status": "APPROVED"
                }
                """;

        mockMvc.perform(put("/leaves/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.leaveType").value("SICK"))
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void shouldDeleteLeaveSuccessfully() throws Exception {
        doNothing().when(leaveService).deleteLeave(1L);

        mockMvc.perform(delete("/leaves/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentLeave() throws Exception {
        doThrow(new LeaveNotFoundException("Leave request not found with id: 99"))
                .when(leaveService).deleteLeave(99L);

        mockMvc.perform(delete("/leaves/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Leave request not found with id: 99"));
    }
}
