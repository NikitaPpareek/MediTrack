package com.nikita.meditrack.repository;

import com.nikita.meditrack.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientId(Long patientId);
    List<Appointment> findByStatusIgnoreCase(String status);
    long countByStatus(String status);
    @Query("select count(a) from Appointment a where a.appointmentDateTime >= :start and a.appointmentDateTime < :end and a.status <> 'CANCELLED'")
    long countActiveBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
