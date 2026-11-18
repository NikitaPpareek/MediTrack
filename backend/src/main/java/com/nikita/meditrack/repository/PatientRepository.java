package com.nikita.meditrack.repository;

import com.nikita.meditrack.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email);
    boolean existsByEmailIgnoreCase(String email);
}
