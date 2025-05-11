-- Populate data in database tables

-- Insert data employees
INSERT INTO "employee" (employee_id, surname, first_name, address, telephone_nr, "isActive", "createdAt", role) VALUES
  (1, 'Kevin', 'Murinda Ganza', 'Kicukiro', '0785775030', TRUE, NOW(), 'DOCTOR'),
  (2, 'Hyguette', 'Imfura', 'Nyarugenge', '0787313036', TRUE, NOW(), 'NURSE'),
  (3, 'Kelly', 'Gwiza', 'Gasabo', '0781951038', TRUE, NOW(), 'DOCTOR'),
  (4, 'Nadine', 'Tuyizere', 'Gisenyi', '0788895685', TRUE, NOW(), 'NURSE');

-- Insert doctors
INSERT INTO "doctor" (doctor_id, employee_id, specialty) VALUES
  (1, 1, 'Cardiology'),
  (2, 3, 'Ophthalmologist');

-- Insert departments
INSERT INTO "department" (department_code, name, building, director_id, "createdAt") VALUES
  (1, 'Internal Medicine', 'Building A', 1, NOW()),
  (2, 'Ophthalmology', 'Building B', 2, NOW());

-- Insert nurses
INSERT INTO "nurse" (nurse_id, employee_id, rotation, salary, department_id) VALUES
  (1, 2, 'Day', 3000.00, 1),
  (2, 4, 'Night', 3200.00, 2);

-- Insert wards
INSERT INTO "ward" (ward_id, ward_nr, nr_of_beds, department_id, supervisor_id, "createdAt") VALUES
  (1,1, 3, 1, 1, NOW()),
  (2,2, 5, 2, 2, NOW()),
  (3,3, 7, 2, 2, NOW()), -- edge case to check constraint
  (4,1, 3, 1, 1, NOW()); -- edge case to check local ward per department

-- Insert patient
INSERT INTO patient(patient_id, surname, first_name, address, telephone_nr, "createdAt") VALUES
  (1, 'Grace', 'Karigirwa', 'Kicukiro', '0788511954', NOW());

-- Insert hospitalizations (assuming patient_id = 1 will be added)
INSERT INTO "hospitalization" (hospitalization_id, doctor_id, patient_id, ward_id, bed_nr, diagnosis, description, admission_date, discharge_date) VALUES
  (1, 1, 1, 1, 1, 'Flu', 'Mild case of flu', NOW(), NOW() + INTERVAL '5 days');

-- Insert transfers
INSERT INTO "transfer" (transfer_id, from_hospitalization_id, to_hospitalization_id, authorized_by, transfer_date, transfer_reason) VALUES
  (1, 1, 1, 1, NOW() + INTERVAL '5 days', 'Further observation');

-- View : Select records

SELECT * FROM employee;

SELECT * FROM doctor;

SELECT * FROM nurse;

SELECT * FROM department;

SELECT * FROM ward;

SELECT * FROM patient;

SELECT * FROM hospitalization;

SELECT * FROM transfer;