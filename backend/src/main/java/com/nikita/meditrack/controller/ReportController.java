package com.nikita.meditrack.controller;

import com.nikita.meditrack.repository.AppointmentRepository;
import com.nikita.meditrack.repository.PatientRepository;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final PatientRepository patients;
    private final AppointmentRepository appointments;
    public ReportController(PatientRepository patients, AppointmentRepository appointments) { this.patients = patients; this.appointments = appointments; }
    @GetMapping("/summary") public Map<String, Object> summary() {
        LocalDate today = LocalDate.now();
        return Map.of("totalPatients", patients.count(), "totalAppointments", appointments.count(), "scheduledAppointments", appointments.countByStatus("SCHEDULED"), "completedAppointments", appointments.countByStatus("COMPLETED"), "appointmentsToday", appointments.countActiveBetween(today.atStartOfDay(), today.plusDays(1).atStartOfDay()));
    }
    @GetMapping("/appointments-by-status") public Map<String, Long> byStatus() {
        return Map.of("SCHEDULED", appointments.countByStatus("SCHEDULED"), "COMPLETED", appointments.countByStatus("COMPLETED"), "CANCELLED", appointments.countByStatus("CANCELLED"));
    }
}
