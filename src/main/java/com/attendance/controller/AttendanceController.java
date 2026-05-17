package com.attendance.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    /**
     * GET /attendance/status
     * Health-check / status endpoint.
     * Also used as the ALB target group health-check path.
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> getStatus() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "Service is running");
        response.put("timestamp", LocalDateTime.now()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        response.put("service", "Attendance Management System");
        response.put("version", "1.0.0");
        return ResponseEntity.ok(response);
    }

    /**
     * POST /attendance/checkin
     * Records an employee check-in.
     */
    @PostMapping("/checkin")
    public ResponseEntity<Map<String, String>> checkIn(
            @RequestBody(required = false) Map<String, String> payload) {

        String employeeId = (payload != null && payload.containsKey("employeeId"))
                ? payload.get("employeeId")
                : "UNKNOWN";

        Map<String, String> response = new HashMap<>();
        response.put("message", "Check-in successful");
        response.put("employeeId", employeeId);
        response.put("checkinTime", LocalDateTime.now()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        response.put("status", "SUCCESS");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /attendance/checkout  (bonus endpoint for completeness)
     */
    @PostMapping("/checkout")
    public ResponseEntity<Map<String, String>> checkOut(
            @RequestBody(required = false) Map<String, String> payload) {

        String employeeId = (payload != null && payload.containsKey("employeeId"))
                ? payload.get("employeeId")
                : "UNKNOWN";

        Map<String, String> response = new HashMap<>();
        response.put("message", "Check-out successful");
        response.put("employeeId", employeeId);
        response.put("checkoutTime", LocalDateTime.now()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        response.put("status", "SUCCESS");

        return ResponseEntity.ok(response);
    }
}
