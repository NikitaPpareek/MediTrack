package com.nikita.meditrack.controller;

import com.nikita.meditrack.model.Patient;
import com.nikita.meditrack.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {
    private final PatientService service;
    public PatientController(PatientService service) { this.service = service; }
    @GetMapping public List<Patient> list(@RequestParam(required = false) String q) { return service.search(q); }
    @GetMapping("/{id}") public Patient get(@PathVariable Long id) { return service.get(id); }
    @PostMapping public Patient create(@Valid @RequestBody Patient patient) { return service.create(patient); }
    @PutMapping("/{id}") public Patient update(@PathVariable Long id, @Valid @RequestBody Patient patient) { return service.update(id, patient); }
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { service.delete(id); }
}
