package com.nikita.meditrack.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false) @JoinColumn(name = "patient_id") private Patient patient;
    @NotBlank @Size(max = 100) private String providerName;
    @NotNull @Future private LocalDateTime appointmentDateTime;
    @NotBlank @Pattern(regexp = "SCHEDULED|COMPLETED|CANCELLED", message = "Status must be SCHEDULED, COMPLETED, or CANCELLED") private String status = "SCHEDULED";
    @Size(max = 500) private String reason;

    public Appointment() {}
    public Long getId() { return id; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public String getProviderName() { return providerName; }
    public void setProviderName(String providerName) { this.providerName = providerName; }
    public LocalDateTime getAppointmentDateTime() { return appointmentDateTime; }
    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) { this.appointmentDateTime = appointmentDateTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
