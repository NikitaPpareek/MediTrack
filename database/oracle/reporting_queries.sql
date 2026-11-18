-- 1. Total patient count
SELECT COUNT(*) AS total_patients FROM patients;

-- 2. Appointment counts by status
SELECT status, COUNT(*) AS appointment_count
FROM appointments
GROUP BY status
ORDER BY status;

-- 3. Upcoming scheduled appointments with patient details
SELECT a.id AS appointment_id,
       p.full_name AS patient_name,
       p.email,
       a.provider_name,
       a.appointment_datetime,
       a.status
FROM appointments a
JOIN patients p ON p.id = a.patient_id
WHERE a.status = 'SCHEDULED'
  AND a.appointment_datetime >= CURRENT_TIMESTAMP
ORDER BY a.appointment_datetime;

-- 4. Patients with no appointments (LEFT JOIN / NULL filtering)
SELECT p.id, p.full_name, p.email
FROM patients p
LEFT JOIN appointments a ON a.patient_id = p.id
WHERE a.id IS NULL
ORDER BY p.full_name;

-- 5. Monthly appointment summary
SELECT TRUNC(CAST(a.appointment_datetime AS DATE), 'MM') AS appointment_month,
       a.status,
       COUNT(*) AS appointment_count
FROM appointments a
GROUP BY TRUNC(CAST(a.appointment_datetime AS DATE), 'MM'), a.status
ORDER BY appointment_month, a.status;

-- 6. Providers with the highest number of appointments
SELECT provider_name, COUNT(*) AS appointment_count,
       DENSE_RANK() OVER (ORDER BY COUNT(*) DESC) AS provider_rank
FROM appointments
GROUP BY provider_name
ORDER BY provider_rank, provider_name;

-- 7. Patients with more than one appointment
SELECT p.id, p.full_name, COUNT(a.id) AS appointment_count
FROM patients p
JOIN appointments a ON a.patient_id = p.id
GROUP BY p.id, p.full_name
HAVING COUNT(a.id) > 1
ORDER BY appointment_count DESC;
