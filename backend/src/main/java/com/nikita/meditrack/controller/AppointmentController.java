package com.nikita.meditrack.controller;

import com.nikita.meditrack.model.Appointment;
import com.nikita.meditrack.repository.AppointmentRepository;
import com.nikita.meditrack.repository.PatientRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    private final AppointmentRepository appointments;
    private final PatientRepository patients;
    public AppointmentController(AppointmentRepository appointments, PatientRepository patients) { this.appointments = appointments; this.patients = patients; }
    @GetMapping public List<Appointment> list(@RequestParam(required = false) String status, @RequestParam(required = false) Long patientId) {
        if (patientId != null) return appointments.findByPatientId(patientId);
        if (status != null && !status.isBlank()) return appointments.findByStatusIgnoreCase(status);
        return appointments.findAll();
    }
    @PostMapping public Appointment create(@Valid @RequestBody Appointment input) {
        if (input.getPatient() == null || input.getPatient().getId() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "patient.id is required");
        input.setPatient(patients.findById(input.getPatient().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found")));
        if (input.getStatus() == null || input.getStatus().isBlank()) input.setStatus("SCHEDULED");
        return appointments.save(input);
    }
    @PatchMapping("/{id}/status") public Appointment updateStatus(@PathVariable Long id, @RequestParam String status) {
        if (!List.of("SCHEDULED", "COMPLETED", "CANCELLED").contains(status.toUpperCase())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid appointment status");
        Appointment a = appointments.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found"));
        a.setStatus(status.toUpperCase()); return appointments.save(a);
    }
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { appointments.delete(appointments.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found"))); }
}
