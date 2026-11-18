package com.nikita.meditrack.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "patients")
public class Patient {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank @Size(max = 80) private String fullName;
    @NotNull @Past private LocalDate dateOfBirth;
    @NotBlank @Pattern(regexp = "MALE|FEMALE|OTHER", message = "Gender must be MALE, FEMALE, or OTHER") private String gender;
    @NotBlank @Email @Column(unique = true) private String email;
    @Size(max = 25) private String phone;
    @Size(max = 500) private String notes;

    public Patient() {}
    public Patient(String fullName, LocalDate dateOfBirth, String gender, String email, String phone, String notes) {
        this.fullName = fullName; this.dateOfBirth = dateOfBirth; this.gender = gender;
        this.email = email; this.phone = phone; this.notes = notes;
    }
    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
