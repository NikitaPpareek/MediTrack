package com.nikita.meditrack.service;

import com.nikita.meditrack.model.Patient;
import com.nikita.meditrack.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class PatientService {
    private final PatientRepository repository;
    public PatientService(PatientRepository repository) { this.repository = repository; }
    public List<Patient> search(String q) {
        if (q == null || q.isBlank()) return repository.findAll();
        return repository.findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(q.trim(), q.trim());
    }
    public Patient create(Patient patient) {
        if (repository.existsByEmailIgnoreCase(patient.getEmail())) throw new ResponseStatusException(HttpStatus.CONFLICT, "A patient with this email already exists");
        return repository.save(patient);
    }
    public Patient get(Long id) { return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found")); }
    public Patient update(Long id, Patient input) {
        Patient current = get(id);
        if (!current.getEmail().equalsIgnoreCase(input.getEmail()) && repository.existsByEmailIgnoreCase(input.getEmail())) throw new ResponseStatusException(HttpStatus.CONFLICT, "A patient with this email already exists");
        current.setFullName(input.getFullName()); current.setDateOfBirth(input.getDateOfBirth()); current.setGender(input.getGender()); current.setEmail(input.getEmail()); current.setPhone(input.getPhone()); current.setNotes(input.getNotes());
        return repository.save(current);
    }
    public void delete(Long id) { repository.delete(get(id)); }
}
