package com.attendance;

import com.attendance.controller.AttendanceController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(AttendanceController.class)
class AttendanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // ─── /attendance/status ────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /attendance/status returns 200 with 'Service is running'")
    void testGetStatus_Returns200() throws Exception {
        mockMvc.perform(get("/attendance/status")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("Service is running")))
                .andExpect(jsonPath("$.service", is("Attendance Management System")))
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.version", is("1.0.0")));
    }

    // ─── /attendance/checkin ──────────────────────────────────────────────────

    @Test
    @DisplayName("POST /attendance/checkin with employeeId returns 201 with 'Check-in successful'")
    void testCheckIn_WithEmployeeId_Returns201() throws Exception {
        String requestBody = """
                {
                    "employeeId": "EMP-001"
                }
                """;

        mockMvc.perform(post("/attendance/checkin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is("Check-in successful")))
                .andExpect(jsonPath("$.employeeId", is("EMP-001")))
                .andExpect(jsonPath("$.status", is("SUCCESS")))
                .andExpect(jsonPath("$.checkinTime", notNullValue()));
    }

    @Test
    @DisplayName("POST /attendance/checkin without body defaults employeeId to UNKNOWN")
    void testCheckIn_WithoutBody_Returns201WithUnknown() throws Exception {
        mockMvc.perform(post("/attendance/checkin")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is("Check-in successful")))
                .andExpect(jsonPath("$.employeeId", is("UNKNOWN")));
    }

    // ─── /attendance/checkout ─────────────────────────────────────────────────

    @Test
    @DisplayName("POST /attendance/checkout returns 200 with 'Check-out successful'")
    void testCheckOut_Returns200() throws Exception {
        String requestBody = """
                {
                    "employeeId": "EMP-001"
                }
                """;

        mockMvc.perform(post("/attendance/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Check-out successful")))
                .andExpect(jsonPath("$.employeeId", is("EMP-001")))
                .andExpect(jsonPath("$.status", is("SUCCESS")));
    }
}
